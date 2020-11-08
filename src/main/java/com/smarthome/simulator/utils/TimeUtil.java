package com.smarthome.simulator.utils;

import java.time.LocalTime;

public class TimeUtil {

    public static boolean isInRange(LocalTime time, LocalTime start, LocalTime end) {
        if (start.isBefore(end))
            return time.isAfter(start) && time.isBefore(end);
        return !(time.isAfter(end) && time.isBefore(start));
    }

}
