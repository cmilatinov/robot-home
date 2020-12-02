package com.smarthome.simulator.modules;

import com.smarthome.simulator.*;
import com.smarthome.simulator.models.HouseLayout;
import com.smarthome.simulator.models.Simulation;
import com.smarthome.simulator.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SHH extends Module{

    public static final String SET_ZONE = "setZone";

    public static final String SET_ZONE_TEMP = "setZoneTemperature";

    public static final String SET_ROOM_TEMPERATURE = "setRoomTemperature";

    /**
     * Creates a module with a given name and simulation reference.
     *
     * @param simulation The simulation instance to bind to the module.
     */
    public SHH(Simulation simulation) {
        super("SHH", simulation);
    }

    @Override
    public List<String> getCommandList() {
        return new ArrayList<String>() {{
            add(SET_ZONE);
            add(SET_ROOM_TEMPERATURE);
        }};
    }

    @Override
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {

        // If the command was sent by the user, check if the active user profile has the needed permission.
        if (sentByUser && !checkPermission(command))
            return;

        // Log command
        SmartHomeSimulator.LOGGER.log(Logger.INFO, getName(), "Executing command '" + command + "'");

        switch (command){
            case SET_ZONE:
                setZone(payload);
                break;
            case SET_ROOM_TEMPERATURE:
                setRoomTemperature(payload);
                break;
        }
    }

    private void setZone(Map<String, Object> payload){
        String id = (String) payload.get("id");
        int zoneId = Integer.parseInt(payload.get("zoneId").toString());

        // Check layout exists
        HouseLayout layout = this.simulation.getHouseLayout();
        if (layout == null)
            return;

        layout.getRooms()
                .stream()
                .filter(room -> room.getId().equals(id))
                .findFirst()
                .ifPresent(room -> room.setZoneId(zoneId));
    }

    private void setRoomTemperature(Map<String, Object> payload) {
        String room_id = (String) payload.get("id");
        Float temperature = Float.parseFloat(payload.get("temperature").toString());

        this.simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> room.getId().equals(room_id))
                .findFirst()
                .ifPresent(room -> {
                    room.setTemperature(temperature);
                    room.setTemperatureOverridden(true);
                });
    }
}
