package com.smarthome.simulator.models;

import java.util.UUID;

/**
 * This class generates a random ID for any objects of the class that inherits it.
 */
public class IdentifiableObject {

    /**
     * String representing the ID
     */
    private String id;

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor
     */
    public IdentifiableObject() {
        id = UUID.randomUUID().toString();
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gives the id of this object
     *
     * @return String representation of the id
     */
    public String getId() {
        return id;
    }

}
