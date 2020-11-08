package com.smarthome.simulator.utils;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to convert event objects into maps.
 */
public class EventUtil {

    /**
     * Converts a {@link JSONObject} event into a {@link Map}.
     *
     * @param event The event to convert.
     * @return {@link Map} The resulting map.
     */
    public static Map<String, Object> convertToMap(JSONObject event) {
        Map<String, Object> eventMap = new HashMap<>();
        for (Object o : event.keySet()) {
            eventMap.put(o.toString(), event.get(o));
        }
        return eventMap;
    }

}
