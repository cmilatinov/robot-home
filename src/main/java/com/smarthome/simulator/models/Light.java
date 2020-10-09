package com.smarthome.simulator.models;

public class Light {
    private boolean on;

    public Light(boolean on) {
        this.on = on;
    }

    @Override
    public String toString() {
        return "Light{" +
                "on=" + on +
                '}';
    }

    public Light() {
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
