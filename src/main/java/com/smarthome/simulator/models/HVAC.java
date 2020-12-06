package com.smarthome.simulator.models;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.exceptions.ModuleException;
import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHH;
import com.smarthome.simulator.utils.DelayedRunnable;
import com.smarthome.simulator.utils.Logger;
import com.smarthome.simulator.utils.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The HVAC class represents the Heating Ventilation Air Conditioning system.
 */
public class HVAC {

    /**
     * This is a pointer to the simulation instance
     */
    private final Simulation simulation;

    /**
     * The is a pointer to the shh instance
     */
    private final SHH shh;

    /**
     * This is a Mapping of Room -> State
     */
    private final Map<Room, HVACState> roomStates = new HashMap<>();

    /**
     * Enum representing the various states that HVAC can have
     */
    private enum HVACState {
        STOPPED,
        PAUSED,
        RUNNING
    };

    /**
     * The rate at which the temperature changes with HVAC on
     */
    private static final float HVAC_RATE = 0.1f;

    /**
     * The rate at which the temperature changes with HVAC off
     */
    private static final float STOPPED_RATE = 0.05f;

    /**
     * The temperature threshold/difference before HVAC turns back on
     */
    private static final float TEMP_THRESHOLD = 0.25f;

    /**
     * The alerting minimum temperature point
     */
    private static final float MINIMUM_ALERT_TEMP = 0f;

    /**
     * The alerting maximum temperature point
     */
    private static final float MAXIMUM_ALERT_TEMP = 50f;

    /**
     * The parameterized constructor for the HVAC class
     * @param simulation pointer to the existing simulation instance
     * @param shh pointer to the existing shh instance
     */
    public HVAC(Simulation simulation, SHH shh) {
        super();
        this.shh = shh;
        this.simulation = simulation;
    }

    // ============================  Other methods  ============================

