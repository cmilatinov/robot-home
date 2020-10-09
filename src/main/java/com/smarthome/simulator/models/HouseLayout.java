package com.smarthome.simulator.models;

import java.util.ArrayList;

public class HouseLayout {
    private int id;
    private String name;
    private ArrayList<Room> rooms;

    @Override
    public String toString() {
        return "HouseLayout{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    }

    public HouseLayout(int id, String name, ArrayList<Room> rooms) {
        this.id = id;
        this.name = name;
        this.rooms = rooms;
    }
}
