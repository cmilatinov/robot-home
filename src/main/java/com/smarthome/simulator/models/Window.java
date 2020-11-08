package com.smarthome.simulator.models;

/**
 * This class represents a window with its functionalities.
 */
public class Window extends IdentifiableObject {

    /**
     * Is the window open or not.
     */
    private boolean open;

    /**
     * Is the window blocked or not.
     */
    private boolean blocked;

    // ============================ CONSTRUCTORS ============================

    /**
     * Default constructor.
     */
    public Window() {
        this.open = false;
        this.blocked = false;
    }

    /**
     * Parameterized constructor.
     *
     * @param open    If the window should be open or not.
     * @param blocked If the window should be blocked or not.
     */
    public Window(boolean open, boolean blocked) {
        this.open = open;
        this.blocked = blocked;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a window in a string format.
     *
     * @return String representation of all the current attributes of the window.
     */
    @Override
    public String toString() {
        return "Window{" +
                "id='" + getId() + '\'' +
                "open=" + open +
                "blocked=" + blocked +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function shows if a window is open or not.
     *
     * @return Boolean representation of it.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * This function sets the window to be open or closed.
     *
     * @param open New state of the door.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * This function tells if a window is blocked or not.
     *
     * @return Boolean representation of it.
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * This function sets the window to be blocked or unblocked.
     *
     * @param blocked New state of the lock.
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

}
