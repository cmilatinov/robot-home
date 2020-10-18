package com.smarthome.simulator.models;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents a user profile with its functionalities.
 */
public class UserProfile extends IdentifiableObject {

    /**
     * Name of the user profile.
     */
    private String name;

    /**
     * List of {@link String} representing permissions of the user profile.
     */
    private ArrayList<String> permissions;

    // ============================ CONSTRUCTORS ============================

    /**
     * Constructor for a profile with an empty permission list.
     *
     * @param name The name of the user profile.
     */
    public UserProfile(String name) {
        this.name = name;
        this.permissions = new ArrayList<>();
    }

    /**
     * Parameterized constructor.
     *
     * @param name        The name of the user profile.
     * @param permissions The list of {@link String} representing the permissions of the user profile.
     */
    public UserProfile(String name, ArrayList<String> permissions) {
        super();
        this.name = name;
        this.permissions = permissions;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a user profile in a string format.
     *
     * @return String representation of all the current attributes of the user profile.
     */
    @Override
    public String toString() {
        return "UserProfile{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the name of the user profile.
     *
     * @return The name of the user profile.
     */
    public String getName() {
        return name;
    }

    /**
     * This function sets the name of the user profile.
     *
     * @param name The name of the user profile.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This function gets the list of {@link String} representing the permissions of the user profile.
     *
     * @return The list of {@link String} representing the permissions of the user profile.
     */
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    /**
     * This function sets the list of {@link String} representing the permissions of the user profile.
     *
     * @param permissions The list of {@link String} representing the permissions of the user profile.
     */
    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile)) return false;
        UserProfile that = (UserProfile) o;
        return this.getId().equals(that.getId());
    }

}