    /**
     * This function is responsible for changing the temperature of the rooms with the correct rate.
     * @param shh pointer to the existing shh instance
     * @param simulation pointer to the existing simulation instance
     */
    private void changeTemp(SHH shh, Simulation simulation) {

        //SmartHomeSimulator.LOGGER.log(Logger.DEBUG, "SHH", "Test");


        HouseLayout house = simulation.getHouseLayout();
        if (house != null) {
            List<Room> rooms = house.getRooms();

            boolean isSummer = simulation.getCurrentSeason().equals(Simulation.SUMMER);
            boolean isWinter = simulation.getCurrentSeason().equals(Simulation.WINTER);

            for (Room room : rooms) {

                // Add this room to state map if not present, HVAC stopped at start
                roomStates.putIfAbsent(room, HVACState.STOPPED);

                float roomTemp = room.getTemperature();
                AtomicReference<Float> desiredTemp = new AtomicReference<>(simulation.getCurrentSeason().equals(Simulation.SUMMER) ? shh.getSummerTemperature() : shh.getWinterTemperature());
                if (room.isZoneTempOverridden())
                    desiredTemp.set(room.getDesiredTemperature());
                else {
                    shh.getZones()
                            .stream()
                            .filter(zone -> zone.getRooms().contains(room))
                            .findFirst()
                            .ifPresent(zone -> {
                                for (Period p : zone.getPeriods()) {
                                    if (TimeUtil.isInRange(simulation.getTime(), p.getStartTimeObj(), p.getEndTimeObj())) {
                                        desiredTemp.set(p.getDesiredTemperature());
                                        break;
                                    }
                                }
                            });
                }
                float tempDiff = desiredTemp.get() - roomTemp;

                boolean belowDesired = roomTemp < desiredTemp.get();
                boolean aboveDesired = roomTemp > desiredTemp.get();
                boolean outsideCoolerThanInside = simulation.getTemperatureOutside() < roomTemp;
                boolean outsideHotterThanInside = simulation.getTemperatureOutside() > roomTemp;
                boolean paused = true;

                // Check critical temp
                if ((roomTemp <= MINIMUM_ALERT_TEMP || roomTemp >= MAXIMUM_ALERT_TEMP) && !room.isCriticalTempLogged()) {
                    room.setCriticalTempLogged(true);
                    SmartHomeSimulator.LOGGER.log(Logger.WARN, "SHH", "Critical temperature: " + roomTemp + " in room " + room.getName());
                } else if (roomTemp > MINIMUM_ALERT_TEMP && roomTemp < MAXIMUM_ALERT_TEMP && room.isCriticalTempLogged()) {
                    room.setCriticalTempLogged(false);
                }

                // Away mode temp control
                if (simulation.isAway()) {
                    float winterTemp = shh.getWinterTemperature();
                    float summerTemp = shh.getSummerTemperature();
                    if (isWinter && winterTemp != room.getTemperature()) {
                        if (winterTemp > roomTemp) {
                            room.setTemperature((winterTemp - roomTemp < HVAC_RATE) ? winterTemp : roomTemp + HVAC_RATE);
                        } else if (winterTemp < roomTemp) {
                            room.setTemperature((winterTemp - roomTemp < HVAC_RATE) ? winterTemp : roomTemp - HVAC_RATE);
                        }
                    } else if (isSummer && summerTemp != room.getTemperature()) {
                        if (summerTemp > roomTemp) {
                            room.setTemperature((summerTemp - roomTemp < HVAC_RATE) ? summerTemp : roomTemp + HVAC_RATE);
                        } else if (summerTemp < roomTemp) {
                            room.setTemperature((summerTemp - roomTemp < HVAC_RATE) ? summerTemp : roomTemp - HVAC_RATE);
                        }
                    }
                } else {

                    // Open windows in summer
                    AtomicBoolean windowOpen = new AtomicBoolean(room.getWindows().stream().anyMatch(Window::isOpen));

                    // Summer, outside temp is cooler, actual temp > desired temp
                    if (isSummer && !outsideHotterThanInside && aboveDesired) {

                        // If room temp > outside temp, try to open a window
                        if (roomTemp > simulation.getTemperatureOutside() && !windowOpen.get()) {

                            // If no windows are open, attempt to open at least one
                            for (Window window: room.getWindows()) {
                                Map<String, Object> payload = new HashMap<String, Object>();
                                payload.put("id", window.getId());
                                payload.put("blocked", window.isBlocked());
                                payload.put("open", true);
                                try {
                                    simulation.executeCommand(SHC.CONTROL_WINDOW, payload, false);
                                }catch(ModuleException e){}
                                if (window.isOpen()) {
                                    windowOpen.set(true);
                                    break;
                                }
                            }

                            // If a window is open in the room, change temp by -0.05
                            // Else, HVAC has to run to cool the room, change temp by -0.1
                            roomStates.put(room, windowOpen.get() ? HVACState.STOPPED : HVACState.RUNNING);

                        // Otherwise, force HVAC to run and close all windows to get to desired temp
                        } else {
                            room.getWindows().forEach(window -> {
                                Map<String, Object> payload = new HashMap<String, Object>();
                                payload.put("id", window.getId());
                                payload.put("blocked", window.isBlocked());
                                payload.put("open", false);
                                try {
                                    simulation.executeCommand(SHC.CONTROL_WINDOW, payload, false);
                                }catch(ModuleException e){};
                            });
                            roomStates.put(room, HVACState.RUNNING);
                        }

                    // Otherwise, if we've reach desired temp, pause HVAC
                    } else if (room.getTemperature() == desiredTemp.get())
                        roomStates.put(room, HVACState.PAUSED);

                    // Abs temp diff > 0.25 and HVAC is paused
                    else if (Math.abs(tempDiff) > TEMP_THRESHOLD && roomStates.get(room) == HVACState.PAUSED)
                        roomStates.put(room, HVACState.RUNNING);

                    // If HVAC is running
                    if (roomStates.get(room) == HVACState.RUNNING) {

                        // Adjust temperature accordingly (+/-)
                        if (belowDesired)
                            room.setTemperature((Math.abs(tempDiff) < HVAC_RATE) ? desiredTemp.get() : roomTemp + HVAC_RATE);
                        else if (aboveDesired)
                            room.setTemperature((Math.abs(tempDiff) < HVAC_RATE) ? desiredTemp.get() : roomTemp - HVAC_RATE);

                    // Otherwise, slowly drift to outside temp
                    } else {
                        float outsideTemp = simulation.getTemperatureOutside();
                        float factor = outsideTemp > roomTemp ? 1.0f : -1.0f;
                        room.setTemperature((Math.abs(outsideTemp - roomTemp) < STOPPED_RATE) ? outsideTemp : roomTemp + factor * STOPPED_RATE);
                    }
                }
            }
        }
        SmartHomeSimulator.updateRoomTempView();
    }

    /**
     * This function starts the HVAC when called in the SHH.
     */
    public void startHVAC() {
        HVAC hvac = this;

        DelayedRunnable task = new DelayedRunnable() {

            private long delay = 1000000000;

            @Override
            public long getDelay() {
                return delay;
            }

            @Override
            public void setDelay(long delay) {
                this.delay = delay;
            }

            @Override
            public boolean isPeriodic() {
                return true;
            }

            @Override
            public long getPeriod() {
                return 1000000000;
            }

            @Override
            public void run() {
                changeTemp(shh, simulation);
            }
        };

        simulation.getDispatcher().schedule(task);
    }

    // ============================ GETTERS/SETTERS ============================


}
