package com.smarthome.simulator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Zone class is a data structure made to represent the zone objects.
 */
public class Zone extends IdentifiableObject {

    /**
     * The name of the zone
     */
    private String name;

    /**
     * The list of rooms that are within this zone
     */
    private final List<Room> rooms = new ArrayList<>();

    /**
     * The list of periods for this zone
     */
    private final List<Period> periods = new ArrayList<>();

    /**
     * This represents whether this zone is the default one or not
     */
    private final boolean isDefault;

    /**
     * The parameterized constructor for the zone class
     *
     * @param name      The name of the zone
     * @param isDefault Whether or not it's a default
     */
    public Zone(String name, boolean isDefault) {
        this.name = name;
        this.isDefault = isDefault;
    }

    /**
     * This function gets the name of the zone
     *
     * @return the name of the zone
     */
    public String getName() {
        return name;
    }

    /**
     * This function sets the name of the zone
     *
     * @param name the name of the zone
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This function gets the list of rooms in the zone
     *
     * @return the list of rooms in the zone
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * This function gets the list of periods in the zone
     *
     * @return the list of periods in the zone
     */
    public List<Period> getPeriods() {
        return periods;
    }

    /**
     * This function checks if this zone instance is default one
     *
     * @return boolean representation of it.
     */
    public boolean isDefault() {
        return isDefault;
    }
}
