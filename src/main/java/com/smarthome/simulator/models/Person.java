package com.smarthome.simulator.models;

public class Person extends Id {
    private String name;
    private int roomId;

    public Person(String name, int roomId) {
        super();
        this.name = name;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Person [id='" + getId() + ", name=" + name + ", roomId=" + roomId + "]";
    }

    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
