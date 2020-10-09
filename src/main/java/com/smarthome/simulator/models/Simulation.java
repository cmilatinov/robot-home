package com.smarthome.simulator.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Simulation {
    private LocalDateTime dateTime;
    private boolean running;
    private UserProfile userProfile;

    @Override
    public String toString() {
        return "Simulation{" +
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

    public Simulation() {
    }

    public Simulation(LocalDateTime dateTime, boolean running, UserProfile userProfile, float temperatureInside, float temperatureOutside, HouseLayout houseLayout, String houseLocation, ArrayList<Person> people) {
        this.dateTime = dateTime;
        this.running = running;
        this.userProfile = userProfile;
        this.temperatureInside = temperatureInside;
        this.temperatureOutside = temperatureOutside;
        this.houseLayout = houseLayout;
        this.houseLocation = houseLocation;
        this.people = people;
    }

    private float temperatureInside;
    private float temperatureOutside;
    private HouseLayout houseLayout;
    private String houseLocation;
    private ArrayList<Person> people;
}
