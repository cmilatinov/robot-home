package com.smarthome.simulator.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Simulation {

    /**
     * The date and time of the simulation.
     */
    private LocalDateTime dateTime;

    /**
     * The state of the simulation (running or not).
     */
    private boolean running;

    /**
     * The active {@link UserProfile} for the simulation.
     */
    private UserProfile userProfile;

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
     *
     */
    private String houseLocation;

    /**
     * The list of people present in the house.
     */
    private ArrayList<Person> people;


    /**
     * Creates a simulation with default parameters.
     */
    public Simulation() {
        this.dateTime = LocalDateTime.now();
        this.running = false;
        this.userProfile = null;
        this.temperatureInside = 24.0f;
        this.temperatureOutside = 11.0f;
        this.houseLayout = null;
        this.houseLocation = "";
        this.people = new ArrayList<>();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public float getTemperatureInside() {
        return temperatureInside;
    }

    public void setTemperatureInside(float temperatureInside) {
        this.temperatureInside = temperatureInside;
    }

    public float getTemperatureOutside() {
        return temperatureOutside;
    }

    public void setTemperatureOutside(float temperatureOutside) {
        this.temperatureOutside = temperatureOutside;
    }

    public HouseLayout getHouseLayout() {
        return houseLayout;
    }

    public void setHouseLayout(HouseLayout houseLayout) {
        this.houseLayout = houseLayout;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return  "Simulation{" +
                "dateTime=" + dateTime +
                ", running=" + running +
                ", userProfile=" + userProfile +
                ", temperatureInside=" + temperatureInside +
                ", temperatureOutside=" + temperatureOutside +
                ", houseLayout=" + houseLayout +
                ", houseLocation='" + houseLocation + '\'' +
                ", people=" + people +
                '}';
    }

}
