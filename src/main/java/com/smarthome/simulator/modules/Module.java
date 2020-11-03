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

    public abstract void executeCommand(String command, Map<String, Object> payload, boolean sentByUser);

    public boolean checkPermission(String command, boolean sentByUser) {
        boolean allowed;

        if (sentByUser) {
            allowed = simulation.getActiveUserProfile().getPermissions().stream().anyMatch(p ->
                    p.equals(command)
            );
        } else {
            allowed = true;
        }

        if (!allowed) {
            System.out.println("Permission Denied for " + command);
        }

        return allowed;
    }
}
