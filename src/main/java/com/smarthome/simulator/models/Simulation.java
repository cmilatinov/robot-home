package com.smarthome.simulator.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    private ArrayList<UserProfile> userProfiles;

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
     * The {@link HouseLayout} of the home.
     */
    private HouseLayout houseLayout;

    /**
     * The house location.
     */
    private String houseLocation;

    /**
     * The list of {@link Person} present in the house.
     */
    private ArrayList<Person> people;

    // ============================ CONSTRUCTORS ============================

    /**
     * Creates a simulation with default parameters.
     */
    public Simulation() {
        super();
        this.dateTime = LocalDateTime.now();
        this.running = false;

        this.userProfiles = new ArrayList<UserProfile>() {{
            add(new UserProfile("Parents"));
            add(new UserProfile("Children"));
            add(new UserProfile("Guests"));
            add(new UserProfile("Strangers"));
        }};

        this.activeUserProfile = this.userProfiles.get(0);
        this.temperatureInside = 24.0f;
        this.temperatureOutside = 11.0f;
        this.houseLayout = null;
        this.houseLocation = "";
        this.people = new ArrayList<>();
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a simulation in a string format.
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
                ", houseLocation='" + houseLocation + '\'' +
                ", people=" + people +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the list of {@link UserProfile}.
     * @return The list of {@link UserProfile}.
     */
    public ArrayList<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    /**
     * This function sets the list of {@link UserProfile}.
     * @param userProfiles The list of {@link UserProfile}.
     */
    public void setUserProfiles(ArrayList<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    /**
     * This function gets the list of active {@link UserProfile}.
     * @return The list of active {@link UserProfile}.
     */
    public UserProfile getActiveUserProfile() {
        return activeUserProfile;
    }

    /**
     * This function sets the list of active {@link UserProfile}.
     * @param activeUserProfile The list of active {@link UserProfile}.
     */
    public void setActiveUserProfile(UserProfile activeUserProfile) {
        this.activeUserProfile = activeUserProfile;
    }

    /**
     * Returns the formatted date time.
     * @return String The date time of the simulation in the "yyyy-MM-dd HH:mm" format.
     */
    public String getDateTime() {
        return dateTime.format(formatter);
    }

    /**
     * This function sets the date time of the simulation.
     * @param dateTime The date time string in the "yyyy-MM-dd HH:mm" format.
     */
    public void setDateTime(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * This function shows if the simulation is running or not.
     * @return Boolean representation of it.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * This function sets the state of the simulation to running or not running.
     * @param running New state of the simulation.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * This function gets the temperature of inside the house.
     * @return The temperature of inside the house.
     */
    public float getTemperatureInside() {
        return temperatureInside;
    }

    /**
     * This function sets the temperature of inside the house.
     * @param temperatureInside The temperature of inside the house.
     */
    public void setTemperatureInside(float temperatureInside) {
        this.temperatureInside = temperatureInside;
    }

    /**
     * This function gets the temperature of outside the house.
     * @return The temperature of outside the house.
     */
    public float getTemperatureOutside() {
        return temperatureOutside;
    }

    /**
     * This function sets the temperature of outside the house.
     * @param temperatureOutside The temperature of outside the house.
     */
    public void setTemperatureOutside(float temperatureOutside) {
        this.temperatureOutside = temperatureOutside;
    }

    /**
     * This function gets the {@link HouseLayout} of this simulation.
     * @return The {@link HouseLayout} of this simulation.
     */
    public HouseLayout getHouseLayout() {
        return houseLayout;
    }

    /**
     * This function sets the {@link HouseLayout} of this simulation.
     * @param houseLayout The {@link HouseLayout} of this simulation.
     */
    public void setHouseLayout(HouseLayout houseLayout) {
        this.houseLayout = houseLayout;
    }

    /**
     * This function gets the house location.
     * @return The house location.
     */
    public String getHouseLocation() {
        return houseLocation;
    }

    /**
     * This function sets the house location.
     * @param houseLocation The house location.
     */
    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    /**
     * This function gets the list of {@link Person} in this simulation.
     * @return The list of {@link Person} in this simulation.
     */
    public ArrayList<Person> getPeople() {
        return people;
    }

    /**
     * This function sets the list of {@link Person} in this simulation.
     * @param people The list of {@link Person} in this simulation.
     */
    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    // ============================ OTHER METHODS ============================

    /**
     * This function adds a new {@link Person} in the list of people in this simulation.
     * @param person The {@link Person} to be added.
     */
    public void addPerson(Person person) {
        people.add(person);
    }

    /**
     * This function removes a new {@link Person} in the list of people in this simulation.
     * @param person The {@link Person} to be removed.
     */
    public void removePerson(Person person) {
        people.remove(person);
    }

}
