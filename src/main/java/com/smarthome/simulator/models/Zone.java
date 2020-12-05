package com.smarthome.simulator.models;

import java.util.ArrayList;
import java.util.List;

public class Zone extends IdentifiableObject {
    private final boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    private String name;
    private final List<Room> rooms = new ArrayList<>();
    private final List<Period> periods = new ArrayList<>();

    public Zone(String name,boolean isDefault) {
        this.name = name;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Period> getPeriods() {
        return periods;
    }
}
