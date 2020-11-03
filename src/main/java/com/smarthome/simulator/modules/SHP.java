package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Light;
import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Simulation;

import java.util.ArrayList;

public class SHP {

    private boolean away;
    private float alertDelay;
    private ArrayList<Light> awayLights;
    private Simulation simulation;
    //private SHC shc;

    public SHP (boolean _away, float _alertDelay, Simulation _simulation /*, SHC _shc*/) {
        this.away = _away;
        this.alertDelay = _alertDelay;
        this.awayLights = new ArrayList<Light>();
        this.simulation = _simulation;
        //this.shc = _shc
    }
    public SHP (Simulation _simulation/* SHC _shc*/) {
        this.away = false;
        this.alertDelay = 0;
        this.awayLights = new ArrayList<Light>();
        this.simulation = _simulation;
        /*this.shc = _shc;*/
    }

    /**
     * Send a notification to user and alerts authorities after set delay.
     */
    public void Alert () {
        NotifyUser();
        Thread alertThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep((long) alertDelay);
                    NotifyAuthorities();
                } catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                }
            }
        };

    }

    private void NotifyUser () {
        System.out.println("Notifying user!");
    }

    private void NotifyAuthorities () {
        System.out.println("Notifying authorities!");
    }

    private boolean isHouseEmpty () {
        ArrayList<Person> people = simulation.getPeople();

        for (int i=0; i<people.size(); i++) {
            if (people.get(i).getRoomId() == null);
            return false;
        }
        return true;
    }

    public boolean isAway() {
        return away;
    }

    public void setAway(boolean _away) {
        if (!away && away && isHouseEmpty()) {
            this.away = _away;
            /*shc.Lockdown()*/
        }
        else if (!_away)
            this.away = false;
    }

    public float getAlertDelay() {
        return alertDelay;
    }

    public void setAlertDelay(float alertDelay) {
        this.alertDelay = alertDelay;
    }

    public ArrayList<Light> getAwayLights() {
        return awayLights;
    }

    public void setAwayLights(ArrayList<Light> awayLights) {
        this.awayLights = awayLights;
    }

    public void toggleAwayLight(Light light) {
        if (!awayLights.contains(light)) {
            awayLights.add(light);
        }
        else {
            awayLights.remove(light);
        }
    }
}
