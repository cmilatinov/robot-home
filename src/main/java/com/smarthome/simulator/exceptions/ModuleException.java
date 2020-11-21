package com.smarthome.simulator.exceptions;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.utils.Logger;

/**
 * The ModuleException class will display an error message if there is any error related to the modules.
 */
public class ModuleException extends RuntimeException {

    /**
     * Default Constructor
     */
    public ModuleException() {
        super("Error while loading module.");
    }

    /**
     * Parameterized constructor.
     *
     * @param msg Custom error message.
     */
    public ModuleException(String msg) {
        super(msg);
    }

    /**
     * Parameterized constructor.
     *
     * @param logLevel The logging level to use ({@link Logger#INFO}, {@link Logger#DEBUG}, {@link Logger#WARN}, {@link Logger#ERROR}).
     * @param tag The module involved with the log.
     * @param msg Custom error message.
     */
    public ModuleException(int logLevel, String tag, String msg) {
        SmartHomeSimulator.LOGGER.log(logLevel, tag, msg);
    }
}
