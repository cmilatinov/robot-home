package com.smarthome.simulator.modules;

import com.smarthome.simulator.*;
import com.smarthome.simulator.models.*;
import com.smarthome.simulator.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.event.PaintEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SHH extends Module{

    private final List<Zone> zones = new ArrayList<>();

    public static final String SET_DEFAULT_ZONE = "setDefaultZone";
    public static final String SET_ROOM_TEMPERATURE = "setRoomTemperature";
    public static final String EDIT_ZONE = "editZone";
    public static final String ADD_ZONE = "addZone";
    public static final String REMOVE_ZONE = "removeZone";
    public static final String SET_ROOM_OVERRIDE = "setRoomOverride";

    /**
     * Creates a module with a given name and simulation reference.
     *
     * @param simulation The simulation instance to bind to the module.
     */
    public SHH(Simulation simulation) {
        super("SHH", simulation);
    }

    @Override
    public List<String> getCommandList() {
        return new ArrayList<String>() {{
            add(SET_DEFAULT_ZONE);
            add(SET_ROOM_TEMPERATURE);
            add(EDIT_ZONE);
            add(ADD_ZONE);
            add(REMOVE_ZONE);
            add(SET_ROOM_OVERRIDE);
        }};
    }

    @Override
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {

        // If the command was sent by the user, check if the active user profile has the needed permission.
        if (sentByUser && !checkPermission(command))
            return;

        // Log command
        SmartHomeSimulator.LOGGER.log(Logger.INFO, getName(), "Executing command '" + command + "'");

        switch (command){
            case SET_DEFAULT_ZONE:
                setDefaultZone();
                break;
            case SET_ROOM_TEMPERATURE:
                setRoomTemperature(payload);
                break;
            case EDIT_ZONE:
                editZone(payload);
                break;
            case ADD_ZONE:
                addZone(payload);
                break;
            case REMOVE_ZONE:
                removeZone(payload);
                break;
            case SET_ROOM_OVERRIDE:
                setRoomOverride(payload);
                break;
        }
    }

    private void setRoomOverride(Map<String, Object> payload) {
        String roomId = (String) payload.get("id");
        Boolean overrideBool = Boolean.parseBoolean(payload.get("value").toString());
        simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .ifPresent(room -> room.setTemperatureOverridden(overrideBool));
    }

    private void removeZone(Map<String, Object> payload) {
        String zoneId = (String) payload.get("id");

        Optional<Zone> defaultZone = zones.stream()
                .filter(zone -> zone.isDefault())
                .findFirst();

        zones.stream()
                .filter(zone -> zone.getId().equals(zoneId))
                .findFirst()
                .ifPresent(zone -> {
                    if (defaultZone.isPresent()) {
                        defaultZone.get().getRooms().addAll(zone.getRooms());
                        zones.remove(zone);
                    }
                });
    }

    private void addZone(Map<String, Object> payload) {
        String zoneName = (String) payload.get("name");
        List<Room> rooms = (List<Room>) ((JSONArray) payload.get("room_ids"))
                .stream()
                .map(p -> p.toString())
                .reduce(new ArrayList<Room>(), (listObj, roomId) -> {
                    List<Room> list = (List<Room>) listObj;
                    simulation.getHouseLayout().getRooms()
                            .stream()
                            .filter(room -> room.getId().equals(roomId))
                            .findFirst()
                            .ifPresent(list::add);
                    return list;
                });
        zones.forEach(z -> z.getRooms().removeIf(r -> rooms.contains(r)));
        List<Period> periods = (List<Period>) ((JSONArray) payload.get("periods"))
                .stream()
                .map(obj -> {
                    JSONObject json = (JSONObject) obj;
                    return new Period(
                            Integer.parseInt(json.get("startTime").toString()),
                            Integer.parseInt(json.get("endTime").toString()),
                            Float.parseFloat(json.get("desiredTemperature").toString()));
                });

        Zone zone = new Zone(zoneName, false);
        zone.getRooms().addAll(rooms);
        zone.getPeriods().addAll(periods);
        zones.add(zone);

    }

    private void setDefaultZone() {
        zones.clear();
        Zone defaultZone = new Zone("default", true);
        defaultZone.getPeriods().add(new Period(0, 1440, simulation.getTemperatureOutside()));
        defaultZone.getRooms().addAll(simulation.getHouseLayout().getRooms());
        zones.add(defaultZone);
    }

    private void setRoomTemperature(Map<String, Object> payload) {
        String room_id = (String) payload.get("id");
        Float temperature = Float.parseFloat(payload.get("temperature").toString());

        this.simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> room.getId().equals(room_id))
                .findFirst()
                .ifPresent(room -> {
                    room.setDesiredTemperature(temperature);
                    room.setTemperatureOverridden(true);
                });
    }

    private void editZone(Map<String, Object> payload) {
        String zoneName = (String) payload.get("name");
        String zoneId = (String) payload.get("zone_id") ;
        List<Period> periods = (List<Period>) ((JSONArray) payload.get("periods"))
                .stream()
                .map(obj -> {
                    JSONObject json = (JSONObject) obj;
                    return new Period(
                            Integer.parseInt(json.get("startTime").toString()),
                            Integer.parseInt(json.get("endTime").toString()),
                            Float.parseFloat(json.get("desiredTemperature").toString()));
                });

        zones.stream()
            .filter(z -> z.getId().equals(zoneId))
            .findFirst()
            .ifPresent(z -> {
                z.getPeriods().clear();
                z.setName(zoneName);
                z.getPeriods().addAll(periods);
            });
    }

    public List<Zone> getZones() {
        return zones;
    }
}
