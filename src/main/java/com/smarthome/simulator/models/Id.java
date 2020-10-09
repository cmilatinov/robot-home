package com.smarthome.simulator.models;

import java.util.UUID;

public class Id {
    private String id;

    public Id() {
         id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
