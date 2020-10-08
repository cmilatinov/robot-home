package com.smarthome.simulator.models;

public class Window {
    private String state;

    public Window(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Window{" +
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
