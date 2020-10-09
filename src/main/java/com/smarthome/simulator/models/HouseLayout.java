package com.smarthome.simulator.models;

import java.util.ArrayList;

public class HouseLayout extends Id {
    private String name;
    private ArrayList<Room> rooms;

    @Override
    public String toString() {
        return "HouseLayout{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public HouseLayout() {
        super();
    }

    public HouseLayout(String name, ArrayList<Room> rooms) {
        super();
        this.name = name;
        this.rooms = rooms;
    }
}
