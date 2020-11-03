package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Simulation;

import java.util.List;
import java.util.Map;

public abstract class Module {
    private final String name;
    protected final Simulation simulation;

    public Module(String name, Simulation simulation) {
        this.name = name;
        this.simulation = simulation;
    }

    public abstract List<String> getPermissions();

    public abstract void executeCommand(String name, Map<String, String> payload, boolean sentByUser);
}
