package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Door;
import com.smarthome.simulator.models.Light;
import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SHP extends Module{

    private boolean away;
    private float alertDelay;
    private SHC shc;
    private HashMap<String, Boolean> awayLights;

    public static final String P_SET_AWAY_LIGHTS = "SetAwayLights";
    public static final String P_SET_AWAY_MODE = "SetAwayMode";

    public SHP (boolean _away, float _alertDelay, Simulation _simulation) {
        super("SHP", _simulation);
        this.away = _away;
        this.alertDelay = _alertDelay;
        awayLights = new HashMap<String, Boolean>();

    }
    public SHP (Simulation _simulation) {
        super("SHP", _simulation);
        this.away = false;
        this.alertDelay = 0;
        awayLights = new HashMap<String, Boolean>();
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
        if (_away && isHouseEmpty()) {
            this.away = _away;
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

    private void setAwayLights(ArrayList<Light> lights) {
        System.out.println("setting away lights");
        awayLights = new HashMap<String, Boolean>();
        for(int i = 0; i < lights.size(); i++) {
            Light l = lights.get(i);
            awayLights.put(l.getId(), l.isOn());
        }
    }

    private void ExecuteAwayLights() {
        Module shc = simulation.getModule("ControlLights");

        if (!awayLights.isEmpty()) {
            awayLights.forEach((k, v) -> {
                HashMap lightPayLoad = new HashMap<String, Object>() {{
                    put("id", k);
                    put("on", v);
                }};
                System.out.println(lightPayLoad);
                shc.executeCommand("RemoteControlLights", lightPayLoad, false);
            });
        }
    }

    private void ExecuteAwayMode(Map<String, Object> payload) { 
        setAway((boolean) payload.get("value"));

        System.out.println("away: " + away);

        if (!away) {return;}

        Module shc = simulation.getModule("RemoteControlDoors");

        /*ArrayList<Door> doors = simulation.getAllDoors();
        for (int i=0; i < doors.size(); i++) {
            Door door = doors.get(i);
            HashMap doorPayLoad = new HashMap<String, Object>() {{
                put ("id", door.getId());
                put ("open", false);
                put ("locked", true);
            }};
            System.out.println(doorPayLoad);
            shc.executeCommand("RemoteControlDoors", doorPayLoad, false);
        }*/

        shc.executeCommand("LockAllDoors", null, false);
        shc.executeCommand("CloseAllWindows", null, false);

        //TODO check if within allotted time to set away lights on or off
        if (true)
            ExecuteAwayLights();

    }


    @Override
    public List<String> getPermissions() {
        return new ArrayList<String> () {{
            add(P_SET_AWAY_MODE);
            add(P_SET_AWAY_LIGHTS);
        }};
    }

    @Override
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {
        if (!checkPermission(command, sentByUser)) {
            return;
        }
        switch (command) {
            case "SetAwayLights":
                setAwayLights(simulation.getAllLights());
                break;

            case "SetAwayMode":
                ExecuteAwayMode(payload);
                break;
        }
    }
}
