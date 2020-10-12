package com.smarthome.simulator.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Simulation extends Id {
    private LocalDateTime dateTime;
    private boolean running;
    private ArrayList<UserProfile> userProfiles;
    private UserProfile activeUserProfile;
    private float temperatureInside;
    private float temperatureOutside;
    private HouseLayout houseLayout;
    private String houseLocation;
    private ArrayList<Person> people;

    public void addPerson(Person person) {
        people.add(person);
    }

    public void removePerson(Person person) {
        people.remove(person);
    }

    public ArrayList<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(ArrayList<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public UserProfile getActiveUserProfile() {
        return activeUserProfile;
    }

    public void setActiveUserProfile(UserProfile activeUserProfile) {
        this.activeUserProfile = activeUserProfile;
    }

    @Override
    public String toString() {
        return "Simulation{" +
                "id='" + getId() + '\'' +
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

    public Simulation() {
        super();
    }

    public Simulation(LocalDateTime dateTime, boolean running, ArrayList<UserProfile> userProfiles, UserProfile activeUserProfile, float temperatureInside, float temperatureOutside, HouseLayout houseLayout, String houseLocation, ArrayList<Person> people) {
        super();
        this.dateTime = dateTime;
        this.running = running;
        this.userProfiles = userProfiles;
        this.activeUserProfile = activeUserProfile;
        this.temperatureInside = temperatureInside;
        this.temperatureOutside = temperatureOutside;
        this.houseLayout = houseLayout;
        this.houseLocation = houseLocation;
        this.people = people;
    }
}
