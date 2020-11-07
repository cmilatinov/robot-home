package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Simulation;
import java.util.List;
import java.util.Map;

/**
 * Abstract class representing the modules of the simulation.
 */
public abstract class Module {

    /**
     * Name of the module.
     */
    private final String name;

    /**
     * The currently running {@link Simulation}.
     */
    protected final Simulation simulation;

    /**
     * Parameterized constructor.
     *
     * @param name Name of the module created.
     * @param simulation the current {@link Simulation} running.
     */
    public Module(String name, Simulation simulation) {
        this.name = name;
        this.simulation = simulation;
    }

    /**
     * Gets the list of permissions/commands that the Module is responsible for/can execute.
     *
     * @return List of Strings representing the permissions/commands pertaining to the module
     */
    public abstract List<String> getPermissions();

    /**
     * Executes a command given by a user or other system module.
     *
     * @param command The name of the command to be executed.
     * @param payload The arguments for the command.
     * @param sentByUser Whether the command was called by a user or not. False if called by other system modules.
     */
    public abstract void executeCommand(String command, Map<String, Object> payload, boolean sentByUser);

    /**
     * Function verifies if the user is allowed to run the command.
     *
     * @param command The name of the command to be executed.
     * @param sentByUser Whether the command was called by a user or not. False if called by other system modules.
     */
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
            System.out.println("PERMISSION DENIED. '" + simulation.getActiveUserProfile().getName() + "' doesn't have " +
                    "the permission: '" + command + "'");
        }

        return allowed;
    }

}
