package com.smarthome.simulator.modules;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.exceptions.ModuleException;
import com.smarthome.simulator.models.Simulation;
import com.smarthome.simulator.utils.Logger;

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
     * Creates a module with a given name and simulation reference.
     *
     * @param name       The name of the module.
     * @param simulation The simulation instance to bind to the module.
     */
    public Module(String name, Simulation simulation) {
        this.name = name;
        this.simulation = simulation;
    }

    /**
     * Returns this module's list of commands that can be executed.
     *
     * @return {@link List<String>} The list of commands associated to this module.
     */
    public abstract List<String> getCommandList();

    /**
     * Executes a specific module command with the given payload.
     *
     * @param command    The command to execute.
     * @param payload    The payload to pass to the command.
     * @param sentByUser True if the command was sent by the user, false otherwise.
     */
    public abstract void executeCommand(String command, Map<String, Object> payload, boolean sentByUser);

    /**
     * Function verifies if the user is allowed to run the command.
     *
     * @param command The name of the command to be executed.
     */
    public boolean checkPermission(String command) {
        try {
            // Check if user has permission to execute this command
            if (!simulation.getActiveUserProfile()
                    .getPermissions()
                    .contains(command)) {
                throw new ModuleException(Logger.ERROR, name, "PERMISSION DENIED. '" + simulation.getActiveUserProfile().getName() + "' doesn't have " +
                        "the permission: '" + command + "'");
            }
        }catch(ModuleException e) {
            return false;
        }

        // User does indeed have the required permission
        return true;
    }

    /**
     * Returns this module's name.
     *
     * @return {@link String} The name of the module.
     */
    public String getName() {
        return name;
    }

}
