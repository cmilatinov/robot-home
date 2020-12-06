package com.smarthome.simulator;

import com.smarthome.simulator.exceptions.ModuleException;
import com.smarthome.simulator.exceptions.UserProfileException;
import com.smarthome.simulator.models.Window;
import com.smarthome.simulator.models.*;
import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHH;
import com.smarthome.simulator.modules.SHP;
import com.smarthome.simulator.utils.EventUtil;
import com.smarthome.simulator.utils.Logger;
import com.smarthome.simulator.utils.TimeUtil;
import com.smarthome.simulator.web.JavaScriptQueryHandler;
import com.smarthome.simulator.web.WebServer;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefLoadHandler;
import org.cef.network.CefRequest;
import org.json.simple.JSONArray;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Main program class.
 */
public class SmartHomeSimulator {

    /**
     * JavaScript handler used as a bridge between Java and the browser's JavaScript.
     */
    private static JavaScriptQueryHandler handler;

    /**
     * The JFrame of the Swing application.
     */
    private static JFrame frame;

    /**
     * The global simulation instance of the application.
     */
    private static final Simulation simulation = new Simulation();

    /**
     * The web server serving the front-end files.
     */
    private static final WebServer server = new WebServer();

    /**
     * Logger for the simulator.
     */
    public static Logger LOGGER;

