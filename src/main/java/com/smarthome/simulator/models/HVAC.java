package com.smarthome.simulator.models;


import com.smarthome.simulator.utils.DelayedRunnable;

public class HVAC {
    private Simulation simulation;
    enum State {
        WORKING,
        PAUSED,
        STOPPED
    }
    private State state;
    private float workingRate = 0.1f;
    private float pausedRate = 0f;
    private float stoppedRate = 0.05f;

    public HVAC(){
        super();
        this.state = State.STOPPED;
    }
    // ============================  Other methods  ============================

    private void changeTemp(){



        if(this.state == State.WORKING){
            DelayedRunnable task = new DelayedRunnable() {


                @Override
                public long getDelay() {
                    return 0;
                }

                @Override
                public void setDelay(long delay) {

                }

                @Override
                public boolean isPeriodic() {
                    return false;
                }

                @Override
                public long getPeriod() {
                    return 0;
                }

                @Override
                public void run() {

                }
            }
        }else if(this.state == State.PAUSED){

        }else if(this.state == State.STOPPED){

        }
    }

    // ============================ GETTERS/SETTERS ============================


}
