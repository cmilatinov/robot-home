package com.smarthome.simulator.models;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHH;
import com.smarthome.simulator.utils.DelayedRunnable;
import com.smarthome.simulator.utils.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class HVAC {
    private Simulation simulation;
    private SHH shh;

    private final float workingRate = 0.1f;
    private final float pausedRate = 0f;
    private final float stoppedRate = 0.05f;
    private static final float MINIMUM_ALERT_TEMP = 0f;
    private static final float MAXIMUM_ALERT_TEMP = 50f;

    public HVAC(Simulation simulation, SHH shh){
        super();
        this.shh = shh;
        this.simulation = simulation;
    }

    // ============================  Other methods  ============================

    private void changeTemp(Simulation simulation){
        HouseLayout house = simulation.getHouseLayout();
        if(house != null) {
            List<Room> rooms = house.getRooms();

            boolean isSummer = simulation.getCurrentSeason().equals(Simulation.SUMMER);
            boolean isWinter = simulation.getCurrentSeason().equals(simulation.WINTER);

            for(Room room: rooms) {

                float roomTemp = room.getTemperature();
                float desiredTemp = room.getDesiredTemperature();
                float tempDiff = desiredTemp - roomTemp;

                boolean belowDesired = roomTemp < desiredTemp;
                boolean aboveDesired = roomTemp > desiredTemp;
                boolean outsideCoolerThanInside = simulation.getTemperatureOutside() < roomTemp;
                boolean outsideHotterThanInside = simulation.getTemperatureOutside() > roomTemp;
                boolean paused = true;

                //Check critical temp
                if(roomTemp <= MINIMUM_ALERT_TEMP || roomTemp >= MAXIMUM_ALERT_TEMP && !room.isCriticalTempLogged()) {
                    room.setCriticalTempLogged(true);
                    SmartHomeSimulator.LOGGER.log(Logger.WARN, "SHH", "Critical temperature: " + roomTemp + " in room " + room.getName());
                }
                else if (roomTemp > MINIMUM_ALERT_TEMP && roomTemp < MAXIMUM_ALERT_TEMP && room.isCriticalTempLogged()) {
                    room.setCriticalTempLogged(false);
                }

                if (simulation.isAway()) {
                    float winterTemp = shh.getWinterTemperature();
                    float summerTemp = shh.getSummerTemperature();
                    if (isWinter && winterTemp != room.getTemperature()) {
                        if (winterTemp > roomTemp) {
                            room.setTemperature((winterTemp-roomTemp < 0.1f)?winterTemp:roomTemp+0.1f);
                        }
                        else if (winterTemp < roomTemp) {
                            room.setTemperature((winterTemp-roomTemp < 0.1f)?winterTemp:roomTemp-0.1f);
                        }
                    }
                    else if (isSummer && summerTemp != room.getTemperature()) {
                        if (summerTemp > roomTemp) {
                            room.setTemperature((summerTemp-roomTemp < 0.1f)?summerTemp:roomTemp+0.1f);
                        }
                        else if (summerTemp < roomTemp) {
                            room.setTemperature((summerTemp-roomTemp < 0.1f)?summerTemp:roomTemp-0.1f);
                        }
                    }
                }

                else {
                    //Open windows in summer
                    if (isSummer && outsideCoolerThanInside && aboveDesired) {
                        AtomicBoolean windowOpen = new AtomicBoolean(false);
                        room.getWindows().forEach(window -> {
                            Map<String, Object> payload = new HashMap<String, Object>();
                            payload.put("id", window.getId());
                            payload.put("blocked", window.isBlocked());
                            payload.put("open", true);
                            simulation.executeCommand(SHC.CONTROL_WINDOW, payload, false);
                            if (window.isOpen())
                                windowOpen.set(true);
                        });
                        if (windowOpen.get()) {
                            room.setTemperature(roomTemp - 0.05f);
                            //SmartHomeSimulator.LOGGER.log(Logger.INFO, "SHH", "Shutting down AC in room: " + room.getName());
                        }
                    }
                    if (Math.abs(tempDiff) > 0.25f) {
                        if (paused) {
                            paused = false;
                            //SmartHomeSimulator.LOGGER.log(Logger.INFO, "SHH", "HVAC Unpausing in room: " + room.getName());
                        }
                        if (belowDesired)
                            room.setTemperature(roomTemp + 0.1f);
                        else
                            room.setTemperature(roomTemp - 0.1f);
                    }
                    else {
                        paused = true;
                        //SmartHomeSimulator.LOGGER.log(Logger.INFO, "SHH", "HVAC Pausing in room: " + room.getName());
                    }
                }
            }
        }
    }

    public void startHVAC () {
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
                changeTemp(simulation);
            }
        };

        simulation.getDispatcher().schedule(task);
    }

    // ============================ GETTERS/SETTERS ============================


}
