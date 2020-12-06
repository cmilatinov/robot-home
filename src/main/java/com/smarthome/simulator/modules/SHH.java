package com.smarthome.simulator.modules;


import com.smarthome.simulator.models.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The SHH class represents the Smart Home Heater module.
 */
public class SHH extends Module {

    /**
     * All zones that are active
     */
    private final List<Zone> zones = new ArrayList<>();

    /**
     * The hvac instance that will be used from here
     */
    private final HVAC hvac;

    /**
     * Permission identifier to set default zone
     */
    public static final String SET_DEFAULT_ZONE = "setDefaultZone";

    /**
     * Permission identifier to set room temperature
     */
    public static final String SET_ROOM_TEMPERATURE = "setRoomTemperature";

    /**
     * Permission identifier to edit zone
     */
    public static final String EDIT_ZONE = "editZone";

    /**
     * Permission identifier to add zone
     */
    public static final String ADD_ZONE = "addZone";

    /**
     * Permission identifier to remove zone
     */
    public static final String REMOVE_ZONE = "removeZone";

    /**
     * Permission identifier to set if room temperature has been overridden
     */
    public static final String SET_ROOM_OVERRIDE = "setRoomOverride";

    /**
     * Permission identifier to set season temperature
     */
    public static final String SET_SEASON_TEMP = "setSeasonTemp";

    /**
     * Permission identifier to set winter range
     */
    public static final String SET_WINTER_RANGE = "setWinterRange";

    /**
     * Permission identifier to set away mode temperature
     */
    public static final String SET_AWAY_MODE_TEMP = "setAwayModeTemp";

    /**
     * The default temperature for winter when the home is in away mode
     */
    private float winterTemperature;

    /**
     * The default temperature for summer when the home is in away mode
     */
    private float summerTemperature;


    /**
     * Creates a module with a given name and simulation reference.
     *
     * @param simulation The simulation instance to bind to the module.
     */
    public SHH(Simulation simulation) {
        super("SHH", simulation);
        this.winterTemperature = 22.0f;
        this.summerTemperature = 16.0f;
        hvac = new HVAC(simulation, this);
        hvac.startHVAC();
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
            case SET_SEASON_TEMP:
                setSeasonTemp(payload);
                break;
        }
    }

    /**
     * This function sets the seasons' temperatures (winter and summer)
     * @param payload The arguments needed for this command.
     */
    private void setSeasonTemp(Map<String, Object> payload){
        // desired temperature of winter and summer time in away mode
        float winterTemp = Float.parseFloat(payload.get("winter").toString());
        float summerTemp = Float.parseFloat(payload.get("summer").toString());

        // set the temperature to the simulation
        setWinterTemperature(winterTemp);
        setSummerTemperature(summerTemp);

    }

    /**
     * This function sets the winter season's range of time
     * @param payload The arguments needed for this command.
     */
    private void setWinterRange(Map<String, Object> payload){
        // receives the month in integer format
        int startMonth = Integer.parseInt(payload.get("start").toString());
        int endMonth = Integer.parseInt(payload.get("end").toString());

        //set the winter range
        this.simulation.setStartWinterMonth(startMonth);
        this.simulation.setEndWinterMonth(endMonth);
    }

    /**
     * This function sets the overridden attribute of a room to true
     * @param payload The arguments needed for this command.
     */
    private void setRoomOverride(Map<String, Object> payload) {
        String roomId = (String) payload.get("id");
        boolean overrideBool = (boolean) payload.get("value");
        simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .ifPresent(room -> room.setOverrideZoneTemp(overrideBool));
    }

    /**
     * This function removes a given zone from the list of zones and sets its rooms to the default zone
     * @param payload The arguments needed for this command.
     */
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

    /**
     * This function adds a new zone in the list of zones, sets the given list of rooms in it,
     * including the periods and the desired temperatures for them.
     * @param payload The arguments needed for this command.
     */
    @SuppressWarnings({"unchecked", "Duplicates"})
    private void addZone(Map<String, Object> payload) {
        String zoneName = (String) payload.get("name");
        List<Room> rooms = (List<Room>) ((JSONArray) payload.get("rooms"))
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

    /**
     * This function sets up the default zone where all rooms belong initially.
     */
    private void setDefaultZone() {
        zones.clear();
        Zone defaultZone = new Zone("default", true);
        defaultZone.getPeriods().add(new Period("00:00", "23:59", simulation.getTemperatureOutside()));
        defaultZone.getRooms().addAll(simulation.getHouseLayout().getRooms());
        zones.add(defaultZone);
    }

    /**
     * This function sets the temperature of a specific room when triggered by the user.
     * @param payload The arguments needed for this command.
     */
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

    /**
     * This function edits the properties a given zone.
     * @param payload The arguments needed for this command.
     */
    @SuppressWarnings({"unchecked", "Duplicates"})
    private void editZone(Map<String, Object> payload) {
        String zoneName = (String) payload.get("name");
        String zoneId = (String) payload.get("id");
        List<Period> periods = (List<Period>) ((JSONArray) payload.get("periods"))
                .stream()
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

    /**
     * This functions will return a list of all zones currently active.
     * @return List of zones.
     */
    public List<Zone> getZones() {
        return zones;
    }

    /**
     * The default winter temperature
     *
     * @return the fault winter temperature
     */
    public float getWinterTemperature() {
        return winterTemperature;
    }

    /**
     * Sets the winter temperature
     * @param winterTemperature
     * @return if the change was made or not
     */
    public boolean setWinterTemperature(float winterTemperature) {
        if(winterTemperature <= 30 && winterTemperature >= 15){
            this.winterTemperature = winterTemperature;
            return true;
        }
        return false;
    }

    /**
     * The default summer temperature
     *
     * @return the default summer temperature
     */
    public float getSummerTemperature() {
        return summerTemperature;
    }

    /**
     * Sets the default summer temperature
     * @param summerTemperature
     * @return if the change was made or not
     */
    public boolean setSummerTemperature(float summerTemperature) {
        if(summerTemperature <= 30 && summerTemperature >= 15){
            this.summerTemperature = summerTemperature;
            return true;
        }
        return false;
    }
}
