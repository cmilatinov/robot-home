package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Light;
import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SHP extends Module{

    private boolean away;
    private float alertDelay;
    private SHC shc;

    public SHP (boolean _away, float _alertDelay, SHC _shc, Simulation _simulation) {
        super("SHP", _simulation);
        this.away = _away;
        this.alertDelay = _alertDelay;
        this.shc = _shc;
    }
    public SHP (SHC _shc, Simulation _simulation) {
        super("SHP", _simulation);
        this.away = false;
        this.alertDelay = 0;
        this.shc = _shc;
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

    private void toggleAwayLight(String lightId) {
        Light light;
        simulation.getAllLights()
                .stream()
                .filter(l -> l.getId().equals(lightId))
                .findFirst()
                .ifPresent(l -> l.setAwayLight(!l.isAwayLight()));
    }

    @Override
    public List<String> getPermissions() {
        return new ArrayList<String> () {{
            add("ControlDoors");
            add("ControlLights");
            add("RemoteControlDoors");
            add("RemoteControlLights");
        }};
    }

    @Override
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {
        if (!checkPermission(command, sentByUser)) {
            return;
        }
        switch (command) {
            case "setAwayLights":
                System.out.println("setting away lights");
        }
    }
}
