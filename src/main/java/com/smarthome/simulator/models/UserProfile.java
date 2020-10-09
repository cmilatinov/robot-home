package com.smarthome.simulator.models;

import java.util.ArrayList;

public class UserProfile {
    private int id;
    private String name;
    private ArrayList<String> permissions;
    private Person person;

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                ", person=" + person +
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

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public UserProfile() {
    }

    public UserProfile(int id, String name, ArrayList<String> permissions, Person person) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.person = person;
    }
}
