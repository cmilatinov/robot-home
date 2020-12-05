package com.smarthome.simulator.modules;


import com.smarthome.simulator.models.Period;
import com.smarthome.simulator.models.Room;
import com.smarthome.simulator.models.Simulation;
import com.smarthome.simulator.models.Zone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SHH extends Module {


    private final List<Zone> zones = new ArrayList<>();
    public static final String SET_DEFAULT_ZONE = "setDefaultZone";
    public static final String SET_ROOM_TEMPERATURE = "setRoomTemperature";
    public static final String EDIT_ZONE = "editZone";
    public static final String ADD_ZONE = "addZone";
    public static final String REMOVE_ZONE = "removeZone";
    public static final String SET_ROOM_OVERRIDE = "setRoomOverride";
    public static final String SET_SEASON_TEMP = "setSeasonTemp";
    public static final String SET_WINTER_RANGE = "setWinterRange";
    public static final String SET_AWAY_MODE_TEMP = "setAwayModeTemp";

    /**
     * Creates a module with a given name and simulation reference.
     *
     * @param simulation The simulation instance to bind to the module.
     */
    public SHH(Simulation simulation) {
        super("SHH", simulation);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getCommandList() {
        return new ArrayList<String>() {{
            add(SET_DEFAULT_ZONE);
            add(SET_ROOM_TEMPERATURE);
            add(SET_WINTER_RANGE);
            add(EDIT_ZONE);
            add(ADD_ZONE);
            add(REMOVE_ZONE);
            add(SET_ROOM_OVERRIDE);
            add(SET_SEASON_TEMP);
            add(SET_AWAY_MODE_TEMP);
        }};
    }

    /**
     * {@inheritDoc}
     */
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {
        // Switch state for all the possible commands
        switch (command) {
            case SET_DEFAULT_ZONE:
                setDefaultZone();
                break;
            case SET_ROOM_TEMPERATURE:
                setRoomTemperature(payload);
                break;
            case SET_WINTER_RANGE:
                setWinterRange(payload);
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
            case SET_SEASON_TEMP:
                setSeasonTemp(payload);
                break;
            case SET_AWAY_MODE_TEMP:
                setAwayModeTemp();
                break;
        }
    }

    private void setAwayModeTemp() {
        // waiting for the change rate temperature to be figured out
        // will change the actual temperature to the desired temperature according to the season
        // In Simulator class, winter: winterTemperature, summer: summerTemperature
    }

    private void setSeasonTemp(Map<String, Object> payload){
        // desired temperature of winter and summer time in away mode
        float winterTemp = Float.parseFloat(payload.get("winter").toString());
        float summerTemp = Float.parseFloat(payload.get("summer").toString());

        // set the temperature to the simulation
        simulation.setWinterTemperature(winterTemp);
        simulation.setSummerTemperature(summerTemp);

    }

    private void setWinterRange(Map<String, Object> payload){
        // receives the month in integer format
        int startMonth = Integer.parseInt(payload.get("start").toString());
        int endMonth = Integer.parseInt(payload.get("end").toString());

        //set the winter range
        this.simulation.setStartWinterMonth(startMonth);
        this.simulation.setEndWinterMonth(endMonth);
    }



    private void setRoomOverride(Map<String, Object> payload) {
        String roomId = (String) payload.get("id");
        boolean overrideBool = (boolean) payload.get("value");
        simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .ifPresent(room -> room.setOverrideZoneTemp(overrideBool));
    }

    private void removeZone(Map<String, Object> payload) {
        String zoneId = (String) payload.get("id");

        Optional<Zone> defaultZone = zones.stream()
                .filter(Zone::isDefault)
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

    @SuppressWarnings({"unchecked", "Duplicates"})
    private void addZone(Map<String, Object> payload) {
        String zoneName = (String) payload.get("name");
        List<Room> rooms = (List<Room>) ((JSONArray) payload.get("room_ids"))
                .stream()
                .map(Object::toString)
                .reduce(new ArrayList<Room>(), (listObj, roomId) -> {
                    List<Room> list = (List<Room>) listObj;
                    simulation.getHouseLayout().getRooms()
                            .stream()
                            .filter(room -> room.getId().equals(roomId))
                            .findFirst()
                            .ifPresent(list::add);
                    return list;
                });
        zones.forEach(z -> z.getRooms().removeIf(rooms::contains));
        List<Period> periods = (List<Period>) ((JSONArray) payload.get("periods"))
                .stream()
                .map(Object::toString)
                .reduce(new ArrayList<Period>(), (listObj, periodPayloadObj) -> {
                    List<Period> list = (List<Period>) listObj;
                    JSONObject json = (JSONObject) periodPayloadObj;
                    Period period = new Period(
                            json.get("startTime").toString(),
                            json.get("endTime").toString(),
                            Float.parseFloat(json.get("desiredTemperature").toString()));
                    list.add(period);
                    return list;
                });

        Zone zone = new Zone(zoneName, false);
        zone.getRooms().addAll(rooms);
        zone.getPeriods().addAll(periods);
        zones.add(zone);

    }

    private void setDefaultZone() {
        zones.clear();
        Zone defaultZone = new Zone("default", true);
        defaultZone.getPeriods().add(new Period("00:00", "23:59", simulation.getTemperatureOutside()));
        defaultZone.getRooms().addAll(simulation.getHouseLayout().getRooms());
        zones.add(defaultZone);
    }

    private void setRoomTemperature(Map<String, Object> payload) {
        String room_id = (String) payload.get("id");
        float temperature = Float.parseFloat(payload.get("temperature").toString());

        this.simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> room.getId().equals(room_id))
                .findFirst()
                .ifPresent(room -> {
                    room.setDesiredTemperature(temperature);
                    room.setOverrideZoneTemp(true);
                });
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    private void editZone(Map<String, Object> payload) {
        String zoneName = (String) payload.get("name");
        String zoneId = (String) payload.get("zone_id");
        List<Period> periods = (List<Period>) ((JSONArray) payload.get("periods"))
                .stream()
                .map(Object::toString)
                .reduce(new ArrayList<Period>(), (listObj, periodPayloadObj) -> {
                    List<Period> list = (List<Period>) listObj;
                    JSONObject json = (JSONObject) periodPayloadObj;
                    Period period = new Period(
                            json.get("startTime").toString(),
                            json.get("endTime").toString(),
                            Float.parseFloat(json.get("desiredTemperature").toString()));
                    list.add(period);
                    return list;
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
