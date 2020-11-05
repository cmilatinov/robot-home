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

    /**
     * Is the door special (if it's a building entrance, backyard or garage door)
     */
    private boolean houseEntrance;

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor.
     */
    public Door() {
        this.open = true;
        this.locked = false;
        this.houseEntrance = false;
    }

    /**
     * Parameterized constructor.
     *
     * @param open If the door should be open or not.
     * @param lock If the door should be locked or not.
     * @param houseEntrance If the door is of special kind (building entrance, backyard and garage).
     */
    public Door(boolean open, boolean lock, boolean houseEntrance) {
        this.open = open;
        this.locked = lock;
        this.houseEntrance = houseEntrance;
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

    /**
     * This function tells if a door is a house entrance door (garage, building entrance or backyard).
     *
     * @return Boolean representation of it.
     */
    public boolean isHouseEntrance() {
        return houseEntrance;
    }

    /**
     * This function sets the door to be of type house entrance or not.
     *
     * @param houseEntrance New state of the lock.
     */
    public void setHouseEntrance(boolean houseEntrance) {
        this.houseEntrance = houseEntrance;
    }
}
