package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Room;
import com.smarthome.simulator.models.Simulation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The SHC class represents the Smart Home Core Functionality module.
 */
public class SHC extends Module{

    /**
     * This boolean is set to false when the auto mode is off and to true when it is on.
     */
    private boolean autoMode = false;

    /**
     * Represents the permission to control doors.
     */
    public static final String P_CONTROL_DOORS = "ControlDoors";

    /**
     * Represents the permission to control windows.
     */
    public static final String P_CONTROL_WINDOWS = "ControlWindows";

    /**
     * Represents the permission to control lights.
     */
    public static final String P_CONTROL_LIGHTS = "ControlLights";

    /**
     * Represents the permission to control auto mode.
     */
    public static final String P_CONTROL_AUTO_MODE = "ControlAutoMode";

    /**
     * Represents the permission to control all window closing.
     */
    public static final String P_CLOSE_ALL_WINDOWS = "CloseAllWindows";

    /**
     * Represents the permission to control all doors locking.
     */
    public static final String P_LOCK_ALL_DOORS = "LockAllDoors";

    /**
     * Represents the permission to control room updates.
     */
    public static final String P_UPDATE_ROOM = "UpdateRoom";

    /**
     * Represents the permission to control door remotely.
     */
    public static final String P_REMOTE_CONTROL_DOORS = "RemoteControlDoors";

    /**
     * Represents the permission to control windows remotely.
     */
    public static final String P_REMOTE_CONTROL_WINDOWS = "RemoteControlWindows";

    /**
     * Represents the permission to control light remotely.
     */
    public static final String P_REMOTE_CONTROL_LIGHTS = "RemoteControlLights";

    /**
     * Constructor creates a new SHC with reference to the current simulation.
     *
     * @param simulation represents the running {@link Simulation}
     */
    public SHC(Simulation simulation) {
        super("SHC", simulation);
    }

    /**
     * Gets the list of permissions/commands that the Module is responsible for/can execute.
     *
     * @return List of Strings representing the permissions/commands pertaining to the module
     */
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

    /**
     * Executes a command given by a user or other system module.
     *
     * @param command The name of the command to be executed.
     * @param payload The arguments for the command.
     * @param sentByUser Whether the command was called by a user or not. False if called by other system modules.
     */
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

    /**
     * Function locks all doors in the {@link com.smarthome.simulator.models.HouseLayout}
     */
    private void lockAllDoors() {
        simulation.getAllDoors()
                .stream()
                .forEach(door -> {
                    door.setOpen(false);
                    door.setLocked(true);
                });
    }

    /**
     * Function cloeses all windows in the {@link com.smarthome.simulator.models.HouseLayout}
     */
    private void closeAllWindows() {
        simulation.getAllWindows()
                .stream()
                .forEach(window -> {
                    window.setBlocked(false);
                    window.setOpen(false);
                });
    }

    /**
     * Function to turn on and off the lights of {@link com.smarthome.simulator.models.HouseLayout}
     *
     * @param payload The arguments for the command.
     */
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

    /**
     * Function to open, close, block and unblock the windows of {@link com.smarthome.simulator.models.HouseLayout}
     *
     * @param payload The arguments for the command.
     */
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

    /**
     * Function to open, close, lock and unlock the doors of {@link com.smarthome.simulator.models.HouseLayout}
     *
     * @param payload The arguments for the command.
     */
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
                    } else if (!locked && d.isHouseEntrance()) {
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

    /**
     * Function to turn on and off auto mode.
     *
     * @param payload The arguments for the command.
     */
    private void manageAutoMode(Map<String, Object> payload) {
        autoMode = (Boolean) payload.get("on");
    }

    /**
     * Function to update the room of a {@link com.smarthome.simulator.models.HouseLayout}
     *
     * @param payload The arguments for the command.
     */
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
