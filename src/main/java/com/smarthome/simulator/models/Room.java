package com.smarthome.simulator.models;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Room extends IdentifiableObject {
    private String name;
    private Rectangle2D dimensions;
    private ArrayList<Window> windows;
    private ArrayList<Door> doors;
    private ArrayList<Light> lights;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rectangle2D getDimensions() {
        return dimensions;
    }

    public void setDimensions(Rectangle2D dimensions) {
        this.dimensions = dimensions;
    }

    public ArrayList<Window> getWindows() {
        return windows;
    }

    public void setWindows(ArrayList<Window> windows) {
        this.windows = windows;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public void setDoors(ArrayList<Door> doors) {
        this.doors = doors;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", dimensions=" + dimensions +
                ", windows=" + windows +
                ", doors=" + doors +
                ", lights=" + lights +
                '}';
    }

    public Room() {
        super();
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    public Room(String name, Rectangle2D dimensions, ArrayList<Window> windows, ArrayList<Door> doors, ArrayList<Light> lights) {
        super();
        this.name = name;
        this.dimensions = dimensions;
        this.windows = windows;
        this.doors = doors;
        this.lights = lights;
    }
}
