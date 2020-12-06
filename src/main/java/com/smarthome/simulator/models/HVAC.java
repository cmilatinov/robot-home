package com.smarthome.simulator.models;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.modules.SHH;
import com.smarthome.simulator.utils.DelayedRunnable;
import com.smarthome.simulator.utils.Logger;

import java.util.List;

public class HVAC {
    private Simulation simulation;
    private SHH shh;
    enum State {
        WORKING,
        PAUSED,
        STOPPED
    }
    private State state;
    private final float workingRate = 0.1f;
    private final float pausedRate = 0f;
    private final float stoppedRate = 0.05f;
    private static final float MINIMUM_ALERT_TEMP = -20f;
    private static final float MAXIMUM_ALERT_TEMP = 50f;

    public HVAC(Simulation simulation, SHH shh){
        super();
        this.shh = shh;
        this.simulation = simulation;
        this.state = State.STOPPED;
    }

    // ============================  Other methods  ============================

    private void changeTemp(HVAC hvac, Simulation simulation){
        HouseLayout house = simulation.getHouseLayout();
        if(house != null) {
            List<Room> rooms = house.getRooms();
            for(Room room: rooms) {
                float roomTemp = room.getTemperature();
                if(roomTemp <= MINIMUM_ALERT_TEMP || roomTemp>=MAXIMUM_ALERT_TEMP) {
                    SmartHomeSimulator.LOGGER.log(Logger.WARN, "SHH", "Critical temperature: " + roomTemp);
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
                changeTemp(hvac, simulation);
            }
        };

        simulation.getDispatcher().schedule(task);
    }

    // ============================ GETTERS/SETTERS ============================


}
