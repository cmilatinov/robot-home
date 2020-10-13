package com.smarthome.simulator.models;

public class Light extends IdentifiableObject {
    private boolean on;

    public Light(boolean on) {
        super();
        this.on = on;
    }

    @Override
    public String toString() {
        return "Light{" +
                "id='" + getId() + '\'' +
                "on=" + on +
                '}';
    }

    public Light() {
        super();
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
