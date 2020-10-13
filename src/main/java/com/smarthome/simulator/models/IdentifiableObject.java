package com.smarthome.simulator.models;

import java.util.UUID;

public class IdentifiableObject {
    private String id;

    public IdentifiableObject() {
         id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
