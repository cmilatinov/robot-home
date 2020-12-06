package com.smarthome.simulator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smarthome.simulator.exceptions.ModuleException;
import com.smarthome.simulator.exceptions.UserProfileException;
import com.smarthome.simulator.modules.Module;
import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHH;
import com.smarthome.simulator.modules.SHP;
import com.smarthome.simulator.utils.DelayedRunnable;
import com.smarthome.simulator.utils.Logger;
import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.utils.TaskDispatcher;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Simulation {

    /**
     * TaskDispatcher that handles scheduled tasks.
     */
    private static TaskDispatcher dispatcher;

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

    /**
     * Specifies the first month of winter
     */
    private int startWinterMonth;

    /**
     * Specifies the last month of winter
     */
    private int endWinterMonth;

    /**
     * The default temperature for winter when the home is in away mode
     */
    private float winterTemperature;

    /**
     * The default temperature for summer when the home is in away mode
     */
    private float summerTemperature;

    // ============================ CONSTRUCTORS ============================

    /**
     * Creates a simulation with default parameters.
     */
    public Simulation() {
        super();

        this.dispatcher = new TaskDispatcher(this);
        dispatcher.start();
        dispatcher.schedule(new DelayedRunnable() {

            long delay = (long)5*1000000000;

            @Override
            public long getDelay() {
                return delay;
            }

            @Override
            public void setDelay(long delay) {
                this.delay = delay;
            }

            @Override
            public boolean isPeriodic() {
                return true;
            }

            @Override
            public long getPeriod() {
                return (long)5*1000000000;
            }

            @Override
            public void run() {
                SmartHomeSimulator.LOGGER.log(Logger.DEBUG, "Timer", "Task Schedule Test");
            }
        });

        this.dateTime = LocalDateTime.now();
        this.running = false;

        this.userProfiles = new ArrayList<UserProfile>() {{
            try {
                add(new UserProfile("Parents"));
                add(new UserProfile("Children"));
                add(new UserProfile("Guests"));
                add(new UserProfile("Strangers"));
            } catch (UserProfileException ignored) {
            }
        }};

        this.userProfiles.get(0).setPermissions("Parent");
        this.userProfiles.get(1).setPermissions("Child");
        this.userProfiles.get(2).setPermissions("Guest");
        this.userProfiles.get(3).setPermissions("Stranger");
        this.activeUserProfile = this.userProfiles.get(0);
        this.temperatureOutside = 11.0f;
        this.simulationSpeed = 1.0f;
        this.houseLayout = null;
        this.userLocation = null;
        this.people = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.startWinterMonth = 10;
        this.endWinterMonth = 3;
        this.winterTemperature = 24.0f;
        this.summerTemperature = 16.0f;

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
     * This function returns a reference to the dispatcher object.
     *
     * @return The {@link TaskDispatcher}
     */
    public static TaskDispatcher getDispatcher() {
        return dispatcher;
    }

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
    public void setDateTime(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime, formatter);
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

    public int getStartWinterMonth() {
        return startWinterMonth;
    }

    public void setStartWinterMonth(int startWinterMonth) {
        this.startWinterMonth = startWinterMonth;
    }

    public int getEndWinterMonth() {
        return endWinterMonth;
    }

    public void setEndWinterMonth(int endWinterMonth) {
        this.endWinterMonth = endWinterMonth;
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

    // ============================ OTHER METHODS ============================

    /**
     * @return the season we are currently in
     */
    public String getCurrentSeason() {
        // get the current month
        int month = dateTime.getMonthValue();

        // initial winter range --> october 1st to march 31st
        if (month >= this.startWinterMonth || month <= this.endWinterMonth) {
            return "Winter";
        } else {
            return "Summer";
        }
    }

    /**
     * Registers a module for the simulation, adding its functionality to the simulation.
     *
     * @param moduleClass The class of the module to instantiate.
     */
    public void registerModule(Class<? extends Module> moduleClass) {

        // Check if there already is a module of this type
        Optional<Module> existingModule = modules.stream().filter(m -> m.getClass().equals(moduleClass)).findFirst();
        if (existingModule.isPresent())
            throw new ModuleException("Module of class " + moduleClass + " is already instantiated for this simulation instance.");

        try {

            // Try adding the module to the simulation
            modules.add(moduleClass.getConstructor(Simulation.class).newInstance(this));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ModuleException();
        }

    }

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
        Optional<Module> moduleOptional = modules.stream()
                .filter(m -> m.getCommandList().contains(command))
                .findFirst();

        // If the module exists, execute the command
        if (moduleOptional.isPresent()) {

            // Reference to the module
            Module module = moduleOptional.get();

            // If the command was sent by the user, check if the active user profile has the needed permission.
            if (sentByUser && !module.checkPermission(command))
                return;

            // Log command
            if (SmartHomeSimulator.LOGGER != null)
                SmartHomeSimulator.LOGGER.log(Logger.INFO, module.getName(), "Executing command '" + command + "'");

            // Execute command
            module.executeCommand(command, payload, sentByUser);
            return;
        }

        // No module can handle the given command
        throw new ModuleException(Logger.ERROR, "System", "UNKNOWN COMMAND '" + command + "'!");
    }

}
