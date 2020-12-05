package com.smarthome.simulator.exceptions;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.utils.Logger;

/**
 * The HouseLayoutException class will display an error message if there is any error related to the house layout
 */
public class HouseLayoutException extends RuntimeException {

    /**
     * Default Constructor
     */
    public HouseLayoutException() {
        super("Error while generating the house layout.");
    }

    /**
     * Parameterized constructor.
     *
     * @param msg Custom error message.
     */
    public HouseLayoutException(String msg) {
        super(msg);
    }

    /**
     * Parameterized constructor.
     *
     * @param logLevel The logging level to use ({@link Logger#INFO}, {@link Logger#DEBUG}, {@link Logger#WARN}, {@link Logger#ERROR}).
     * @param tag      The module involved with the log.
     * @param msg      Custom error message.
     */
    public HouseLayoutException(int logLevel, String tag, String msg) {
        super();
        SmartHomeSimulator.LOGGER.log(logLevel, tag, msg);
    }

}
