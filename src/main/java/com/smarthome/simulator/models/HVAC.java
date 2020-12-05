package com.smarthome.simulator.models;

import com.smarthome.simulator.modules.SHH;
import com.smarthome.simulator.utils.DelayedRunnable;

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

    public HVAC(Simulation simulation, SHH shh){
        super();
        this.shh = shh;
        this.simulation = simulation;
        this.state = State.STOPPED;
    }

    // ============================  Other methods  ============================

    private void changeTemp(HVAC hvac, Simulation simulation){
        if(hvac.state == State.WORKING) {

            //increase temp by working amount
        } else if(this.state == State.PAUSED) {
            //check temp
        }else if(this.state == State.STOPPED) {
            //something else
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
