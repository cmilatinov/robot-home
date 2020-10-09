package com.smarthome.simulator.models;

public class Door extends Id {
    private boolean open;

    @Override
    public String toString() {
        return "Door{" +
                "id='" + getId() + '\'' +
                "open=" + open +
                '}';
    }

    public Door() {
        super();
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Door(boolean open) {
        super();
        this.open = open;
    }
}
