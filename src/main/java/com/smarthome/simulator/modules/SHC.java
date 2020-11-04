package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Room;
import com.smarthome.simulator.models.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SHC extends Module{

    private boolean autoMode = false;

    public static final String P_CONTROL_DOORS = "ControlDoors";
    public static final String P_CONTROL_WINDOWS = "ControlWindows";
    public static final String P_CONTROL_LIGHTS = "ControlLights";
    public static final String P_CONTROL_AUTO_MODE = "ControlAutoMode";

    public static final String P_REMOTE_CONTROL_DOORS = "RemoteControlDoors";
    public static final String P_REMOTE_CONTROL_WINDOWS = "RemoteControlWindows";
    public static final String P_REMOTE_CONTROL_LIGHTS = "RemoteControlLights";


    public SHC(Simulation simulation) {
        super("SHC", simulation);
    }

    @Override
    public List<String> getPermissions() {
        return new ArrayList<String>() {{
            add(P_CONTROL_DOORS);
            add(P_CONTROL_WINDOWS);
            add(P_CONTROL_LIGHTS);
            add(P_CONTROL_AUTO_MODE);
            add(P_REMOTE_CONTROL_DOORS);
            add(P_REMOTE_CONTROL_WINDOWS);
            add(P_REMOTE_CONTROL_LIGHTS);
        }};
    }


    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<String>() {{
            add("exampleCommand");
        }};
    }

    // SHC shc = new SHC(simulation)
    // shc.executeCommand("RemoteLightActivation", which light? on or off? , true)
    // shc.executeCommand("RemoteLightActivation", new Hashmap() , true)


    @Override
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {

        if (!checkPermission(command, sentByUser)) {
            return;
        }

        switch (command) {
            case P_CONTROL_DOORS:
            case P_REMOTE_CONTROL_DOORS:
                manageDoorCommand(payload);
                break;
            case P_CONTROL_WINDOWS:
            case P_REMOTE_CONTROL_WINDOWS:
                manageWindowCommand(payload);
                break;
            case P_CONTROL_LIGHTS:
            case P_REMOTE_CONTROL_LIGHTS:
                manageLightCommand(payload);
                break;
            case P_CONTROL_AUTO_MODE:
                manageAutoMode(payload);
                break;

        }

    }

    private void manageLightCommand(Map<String, Object> payload) {
        String id = (String) payload.get("id");
        boolean on = (Boolean) payload.get("on");

        // Change light state if exists
        simulation.getAllLights()
                .stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .ifPresent(l -> l.setOn(on));
    }

    private void manageWindowCommand(Map<String, Object> payload) {
        String id = (String) payload.get("id");
        boolean blocked = (Boolean) payload.get("blocked");
        boolean open = (Boolean) payload.get("open");

        // Change window state if exists
        simulation.getAllWindows()
                .stream()
                .filter(w -> w.getId().equals(id))
                .findFirst()
                .ifPresent(w -> {
                    w.setBlocked(blocked);
                    if (w.isOpen() != open && !blocked)
                        w.setOpen(open);
                });
    }

    private void manageDoorCommand(Map<String, Object> payload) {
        String id = (String) payload.get("id");
        boolean open = (Boolean) payload.get("open");
        boolean locked = (Boolean) payload.get("locked");

        // Change door state if exists
        simulation.getAllDoors()
                .stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .ifPresent(d -> {
                    d.setOpen(open);
                    d.setLocked(locked);
                });
    }

    private void manageAutoMode(Map<String, Object> payload) {
        autoMode = (Boolean) payload.get("on");
    }

    public void updateRoom(Room room) {
        if (!autoMode) return;
        Optional<Person> person = simulation.getPeople()
                .stream()
                .filter(p -> p.getRoomId().equals(room.getId()))
                .findFirst();

        room.getLights().stream().forEach(light -> light.setOn(person.isPresent()));
    }

}
