package com.smarthome.simulator.models;

import java.util.Random;

/**
 * This class represents a Person with its functionalities.
 */
public class Person extends IdentifiableObject {

    /**
     * List of material design colors. Pick one at random to assign to a person in order to make them easily distinguishable.
     */
    private static final String[] MATERIAL_COLORS = {
            "#D50000", "#C51162", "#AA00FF", "#6200EA",
            "#304FFE", "#2962FF", "#0091EA", "#00B8D4",
            "#00BFA5", "#00C853", "#64DD17", "#AEEA00",
            "#FFD600", "#FFAB00", "#FF6D00", "#DD2C00" };

    /**
     * Name of the person.
     */
    private final String name;

    /**
     * Color of the person's avatar.
     */
    private final String color;

    /**
     * The room in which the person is.
     * This has a value of null if the person is outside the house.
     */
    private String roomId;


    // ============================ CONSTRUCTORS ============================

    /**
     * Parameterized constructor.
     *
     * @param name   Name of the person.
     * @param roomId Room in which the person should be.
     */
    public Person(String name, String roomId) {
        super();
        this.name = name;
        this.color = MATERIAL_COLORS[new Random().nextInt(MATERIAL_COLORS.length)];
        this.roomId = roomId;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a person in a string format.
     *
     * @return String representation of all the current attributes of the person.
     */
    @Override
    public String toString() {
        return "Person {id='" + getId() +
                ", name=" + name +
                ", roomId=" + roomId +
                "}";
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the id of the room in which the person is.
     *
     * @return Id of the room.
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * This function sets in which room the person should be in.
     *
     * @param roomId Id of the room.
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * This function gets the name of the person
     *
     * @return The name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * This function gets the color of the person
     *
     * @return The color associated to this person.
     */
    public String getColor() {
        return color;
    }

}
