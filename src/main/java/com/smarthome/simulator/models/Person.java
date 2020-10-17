package com.smarthome.simulator.models;

/**
 * This class represents a Person with its functionalities.
 */
public class Person extends IdentifiableObject {

    /**
     * Name of the person.
     */
    private String name;

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
        return "Person [id='" + getId() + ", name=" + name + ", roomId=" + roomId + "]";
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
     * This function sets the name of the person.
     *
     * @param name The name of the person.
     */
    public void setName(String name) {
        this.name = name;
    }

}