    /**
     * Program entry point.
     *
     * @param args The command line arguments passed to the program.
     */
    public static void main(String[] args) {

        // Load simulation modules
        try {
            simulation.registerModule(SHC.class);
            simulation.registerModule(SHP.class);
            simulation.registerModule(SHH.class);
        } catch (ModuleException ignored) {
        }

        // Start web server
        server.start();

        // Load user profiles if they exist
        File userProfileFile = new File(UserProfile.USER_PROFILE_FILEPATH);
        if (userProfileFile.exists()) {
            List<UserProfile> profiles = null;
            try {
                profiles = UserProfile.loadUserProfiles(userProfileFile);
            } catch (UserProfileException ignored) {
            }
            if (profiles != null) {
                simulation.setUserProfiles(profiles);
                simulation.setActiveUserProfile(profiles.get(0));
            }
        }

        // Use default web browser settings
        PandomiumSettings settings = PandomiumSettings.getDefaultSettingsBuilder().build();

        Pandomium pandomium = new Pandomium(settings);
        pandomium.initialize();

        PandomiumClient client = pandomium.createClient();
        PandomiumBrowser browser = client.loadURL("http://localhost:" + WebServer.SERVER_PORT);

        // Create JavaScript query handler and logger
        handler = new JavaScriptQueryHandler(browser.getCefBrowser(), simulation);
        LOGGER = new Logger(handler);

        // Add query handler to browser
        CefMessageRouter.CefMessageRouterConfig config = new CefMessageRouter.CefMessageRouterConfig();
        config.jsQueryFunction = "cefQuery";
        config.jsCancelFunction = "cefQueryCancel";
        final CefMessageRouter messageRouter = CefMessageRouter.create(config);
        messageRouter.addHandler(handler, true);
        client.getCefClient().addMessageRouter(messageRouter);

        // Add load end handler to browser
        client.getCefClient().addLoadHandler(new CefLoadHandler() {
            public void onLoadingStateChange(CefBrowser cefBrowser, boolean b, boolean b1, boolean b2) {
            }

            public void onLoadStart(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest.TransitionType transitionType) {
            }

            public void onLoadError(CefBrowser cefBrowser, CefFrame cefFrame, ErrorCode errorCode, String s, String s1) {
            }

            public void onLoadEnd(CefBrowser cefBrowser, CefFrame cefFrame, int i) {
                handler.updateViews();
            }
        });

        // Create and display JFrame with browser
        frame = new JFrame();
        frame.getContentPane().add(browser.toAWTComponent(), BorderLayout.CENTER);

        // Close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                server.halt();
            }
        });

        // Size and title
        frame.setTitle("Smart Home Simulator");
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // JFileChooser look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Add browser listeners
        addListeners();
    }

    /**
     * Registers all listeners for events fired from the front-end.
     */
    @SuppressWarnings("unchecked")
    private static void addListeners() {

        // User clicks on upload layout button
        handler.addEventListener("uploadHouseLayout", (event) -> SwingUtilities.invokeLater(() -> {
            HouseLayout layout = HouseLayout.promptForLayout(frame);
            if (layout != null) {
                simulation.setHouseLayout(layout);
                try {
                    simulation.executeCommand(SHH.SET_DEFAULT_ZONE, null, false);
                } catch (ModuleException ignored) {
                }
                handler.updateViews();
            }
        }));

        // User toggles simulation state
        handler.addEventListener("toggleSimulation", (event) -> {

            // Get new value
            boolean value = (boolean) event.get("value");

            // Set simulation running
            simulation.setRunning(value);

            // Update front-end
            handler.updateViews();

        });

        // User adds a new profile
        handler.addEventListener("addProfile", (event) -> {

            // Get name
            String name = (String) event.get("name");

            // Add new profile if name not empty
            if (name.length() > 0) {
                try {
                    simulation.getUserProfiles()
                            .add(new UserProfile(name));
                    // Save changes
                    UserProfile.writeUserProfiles(new File(UserProfile.USER_PROFILE_FILEPATH), simulation.getUserProfiles());
                } catch (UserProfileException ignored) {
                }
            }

            // Update front-end
            handler.updateViews();

        });

        // User edits a profile
        handler.addEventListener("editProfile", (event) -> {

            // Get attributes
            String id = (String) event.get("id");
            String name = (String) event.get("name");
            List<String> permissions = (List<String>) ((JSONArray) event.get("permissions"))
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());

            // Update profile if exists and name not empty
            if (name.length() > 0)
                simulation.getUserProfiles()
                        .stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .ifPresent(p -> {
                            try {
                                p.setName(name);
                                p.setPermissions(permissions);
                                UserProfile.writeUserProfiles(new File(UserProfile.USER_PROFILE_FILEPATH), simulation.getUserProfiles());
                            } catch (UserProfileException ignored) {
                            }
                        });

            // Update front-end
            handler.updateViews();

        });

        // User deletes a profile
        handler.addEventListener("deleteProfile", (event) -> {

            // Get id
            String id = (String) event.get("id");

            // Never delete last profile
            if (simulation.getUserProfiles().size() <= 1)
                return;

            // Remove profile if exists
            simulation.getUserProfiles()
                    .removeIf(p -> p.getId().equals(id));

            // Save changes
            try {
                UserProfile.writeUserProfiles(new File(UserProfile.USER_PROFILE_FILEPATH), simulation.getUserProfiles());
            } catch (UserProfileException ignored) {
            }

            // Set active user profile to first if it is being deleted
            if (simulation.getActiveUserProfile().getId().equals(id))
                simulation.setActiveUserProfile(simulation.getUserProfiles().get(0));

            // Update front-end
            handler.updateViews();

        });

        // User chooses active profile
        handler.addEventListener("setActiveProfile", (event) -> {

            // Get id
            String id = (String) event.get("id");

            // Set active profile if present
            simulation.getUserProfiles()
                    .stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .ifPresent(simulation::setActiveUserProfile);

            // Update front-end
            handler.updateViews();

        });

        // User resizes a room
        handler.addEventListener("setRoomDimensions", (event) -> {

            // Get id
            String id = (String) event.get("id");

            // Parse dimensions
            float x = Float.parseFloat(event.get("x").toString());
            float y = Float.parseFloat(event.get("y").toString());
            float width = Float.parseFloat(event.get("width").toString());
            float height = Float.parseFloat(event.get("height").toString());

            // Check layout exists
            HouseLayout layout = simulation.getHouseLayout();
            if (layout == null)
                return;

            // Set room dimensions if present
            layout.getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .ifPresent(room -> room.setDimensions(new RoomDimensions(x, y, width, height)));

            // Update front-end
            handler.updateViews();

        });

        // User changes the date or time of the simulation
        handler.addEventListener("setSimulationDateTime", (event) -> {

            // Get date string
            String value = (String) event.get("value");

            // Old time before set
            LocalTime before = simulation.getTime();

            // Set date time
            simulation.setDateTime(value);

            // New time after set
            LocalTime after = simulation.getTime();

            // If the simulation is in away mode and we've just entered the away light range, set the correct away lights on.
            if (simulation.isAway()) {
                boolean beforeInRange = TimeUtil.isInRange(before, simulation.getAwayTimeStart(), simulation.getAwayTimeEnd());
                boolean afterInRange = TimeUtil.isInRange(after, simulation.getAwayTimeStart(), simulation.getAwayTimeEnd());

                try {
                    // Entering the time range for lights to stay on
                    if (!beforeInRange && afterInRange)
                        simulation.executeCommand(SHP.TOGGLE_AWAY_LIGHTS, null, false);

                        // Leaving the time range for lights to stay on, so close all lights
                    else if (beforeInRange && !afterInRange)
                        simulation.executeCommand(SHC.CLOSE_ALL_LIGHTS, null, false);
                } catch (ModuleException ignored) {
                }

            }

            // Update front-end
            handler.updateViews();

        });

        // User changes the outside temperature
        handler.addEventListener("setOutsideTemp", (event) -> {

            // Get value
            float value = Float.parseFloat(event.get("value").toString());

            // Set outside temperature
            simulation.setTemperatureOutside(value);

            // Update front-end
            handler.updateViews();

        });

        // User toggles a light
        handler.addEventListener("toggleLightState", event -> {

            // Get payload
            String id = (String) event.get("id");

            // Find the room the user is in
            Optional<Room> userRoom = simulation.getHouseLayout().getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(simulation.getUserLocation()))
                    .findFirst();

            try {
                // The user is in a room
                if (userRoom.isPresent()) {

                    // Get the room
                    Room room = userRoom.get();

                    // Check if the window is in the room
                    Optional<Light> light = room.getLights()
                            .stream()
                            .filter(l -> l.getId().equals(id))
                            .findFirst();

                    // The light is in the same room as the user

                    if (light.isPresent())
                        simulation.executeCommand(SHC.CONTROL_LIGHT, EventUtil.convertToMap(event), true);

                        // The light is another room
                    else
                        simulation.executeCommand(SHC.REMOTE_CONTROL_LIGHT, EventUtil.convertToMap(event), true);

                    // User is outside of the house
                } else
                    simulation.executeCommand(SHC.REMOTE_CONTROL_LIGHT, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User toggles a door
        handler.addEventListener("toggleDoorState", (event) -> {

            // Get payload
            String id = (String) event.get("id");

            // Find the room the user is in
            Optional<Room> userRoom = simulation.getHouseLayout().getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(simulation.getUserLocation()))
                    .findFirst();
            try {
                // The user is in a room
                if (userRoom.isPresent()) {

                    // Get the room
                    Room room = userRoom.get();

                    // Check if the door is in the room
                    Optional<Door> door = room.getDoors()
                            .stream()
                            .filter(d -> d.getId().equals(id))
                            .findFirst();

                    // The door is in the same room as the user
                    if (door.isPresent())
                        simulation.executeCommand(SHC.CONTROL_DOOR, EventUtil.convertToMap(event), true);

                        // The door is another room
                    else
                        simulation.executeCommand(SHC.REMOTE_CONTROL_DOOR, EventUtil.convertToMap(event), true);

                    // User is outside of the house
                } else
                    simulation.executeCommand(SHC.REMOTE_CONTROL_DOOR, EventUtil.convertToMap(event), true);

                // If simulation is away then intruder detected, so alert user
                if (simulation.isAway())
                    simulation.executeCommand(SHP.ALERT_USER, null, false);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User toggles a window
        handler.addEventListener("toggleWindowState", (event) -> {

            // Get payload
            String id = (String) event.get("id");

            // Checks if it's a "Remote" command or not
            String currentUserRoomId = simulation.getUserLocation();
            Optional<Room> userRoom = simulation.getHouseLayout().getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(currentUserRoomId))
                    .findFirst();

            try {
                // The user is in a room
                if (userRoom.isPresent()) {

                    // Get the room
                    Room room = userRoom.get();

                    // Check if the window is in the room
                    Optional<Window> window = room.getWindows()
                            .stream()
                            .filter(w -> w.getId().equals(id))
                            .findFirst();

                    // The window is in the same room as the user
                    if (window.isPresent())
                        simulation.executeCommand(SHC.CONTROL_WINDOW, EventUtil.convertToMap(event), true);

                        // The window is another room
                    else
                        simulation.executeCommand(SHC.REMOTE_CONTROL_WINDOW, EventUtil.convertToMap(event), true);

                    // User is outside of the house
                } else
                    simulation.executeCommand(SHC.REMOTE_CONTROL_WINDOW, EventUtil.convertToMap(event), true);

                // If simulation is away then intruder detected, so alert user
                if (simulation.isAway())
                    simulation.executeCommand(SHP.ALERT_USER, null, false);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User sets their location in the house
        handler.addEventListener("setUserLocation", (event) -> {

            // Get location
            String userLocation = (String) event.get("userLocation");

            // User's current location
            String currentUserRoomId = simulation.getUserLocation();

            // Set the user's location in the house
            simulation.setUserLocation(userLocation);

            // Update the room lights where the user moved from and to
            updateRoomLights(currentUserRoomId, userLocation);

            // Update front-end
            handler.updateViews();

        });

        // User adds a new person to the simulation
        handler.addEventListener("addPerson", (event) -> {

            // Get payload
            String name = (String) event.get("name");
            String roomId = (String) event.get("roomId");

            // Add person to simulation
            simulation.getPeople()
                    .add(new Person(name, roomId));

            // Update the room lights where the user added to
            updateRoomLights(roomId);

            // If simulation is away then intruder detected, so alert user
            try {
                if (simulation.isAway())
                    simulation.executeCommand(SHP.ALERT_USER, null, false);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User moves a person to a new location
        handler.addEventListener("movePerson", (event) -> {

            // Get payload
            String id = (String) event.get("id");
            String roomId = (String) event.get("roomId");

            // Current room id of the user
            AtomicReference<String> currentUserRoomId = new AtomicReference<>();

            // Move person if exists
            simulation.getPeople()
                    .stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .ifPresent(p -> {
                        currentUserRoomId.set(p.getRoomId());
                        p.setRoomId(roomId);
                    });

            // this helps to implement to "auto mode"
            updateRoomLights(currentUserRoomId.get(), roomId);

            try {
                // If simulation is away then intruder detected, so alert user
                if (simulation.isAway())
                    simulation.executeCommand(SHP.ALERT_USER, null, false);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User removes a person from the simulation
        handler.addEventListener("deletePerson", (event) -> {

            // Get payload
            String id = (String) event.get("id");

            // Remove person from simulation and update the room they were in
            simulation.getPeople()
                    .stream()
                    .filter(person -> person.getId().equals(id))
                    .findFirst()
                    .ifPresent(p -> {
                        simulation.getPeople().remove(p);
                        updateRoomLights(p.getRoomId());
                    });

            // Update front-end
            handler.updateViews();

        });

        // User sets lights to stay on during away mode
        handler.addEventListener("setAwayLights", (event) -> {

            try {
                // Execute the set away lights command
                simulation.executeCommand(SHP.SET_AWAY_LIGHTS, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // Set the time delay before alerting authorities when motion is detected during away mode
        handler.addEventListener("setAlertDelay", (event) -> {

            try {
                // Pass command
                simulation.executeCommand(SHP.SET_ALERT_DELAY, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // Set the time window for selected lights to remain on during away mode
        handler.addEventListener("setAwayTime", (event) -> {

            try {
                // Pass command
                simulation.executeCommand(SHP.SET_AWAY_TIME, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User toggles away mode
        handler.addEventListener("toggleAway", (event) -> {

            try {
                // Execute the set away mode command
                simulation.executeCommand(SHP.SET_AWAY_MODE, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User toggles the "auto mode"
        handler.addEventListener("toggleAutoMode", (event) -> {

            // Update all room lights before turning auto mode off
            if (!(boolean) event.get("value"))
                updateAllRoomLights();

            try {
                // Execute the set auto mode command
                simulation.executeCommand(SHC.SET_AUTO_MODE, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update all room lights
            if ((boolean) event.get("value"))
                updateAllRoomLights();

            // Update front-end
            handler.updateViews();

        });

        // User sets the simulation speed
        handler.addEventListener("setSimulationSpeed", event -> {

            // Get payload
            float value = Float.parseFloat(event.get("value").toString());

            // Set simulation speed
            simulation.setSimulationSpeed(value);

            // Update front-end
            handler.updateViews();

        });

        // User sets temperature of a specific room
        handler.addEventListener("setRoomTemperature", (event) -> {

            try {
                // Execute the set room temperature command
                simulation.executeCommand(SHH.SET_ROOM_TEMPERATURE, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();
        });

        // User sets if room should be overriding zone temperature
        handler.addEventListener("setRoomOverride", (event) -> {

            try {
                // Execute the set room override command
                simulation.executeCommand(SHH.SET_ROOM_OVERRIDE, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();
        });

        // User edits a specific zone
        handler.addEventListener("editZone", (event) -> {

            try {
                // Execute the edit zone command
                simulation.executeCommand(SHH.EDIT_ZONE, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();
        });

        // User adds a new zone to the simulation
        handler.addEventListener("addZone", (event) -> {

            try {
                // Execute the add zone command
                simulation.executeCommand(SHH.ADD_ZONE, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User removes a zone
        handler.addEventListener("removeZone", (event) -> {

            try {
                // Execute the remove zone command
                simulation.executeCommand(SHH.REMOVE_ZONE, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User sets the winter and summer dates
        handler.addEventListener("setWinterRange", (event) -> {

            try {
                // Set the range of winter
                simulation.executeCommand(SHH.SET_WINTER_RANGE, EventUtil.convertToMap(event), false);
            } catch (ModuleException ignored) {
            }

            //Update front end
            handler.updateViews();
        });

        handler.addEventListener("setSeasonTemp", (event) -> {

            try {
                //Set the desired winter and summer temperature when the house is in away mode
                simulation.executeCommand(SHH.SET_SEASON_TEMP, EventUtil.convertToMap(event), true);
            } catch (ModuleException ignored) {
            }

            // Update front end
            handler.updateViews();
        });

    }

    /**
     * This function updates all lights for a specific room from the SHC
     *
     * @param rooms the room that needs its lights updated
     */
    private static void updateRoomLights(String... rooms) {
        // Update the given rooms with the given IDs
        simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> Arrays.stream(rooms).anyMatch(rid -> room.getId().equals(rid)))
                .forEach(room -> {
                    HashMap<String, Object> eventMap = new HashMap<>();
                    eventMap.put("room", room);
                    try {
                        simulation.executeCommand(SHC.UPDATE_ROOM_LIGHTS, eventMap, false);
                    } catch (ModuleException ignored) {
                    }
                });
    }

    /**
     * This function updates all the room lights' states from the SHC
     */
    private static void updateAllRoomLights() {
        simulation.getHouseLayout().getRooms()
                .forEach(room -> {
                    HashMap<String, Object> eventMap = new HashMap<>();
                    eventMap.put("room", room);
                    try {
                        simulation.executeCommand(SHC.UPDATE_ROOM_LIGHTS, eventMap, false);
                    } catch (ModuleException ignored) {
                    }
                });
    }

    /**
     * This function updates the front end view for all the room temperatures.
     *
     * @param hvac The HVAC instance taking care of the update.
     */
    public static void updateRoomTempView(HVAC hvac) {
        handler.updateRoomTemps(hvac);
    }


}