package com.smarthome.simulator.utils;

import java.time.LocalTime;

/**
 * Tool for time manipulations.
 */
public class TimeUtil {

    /**
     * Function verifies the range in which the time input is in.
     *
     * @param time  {@link LocalTime} representing the current time.
     * @param start {@link LocalTime} representing the start time.
     * @param end   {@link LocalTime} representing the end time.
     * @return boolean verifying if the time input is in the range of the current time.
     */
    public static boolean isInRange(LocalTime time, LocalTime start, LocalTime end) {
        if (start.isBefore(end))
            return time.isAfter(start) && time.isBefore(end);
        return !(time.isAfter(end) && time.isBefore(start));
    }

}
