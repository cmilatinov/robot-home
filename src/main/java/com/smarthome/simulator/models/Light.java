package com.smarthome.simulator.models;

/**
 * This class represents a light with its functionalities.
 */
public class Light extends IdentifiableObject {
    /**
     * Is the light on or not.
     */
    private boolean on;

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor.
     */
    public Light() {
        super();
    }

    /**
     * Parameterized constructor.
     * @param on Future state of the light.
     */
    public Light(boolean on) {
        super();
        this.on = on;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a light in a string format.
     * @return String representation of all the current attributes of the light.
     */
    @Override
    public String toString() {
        return "Light{" +
                "id='" + getId() + '\'' +
                "on=" + on +
                '}';
    }

    /**
     * This function is meant to compare two Light objects and to verify if they are the same.
     * @param other Light object that represents the light being compared too.
     * @return Boolean value confirming or not if the two lights are the same.
     */
    public boolean equals(Light other)
    {
        if(this.on == other.on)
        {
            return true;
        }
        return false;
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function shows if a light is on or not.
     * @return Boolean representation of it.
     */
    public boolean isOn() {
        return on;
    }

    /**
     * This function sets the light to be on or off.
     * @param on New state of the light.
     */
    public void setOn(boolean on) {
        this.on = on;
    }
}
