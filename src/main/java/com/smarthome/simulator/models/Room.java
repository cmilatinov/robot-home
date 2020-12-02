package com.smarthome.simulator.models;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class represents a room with its functionalities.
 */
public class Room extends IdentifiableObject {

    /**
     * Name of the room.
     */
    private String name;

    /**
     * Dimensions {@link Rectangle2D} of the room.
     */
    private RoomDimensions dimensions;

    /**
     * List of {@link Window} in the room.
     */
    private ArrayList<Window> windows;

    /**
     * List of {@link Door} in the room.
     */
    private ArrayList<Door> doors;

    /**
     * List of {@link Light} in the room.
     */
    private ArrayList<Light> lights;

    /**
     *
     */
    private int zone = 0; // 0 means no zone

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor.
     */
    public Room() {
        super();
    }

    /**
     * Parameterized constructor.
     *
     * @param name       Name of the room.
     * @param dimensions Dimensions of the room.
     * @param windows    List of windows in the room.
     * @param doors      List of doors in the room.
     * @param lights     List of lights in the room.
     */
    public Room(String name, RoomDimensions dimensions, ArrayList<Window> windows, ArrayList<Door> doors, ArrayList<Light> lights) {
        super();
        this.name = name;
        this.dimensions = dimensions;
        this.windows = windows;
        this.doors = doors;
        this.lights = lights;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a room in a string format.
     *
     * @return String representation of all the current attributes of the room.
     */
    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", dimensions=" + dimensions +
                ", windows=" + windows +
                ", doors=" + doors +
                ", lights=" + lights +
                '}';
    }

    /**
     * This function is meant to compare two Room objects and to verify if they are the same.
     *
     * @param other Room object that represents the room being compared too.
     * @return Boolean value confirming or not if the two rooms are the same.
     */
    public boolean equals(Room other) {
        if (this.name.equalsIgnoreCase(other.name)) {
            if (this.dimensions.equals(other.dimensions)) {
                if (this.windows.equals(other.windows)) {
                    if (this.doors.equals(other.doors)) {
                        if (this.lights.equals(other.lights)) {
                            return true;
                        }
                    }
                }
            }

        }

        return false;
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the name of the room.
     *
     * @return The name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * This function sets the name of the room.
     *
     * @param name The name of the room.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This function gets the dimensions of the room.
     *
     * @return The dimensions of the room.
     */
    public RoomDimensions getDimensions() {
        return dimensions;
    }

    /**
     * This function sets the dimensions of the room.
     *
     * @param dimensions The dimensions of the room.
     */
    public void setDimensions(RoomDimensions dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * This function gets the list of windows in the room.
     *
     * @return The list of windows in the room.
     */
    public ArrayList<Window> getWindows() {
        return windows;
    }

    /**
     * This function sets the list of windows in the room.
     *
     * @param windows The list of windows in the room.
     */
    public void setWindows(ArrayList<Window> windows) {
        this.windows = windows;
    }

    /**
     * This function gets the list of doors in the room.
     *
     * @return The list of doors in the room.
     */
    public ArrayList<Door> getDoors() {
        return doors;
    }

    /**
     * This function sets the list of doors in the room.
     *
     * @param doors The list of doors in the room.
     */
    public void setDoors(ArrayList<Door> doors) {
        this.doors = doors;
    }

    /**
     * This function gets the list of lights in the room.
     *
     * @return The list of lights in the room.
     */
    public ArrayList<Light> getLights() {
        return lights;
    }

    /**
     * This function sets the list of lights in the room.
     *
     * @param lights The list of lights in the room.
     */
    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

}
