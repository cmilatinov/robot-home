package com.smarthome.simulator.models;

public class Door {
    private boolean open;

    @Override
    public String toString() {
        return "Door{" +
                "open=" + open +
                '}';
    }

    public Door() {
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Door(boolean open) {
        this.open = open;
    }
}
