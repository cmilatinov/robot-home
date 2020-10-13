package com.smarthome.simulator.models;

public class Door extends IdentifiableObject {
    private boolean open;
    private boolean lock;

    @Override
    public String toString() {
        return "Door{" +
                "id='" + getId() + '\'' +
                "open=" + open +
                "lock=" + lock +
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

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public Door(boolean open, boolean lock) {
        super();
        this.open = open;
        this.lock = lock;
    }
}
