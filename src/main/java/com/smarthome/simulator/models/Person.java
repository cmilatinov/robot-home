package com.smarthome.simulator.models;

public class Person {
    private int id;
    private String name;
    private int roomId;

    public Person(int id, String name, int roomId) {
        super();
        this.id = id;
        this.name = name;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", roomId=" + roomId + "]";
    }

    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
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
}
