package com.smarthome.simulator.models;

/**
 * This class stores the dimensions of a room.
 */
public class RoomDimensions {

    /**
     * Coordinates of the top-left point of the room in the layout.
     */
    private float x, y;

    /**
     * Width and height of the room in the layout.
     */
    private float width, height;

    // ============================ CONSTRUCTORS ============================

    /**
     * Creates a new {@link RoomDimensions} using the provided values.
     *
     * @param x      The x-coordinate of the top-left corner of the room in the layout.
     * @param y      The y-coordinate of the top-left corner of the room in the layout.
     * @param width  The width of the room in the layout.
     * @param height The height of the room in the layout.
     */
    public RoomDimensions(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a window in a string format.
     *
     * @return String representation of all the current attributes of the window.
     */
    @Override
    public String toString() {
        return "RoomDimensions{" +
                "x=" + x +
                "y=" + y +
                "width=" + width +
                "height=" + height +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * Returns the x-coordinate of the top-left corner of the room in the layout.
     *
     * @return float The x-coordinate of the top-left corner of the room in pixels.
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the top-left corner of the room in the layout.
     *
     * @param x The new x-coordinate of the top-left corner of the room in pixels.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Returns the y-coordinate of the top-left corner of the room in the layout.
     *
     * @return float The y-coordinate of the top-left corner of the room in pixels.
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the top-left corner of the room in the layout.
     *
     * @param y The new y-coordinate of the top-left corner of the room in pixels.
     */
    public void setY(float y) {
        this.y = y;
    }


    /**
     * Returns the width of the room in the layout.
     *
     * @return float The width of the room in pixels.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the new width of the room in the layout.
     *
     * @param width The new width of the room in pixels.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Returns the height of the room in the layout.
     *
     * @return float The height of the room in pixels.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the new height of the room in the layout.
     *
     * @param height The new height of the room in pixels.
     */
    public void setHeight(float height) {
        this.height = height;
    }

}
