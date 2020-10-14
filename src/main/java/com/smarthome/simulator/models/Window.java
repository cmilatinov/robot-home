package com.smarthome.simulator.models;

public class Window extends IdentifiableObject {
    private String state;

    public Window(String state) {
        super();
        this.state = state;
    }

    public Window() {
        super();
    }

    @Override
    public String toString() {
        return "Window{" +
                "id='" + getId() + '\'' +
                "state='" + state + '\'' +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
