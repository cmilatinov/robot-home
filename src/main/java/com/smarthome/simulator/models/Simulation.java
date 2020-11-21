package com.smarthome.simulator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.exceptions.DateTimeFormatException;
import com.smarthome.simulator.exceptions.ModuleException;
import com.smarthome.simulator.exceptions.UserProfileException;
import com.smarthome.simulator.modules.Module;
import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHP;
import com.smarthome.simulator.utils.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Simulation {

    /**
     * The date format used to display the simulation date and time.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * The {@link LocalDateTime} of the simulation.
     */
    private LocalDateTime dateTime;

    /**
     * The state of the simulation (running or not).
     */
    private boolean running;

    /**
     * The list of available {@link UserProfile}s.
     */
    private List<UserProfile> userProfiles;

    /**
     * The active {@link UserProfile} for the simulation.
     */
    private UserProfile activeUserProfile;

    /**
     * The temperature inside of the house.
     */
    private float temperatureInside;

    /**
     * The temperature outside of the house.
     */
    private float temperatureOutside;

    /**
     * The simulation speed factor, 1 is realtime, more than 1 is faster than realtime.
     */
    private float simulationSpeed;

    /**
     * The {@link HouseLayout} of the home.
     */
    private HouseLayout houseLayout;

    /**
     * The user's location in the house.
     */
    private String userLocation;

    /**
     * The list of {@link Person} present in the house.
     */
    private List<Person> people;

    /**
     * The list of active {@link Module} in the simulation.
     */
    private final List<Module> modules;

    // ============================ CONSTRUCTORS ============================

    /**
     * Creates a simulation with default parameters.
     */
    public Simulation() {
        super();
        this.dateTime = LocalDateTime.now();
        this.running = false;

        this.userProfiles = new ArrayList<UserProfile>() {{
            try {
                add(new UserProfile("Parents"));
                add(new UserProfile("Children"));
                add(new UserProfile("Guests"));
                add(new UserProfile("Strangers"));
            } catch (UserProfileException e) {
            }
        }};

        this.userProfiles.get(0).setPermissions("Parent");
        this.userProfiles.get(1).setPermissions("Child");
        this.userProfiles.get(2).setPermissions("Guest");
        this.userProfiles.get(3).setPermissions("Stranger");

        this.activeUserProfile = this.userProfiles.get(0);
        this.temperatureInside = 24.0f;
        this.temperatureOutside = 11.0f;
        this.simulationSpeed = 1.0f;
        this.houseLayout = null;
        this.userLocation = null;
        this.people = new ArrayList<>();
        this.modules = new ArrayList<>();
        modules.add(new SHC(this));
        modules.add(new SHP(this));
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a simulation in a string format.
     *
     * @return String representation of all the current attributes of the simulation.
     */
    @Override
    public String toString() {
        return "Simulation{" +
                "dateTime=" + dateTime +
                ", running=" + running +
                ", userProfiles=" + userProfiles +
                ", activeUProfile=" + activeUserProfile +
                ", temperatureInside=" + temperatureInside +
                ", temperatureOutside=" + temperatureOutside +
                ", houseLayout=" + houseLayout +
                ", houseLocation='" + +'\'' +
                ", people=" + people +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the list of {@link UserProfile}.
     *
     * @return The list of {@link UserProfile}.
     */
    public List<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    /**
     * This function sets the list of {@link UserProfile}.
     *
     * @param userProfiles The list of {@link UserProfile}.
     */
    public void setUserProfiles(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    /**
     * This function gets the list of active {@link UserProfile}.
     *
     * @return The list of active {@link UserProfile}.
     */
    public UserProfile getActiveUserProfile() {
        return activeUserProfile;
    }

    /**
     * This function sets the list of active {@link UserProfile}.
     *
     * @param activeUserProfile The list of active {@link UserProfile}.
     */
    public boolean setActiveUserProfile(UserProfile activeUserProfile) {
        if (userProfiles.contains(activeUserProfile)) {
            this.activeUserProfile = activeUserProfile;
            return true;
        }
        return false;
    }

    /**
     * Returns the formatted date time.
     *
     * @return String The date time of the simulation in the "yyyy-MM-dd HH:mm" format.
     */
    public String getDateTime() {
        return dateTime.format(formatter);
    }

    /**
     * This function sets the date time of the simulation.
     *
     * @param dateTime The date time string in the "yyyy-MM-dd HH:mm" format.
     */
    public void setDateTime(String dateTime) throws DateTimeFormatException {
        if (dateTime != null && !dateTime.equals("") && !dateTime.equals(" ")) {
            this.dateTime = LocalDateTime.parse(dateTime, formatter);
            return;
        }
        throw new DateTimeFormatException();
    }

    /**
     * This function shows if the simulation is running or not.
     *
     * @return Boolean representation of it.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * This function sets the state of the simulation to running or not running.
     *
     * @param running New state of the simulation.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * This function gets the temperature of inside the house.
     *
     * @return The temperature of inside the house.
     */
    public float getTemperatureInside() {
        return temperatureInside;
    }

    /**
     * This function sets the temperature of inside the house.
     *
     * @param temperatureInside The temperature of inside the house.
     * @return boolean representation of whether the temperature changed or not.
     */
    public boolean setTemperatureInside(float temperatureInside) {
        if (temperatureInside <= 35 && temperatureInside >= -30) {
            this.temperatureInside = temperatureInside;
            return true;
        }
        return false;
    }

    /**
     * This function gets the temperature of outside the house.
     *
     * @return The temperature of outside the house.
     */
    public float getTemperatureOutside() {
        return temperatureOutside;
    }

    /**
     * This function sets the temperature of outside the house.
     *
     * @param temperatureOutside The temperature of outside the house.
     * @return boolean representation of whether the temperature changed or not.
     */
    public boolean setTemperatureOutside(float temperatureOutside) {
        if (temperatureOutside <= 35 && temperatureOutside >= -30) {
            this.temperatureOutside = temperatureOutside;
            return true;
        }
        return false;
    }

    /**
     * This function gets the {@link HouseLayout} of this simulation.
     *
     * @return The {@link HouseLayout} of this simulation.
     */
    public HouseLayout getHouseLayout() {
        return houseLayout;
    }

    /**
     * This function sets the {@link HouseLayout} of this simulation.
     *
     * @param houseLayout The {@link HouseLayout} of this simulation.
     */
    public void setHouseLayout(HouseLayout houseLayout) {
        this.houseLayout = houseLayout;
    }

    /**
     * This function gets the user's location in the house (Room ID, or null if outside).
     *
     * @return The user's location in the house.
     */
    public String getUserLocation() {
        return userLocation;
    }

    /**
     * This function sets the user's location in the house.
     *
     * @param userLocation The user's new location in the house.
     * @return boolean representation of whether the location changed or not.
     */
    public boolean setUserLocation(String userLocation) {

        if (userLocation == null) {
            this.userLocation = null;
            return true;
        }

        ArrayList<Room> rooms = houseLayout.getRooms();
        for (Room room : rooms) {
            if (room.getId().equals(userLocation)) {
                this.userLocation = userLocation;
                return true;
            }
        }
        return false;
    }

    /**
     * This function gets the list of {@link Person} in this simulation.
     *
     * @return The list of {@link Person} in this simulation.
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * This function sets the list of {@link Person} in this simulation.
     *
     * @param people The list of {@link Person} in this simulation.
     */
    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    /**
     * This function gets the speed of the simulation
     *
     * @return The speed factor of the simulation. (ex: 2 is twice as fast as realtime)
     */
    public float getSimulationSpeed() {
        return simulationSpeed;
    }

    /**
     * This function sets the speed of the simulation.
     *
     * @param simulationSpeed The new speed factor of the simulation.
     */
    public void setSimulationSpeed(float simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    /**
     * Returns the list of modules attached to this simulation.
     *
     * @return {@link List<Module>} The list of modules attached to the simulation.
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Returns whether or not the SHP module, if present, has the away mode enabled.
     *
     * @return boolean
     */
    public boolean isAway() {
        Optional<Module> shp = modules.stream().filter(m -> m instanceof SHP).findFirst();
        if (shp.isPresent())
            return ((SHP) shp.get()).isAwayMode();
        return false;
    }

    /**
     * Returns the start of the away mode window for lights.
     *
     * @return {@link LocalTime}
     */
    public LocalTime getAwayTimeStart() {
        Optional<Module> shp = modules.stream().filter(m -> m instanceof SHP).findFirst();
        return shp.map(module -> ((SHP) module).getAwayTimeStartObj()).orElse(null);
    }

    /**
     * Returns the end of the away mode window for lights.
     *
     * @return {@link LocalTime}
     */
    public LocalTime getAwayTimeEnd() {
        Optional<Module> shp = modules.stream().filter(m -> m instanceof SHP).findFirst();
        return shp.map(module -> ((SHP) module).getAwayTimeEndObj()).orElse(null);
    }

    /**
     * Returns the date part of the current simulation date time.
     *
     * @return {@link LocalDate} The current date of the simulation.
     */
    @JsonIgnore
    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    /**
     * Returns the time part of the current simulation date time.
     *
     * @return {@link LocalTime} The current time of the simulation.
     */
    @JsonIgnore
    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    // ============================ OTHER METHODS ============================

    /**
     * This function collects all of the existing lights of the house layout and returns them as a list.
     *
     * @return The list of {@link Light} objects in this simulation's house layout.
     */
    @JsonIgnore
    public ArrayList<Light> getAllLights() {
        if (houseLayout != null)
            return houseLayout
                    .getRooms()
                    .stream()
                    .map(Room::getLights)
                    .reduce(new ArrayList<>(), (list, roomLights) -> {
                        list.addAll(roomLights);
                        return list;
                    });
        return new ArrayList<>();
    }

    /**
     * This function collects all of the existing doors of the house layout and returns them as a list.
     *
     * @return The list of {@link Door} objects in this simulation's house layout.
     */
    @JsonIgnore
    public ArrayList<Door> getAllDoors() {
        if (houseLayout != null)
            return houseLayout
                    .getRooms()
                    .stream()
                    .map(Room::getDoors)
                    .reduce(new ArrayList<>(), (list, roomDoors) -> {
                        list.addAll(roomDoors);
                        return list;
                    });
        return new ArrayList<>();
    }

    /**
     * This function collects all of the existing windows of the house layout and returns them as a list.
     *
     * @return The list of {@link Window} objects in this simulation's house layout.
     */
    @JsonIgnore
    public ArrayList<Window> getAllWindows() {
        if (houseLayout != null)
            return houseLayout
                    .getRooms()
                    .stream()
                    .map(Room::getWindows)
                    .reduce(new ArrayList<>(), (list, roomWindows) -> {
                        list.addAll(roomWindows);
                        return list;
                    });
        return new ArrayList<>();
    }

    /**
     * Executes a specific module command.
     *
     * @param command    The command to execute.
     * @param payload    The payload passed to the command.
     * @param sentByUser True if the command was sent by a user, false otherwise.
     */
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {

        // Find module which handles the given command
        Optional<Module> module = modules.stream()
                .filter(m -> m.getCommandList().contains(command))
                .findFirst();

        // If the module exists, execute the command
        if (module.isPresent()) {
            module.get().executeCommand(command, payload, sentByUser);
            return;
        }

        // No module can handle the given command
        throw new ModuleException(Logger.ERROR, "System", "UNKNOWN COMMAND '" + command + "'!");
    }

}
