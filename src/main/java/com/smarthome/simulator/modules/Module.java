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

    public void executeCommand(String command, Map<String, String> payload, boolean sentByUser) {
        boolean allowed;

        if (sentByUser) {
            allowed = checkPermission(command);
        } else {
            allowed = true;
        }

        if (!allowed) {
            System.out.println("Permission Denied for " + command);
            return;
        }
    }

    private boolean checkPermission(String command) {
        return simulation.getActiveUserProfile().getPermissions().stream().anyMatch(p ->
                p.equals(command)
        );
    }
}
