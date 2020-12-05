package com.smarthome.simulator.exceptions;

/**
 * The DateTimeFormatException class will display an error message if the format of the Date and Time is not properly defined
 */
public class DateTimeFormatException extends RuntimeException {

    /**
     * Default Constructor
     */
    public DateTimeFormatException() {
        super("Invalid date time format.");
    }

    /**
     * Parameterized constructor.
     *
     * @param msg Custom error message.
     */
    public DateTimeFormatException(String msg) {
        super(msg);
    }
}
