package com.smarthome.simulator.modules;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Room;
import com.smarthome.simulator.models.Simulation;
import com.smarthome.simulator.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The SHC class represents the Smart Home Core Functionality module.
 */
public class SHC extends Module {

    /**
     * Indicates whether the AUTO mode is turned on for lights.
     */
    private boolean autoMode = false;

    /**
     * Represents the permission to control doors.
     */
    public static final String CONTROL_DOOR = "ControlDoor";

    /**
     * Represents the permission to control windows.
     */
    public static final String CONTROL_WINDOW = "ControlWindow";

    /**
     * Represents the permission to control lights.
     */
    public static final String CONTROL_LIGHT = "ControlLight";

    /**
     * Represents the permission to control auto mode.
     */
    public static final String SET_AUTO_MODE = "ControlAutoMode";

    /**
     * Represents the permission to control all window closing.
     */
    public static final String CLOSE_ALL_WINDOWS = "CloseAllWindows";

    /**
     * Represents the permission to control all doors locking.
     */
    public static final String LOCK_ALL_DOORS = "LockAllDoors";

    /**
     * Represents the permission to turn off all lights.
     */
    public static final String CLOSE_ALL_LIGHTS = "CloseAllLights";

    /**
     * Represents the permission to control room updates.
     */
    public static final String UPDATE_ROOM_LIGHTS = "UpdateRoomLights";

    /**
     * Represents the permission to control door remotely.
     */
    public static final String REMOTE_CONTROL_DOOR = "RemoteControlDoor";

    /**
     * Represents the permission to control windows remotely.
     */
    public static final String REMOTE_CONTROL_WINDOW = "RemoteControlWindow";

    /**
     * Represents the permission to control light remotely.
     */
    public static final String REMOTE_CONTROL_LIGHT = "RemoteControlLight";

    /**
     * Constructor creates a new SHC with reference to the current simulation.
     *
     * @param simulation Represents the running {@link Simulation}.
     */
    public SHC(Simulation simulation) {
        super("SHC", simulation);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getCommandList() {
        return new ArrayList<String>() {{
            add(CONTROL_DOOR);
            add(CONTROL_WINDOW);
            add(CONTROL_LIGHT);
            add(SET_AUTO_MODE);
            add(CLOSE_ALL_WINDOWS);
            add(LOCK_ALL_DOORS);
            add(CLOSE_ALL_LIGHTS);
            add(UPDATE_ROOM_LIGHTS);

            add(REMOTE_CONTROL_DOOR);
            add(REMOTE_CONTROL_WINDOW);
            add(REMOTE_CONTROL_LIGHT);
        }};
    }

    /**
     * {@inheritDoc}
     */
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {
        // Switch state for all the possible commands
        switch (command) {
            case CONTROL_DOOR:
            case REMOTE_CONTROL_DOOR:
                controlDoor(payload);
                break;
            case CONTROL_WINDOW:
            case REMOTE_CONTROL_WINDOW:
                controlWindow(payload);
                break;
            case CONTROL_LIGHT:
            case REMOTE_CONTROL_LIGHT:
                controlLight(payload);
                break;
            case SET_AUTO_MODE:
                setAutoMode(payload);
                break;
            case CLOSE_ALL_WINDOWS:
                closeAllWindows();
                break;
            case LOCK_ALL_DOORS:
                lockAllDoors();
                break;
            case CLOSE_ALL_LIGHTS:
                closeAllLights();
                break;
            case UPDATE_ROOM_LIGHTS:
                updateRoomLights(payload);
                break;
        }
    }

    /**
     * Function locks all doors in the {@link com.smarthome.simulator.models.HouseLayout}
     */
    private void lockAllDoors() {
        simulation.getAllDoors()
                .forEach(door -> {
                    door.setOpen(false);
                    door.setLocked(true);
                });
    }

    /**
     * Function closes all windows in the {@link com.smarthome.simulator.models.HouseLayout}
     */
    private void closeAllWindows() {
        simulation.getAllWindows()
                .forEach(window -> {
                    window.setBlocked(false);
                    window.setOpen(false);
                });
    }

    /**
     * Function closes all lights in the {@link com.smarthome.simulator.models.HouseLayout}
     */
    private void closeAllLights() {
        simulation.getAllLights()
                .forEach(light -> light.setOn(false));
    }

    /**
     * Function to turn on and off the lights of {@link com.smarthome.simulator.models.HouseLayout}
     *
     * @param payload The arguments for the command.
     */
    private void controlLight(Map<String, Object> payload) {

        // Target state and ID
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
    private void controlWindow(Map<String, Object> payload) {

        // Target state and ID
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
                        SmartHomeSimulator.LOGGER.log(Logger.ERROR, getName(), "COMMAND CANNOT BE EXECUTED!" +
                                " Window cannot be opened/closed because its path is blocked");
                });
    }

    /**
     * Function to open, close, lock and unlock the doors of {@link com.smarthome.simulator.models.HouseLayout}
     *
     * @param payload The arguments for the command.
     */
    private void controlDoor(Map<String, Object> payload) {

        // Target state and ID
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
                    } else if (!d.isOpen() && d.isLocked() && !locked && d.isHouseEntrance()) {
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
    private void setAutoMode(Map<String, Object> payload) {
        autoMode = (Boolean) payload.get("value");
    }

    /**
     * Function to update the room of a {@link com.smarthome.simulator.models.HouseLayout}
     *
     * @param payload The arguments for the command.
     */
    public void updateRoomLights(Map<String, Object> payload) {

        // AUTO mode is off, or we are in away mode
        if (!autoMode || simulation.isAway())
            return;

        // Find the right room
        Room room = (Room) payload.get("room");

        // Check if there are people in the room
        Optional<Person> person = simulation.getPeople()
                .stream()
                .filter(p -> p.getRoomId() != null && p.getRoomId().equals(room.getId()))
                .findFirst();

        // Set all the lights to on if there are people in the room, otherwise, turn them all off
        room.getLights().forEach(light -> light.setOn(person.isPresent() || room.getId().equals(simulation.getUserLocation())));

    }

    /**
     * Returns whether or not the SHC module is in AUTO mode or not.
     *
     * @return True if AUTO mode is turned on, false otherwise.
     */
    public boolean isAutoMode() {
        return autoMode;
    }

}
