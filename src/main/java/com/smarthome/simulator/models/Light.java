package com.smarthome.simulator.models;

/**
 * This class represents a light with its functionalities.
 */
public class Light extends IdentifiableObject {

    /**
     * Is the light on or not.
     */
    private boolean on;
    private boolean awayLight;

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor.
     */
    public Light() {
        this.on = true;
        this.awayLight = true;
    }

    /**
     * Parameterized constructor.
     *
     * @param on The state of the light.
     */
    public Light(boolean on) {
        this.on = on;
    }

    /**
     * Parameterized constructor.
     *
     * @param on The state of the light.
     * @param awayLight Whether or not the light stays on during away mode.
     */
    public Light(boolean on, boolean awayLight) {
        this.on = on;
        this.awayLight = awayLight;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a light in a string format.
     *
     * @return String representation of all the current attributes of the light.
     */
    @Override
    public String toString() {
        return "Light{" +
                "id='" + getId() + '\'' +
                "on=" + on +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function shows if a light is on or not.
     *
     * @return Boolean representation of it.
     */
    public boolean isOn() {
        return on;
    }

    /**
     * This function sets the light to be on or off.
     *
     * @param on New state of the light.
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * This function shows if a light will stay on during away mode or not
     *
     * @return Boolean representation of it.
     */
    public boolean isAwayLight() {
        return awayLight;
    }

    /**
     * This function sets whether the light will remain on during away mode or not
     *
     * @param awayLight
     */
    public void setAwayLight(boolean awayLight) {
        this.awayLight = awayLight;
    }
}
