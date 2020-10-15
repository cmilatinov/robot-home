package com.smarthome.simulator.models;

/**
 * This class represents a window with its functionalities.
 */
public class Window extends IdentifiableObject {
    /**
     * The state of the window.
     */
    private String state;

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor.
     */
    public Window() {
        super();
    }

    /**
     * Parameterized constructor.
     * @param state The state of the window.
     */
    public Window(String state) {
        super();
        this.state = state;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a window in a string format.
     * @return String representation of all the current attributes of the window.
     */
    @Override
    public String toString() {
        return "Window{" +
                "id='" + getId() + '\'' +
                "state='" + state + '\'' +
                '}';
    }

    /**
     * This function is meant to compare two Window objects and to verify if they are the same.
     * @param other Window object that represents the window being compared too.
     * @return Boolean value confirming or not if the two windows are the same.
     */
    public boolean equals(Window other)
    {
        if(state.equalsIgnoreCase(other.state))
        {
            return true;
        }

        return false;
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the state of the window.
     * @return The state of the window.
     */
    public String getState() {
        return state;
    }

    /**
     * This function sets the state of the window.
     * @param state The state of the window.
     */
    public void setState(String state) {
        this.state = state;
    }
}
