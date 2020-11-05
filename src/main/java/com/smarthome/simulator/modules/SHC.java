package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Room;
import com.smarthome.simulator.models.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SHC extends Module{

    private boolean autoMode = false;

    public static final String P_CONTROL_DOORS = "ControlDoors";
    public static final String P_CONTROL_WINDOWS = "ControlWindows";
    public static final String P_CONTROL_LIGHTS = "ControlLights";
    public static final String P_CONTROL_AUTO_MODE = "ControlAutoMode";
    public static final String P_CLOSE_ALL_WINDOWS = "CloseAllWindows";
    public static final String P_LOCK_ALL_DOORS = "LockAllDoors";
    public static final String P_UPDATE_ROOM = "UpdateRoom";

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
            add(P_CLOSE_ALL_WINDOWS);
            add(P_LOCK_ALL_DOORS);
            add(P_UPDATE_ROOM);

            add(P_REMOTE_CONTROL_DOORS);
            add(P_REMOTE_CONTROL_WINDOWS);
            add(P_REMOTE_CONTROL_LIGHTS);
        }};
    }

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
            case P_CLOSE_ALL_WINDOWS:
                closeAllWindows();
                break;
            case P_LOCK_ALL_DOORS:
                lockAllDoors();
                break;
            case P_UPDATE_ROOM:
                updateRoom(payload);
                break;

        }

    }

    private void lockAllDoors() {
        simulation.getAllDoors()
                .stream()
                .forEach(door -> {
                    door.setOpen(false);
                    door.setLocked(true);
                });
    }

    private void closeAllWindows() {
        simulation.getAllWindows()
                .stream()
                .forEach(window -> {
                    window.setBlocked(false);
                    window.setOpen(false);
                });
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
                    else if (w.isOpen() != open && blocked)
                        System.out.println("COMMAND CANNOT BE EXECUTED!" +
                                " Window cannot be opened/closed because its path is blocked");
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
                    if (!locked && open && !d.isHouseEntrance()) {
                        d.setLocked(false);
                        d.setOpen(true);
                    } else if (open && d.isHouseEntrance()) {
                        d.setLocked(false);
                        d.setOpen(true);
                    } else if (!open) {
                        d.setOpen(false);
                        d.setLocked(locked);

                        // if it's a door of type houseEntrance, then lock automatically once closed.
                        if (d.isHouseEntrance()) {
                            d.setLocked(true);
                        }
                    }
                });
    }

    private void manageAutoMode(Map<String, Object> payload) {
        autoMode = (Boolean) payload.get("on");
    }

    public void updateRoom(Map<String, Object> payload) {
        if (!autoMode) return;
        Room room = (Room) payload.get("room");
        Optional<Person> person = simulation.getPeople()
                .stream()
                .filter(p -> p.getRoomId().equals(room.getId()))
                .findFirst();

        room.getLights().stream().forEach(light -> light.setOn(person.isPresent()));
    }

}
