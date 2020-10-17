package com.smarthome.simulator.models;

/**
 * This class represents a door with its functionalities.
 */
public class Door extends IdentifiableObject {

    /**
     * Is the door open or not.
     */
    private boolean open;

    /**
     * Is the door locked or not.
     */
    private boolean locked;

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor.
     */
    public Door() {
        this.open = true;
        this.locked = false;
    }

    /**
     * Parameterized constructor.
     *
     * @param open If the door should be open or not.
     * @param lock If the door should be locked or not.
     */
    public Door(boolean open, boolean lock) {
        this.open = open;
        this.locked = lock;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a door in a string format.
     *
     * @return String representation of all the current attributes of the door.
     */
    @Override
    public String toString() {
        return "Door{" +
                "id='" + getId() + '\'' +
                "open=" + open +
                "locked=" + locked +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function shows if a door is open or not.
     *
     * @return Boolean representation of it.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * This function sets the door to be open or closed.
     *
     * @param open New state of the door.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * This function tells if a door is locked or not.
     *
     * @return Boolean representation of it.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * This function sets the door to be locked or unlocked.
     *
     * @param lock New state of the lock.
     */
    public void setLocked(boolean lock) {
        this.locked = lock;
    }

}
