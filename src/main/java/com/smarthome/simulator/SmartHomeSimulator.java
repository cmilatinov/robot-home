package com.smarthome.simulator;

import com.smarthome.simulator.models.*;
import com.smarthome.simulator.models.Window;
import com.smarthome.simulator.modules.*;
import com.smarthome.simulator.web.JavaScriptQueryHandler;
import com.smarthome.simulator.web.WebServer;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefLoadHandler;
import org.cef.network.CefRequest;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    private static SHC shc;
    private static SHP shp;

    /**
     * Program entry point.
     *
     * @param args The command line arguments passed to the program.
     */
    public static void main(String[] args) {

        // Start web server
        server.start();

        // Use default web browser settings
        PandomiumSettings settings = PandomiumSettings.getDefaultSettingsBuilder().build();

        Pandomium pandomium = new Pandomium(settings);
        pandomium.initialize();

        PandomiumClient client = pandomium.createClient();
        PandomiumBrowser browser = client.loadURL("http://localhost:" + WebServer.SERVER_PORT);

        // Create JavaScript query handler
        handler = new JavaScriptQueryHandler(browser.getCefBrowser(), simulation);

        // Add query handler to browser
        CefMessageRouter.CefMessageRouterConfig config = new CefMessageRouter.CefMessageRouterConfig();
        config.jsQueryFunction = "cefQuery";
        config.jsCancelFunction = "cefQueryCancel";
        final CefMessageRouter messageRouter = CefMessageRouter.create(config);
        messageRouter.addHandler(handler, true);
        client.getCefClient().addMessageRouter(messageRouter);

        // Add load end handler to browser
        client.getCefClient().addLoadHandler(new CefLoadHandler() {
            public void onLoadingStateChange(CefBrowser cefBrowser, boolean b, boolean b1, boolean b2) {}
            public void onLoadStart(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest.TransitionType transitionType) {}
            public void onLoadError(CefBrowser cefBrowser, CefFrame cefFrame, ErrorCode errorCode, String s, String s1) {}

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add browser listeners
        addListeners();

    }

    /**
     * Registers all listeners for events fired from the front-end.
     */
    private static void addListeners() {

        // User clicks on upload layout button
        handler.addEventListener("uploadHouseLayout", (event) -> {
            SwingUtilities.invokeLater(() -> {
                HouseLayout layout = HouseLayout.promptForLayout(frame);
                if (layout != null) {
                    simulation.setHouseLayout(layout);
                    handler.updateViews();
                }
            });
        });

        // User toggles simulation state
        handler.addEventListener("toggleSimulation", (event) -> {

            // Get new value
            boolean value = (boolean) event.get("value");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("value", value);

            // Set simulation running
            simulation.setRunning(value);

            // Update front-end
            handler.updateViews();

        });

        // User adds a new profile
        handler.addEventListener("addProfile", (event) -> {

            // Get name
            String name = (String) event.get("name");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("name", name);

            // Add new profile if name not empty
            if (name.length() > 0) {
                try {
                    simulation.getUserProfiles()
                            .add(new UserProfile(name));
                } catch (Exception ignored) {}
            }

            // Update front-end
            handler.updateViews();

        });

        // User edits a profile
        handler.addEventListener("editProfile", (event) -> {

            // Get attributes
            String id = (String) event.get("id");
            String name = (String) event.get("name");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);
            eventMap.put("name", name);

            // Update profile if exists and name not empty
            if (name.length() > 0)
                simulation.getUserProfiles()
                        .stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .ifPresent(p -> {
                            try {
                                p.setName(name);
                            } catch (Exception ignored) {
                            }
                        });

            // Update front-end
            handler.updateViews();

        });

        // User deletes a profile
        handler.addEventListener("deleteProfile", (event) -> {

            // Get id
            String id = (String) event.get("id");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);

            // Never delete last profile
            if (simulation.getUserProfiles().size() <= 1)
                return;

            // Remove profile if exists
            simulation.getUserProfiles()
                    .removeIf(p -> p.getId().equals(id));

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

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);

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

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);
            eventMap.put("x", x);
            eventMap.put("y", y);
            eventMap.put("width", width);
            eventMap.put("height", height);

            // Check layout exists
            HouseLayout layout = simulation.getHouseLayout();
            if (layout == null)
                return;

            // Set room dimensions if present
            layout.getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .ifPresent(room -> room.setDimensions(new RoomDimensions((float) x, (float) y, width, height)));

            // Update front-end
            handler.updateViews();

        });

        // User changes the date or time of the simulation
        handler.addEventListener("setSimulationDateTime", (event) -> {

            // Get date string
            String value = (String) event.get("value");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("value", value);

            // Set date time
            try {
                simulation.setDateTime(value);
            } catch (Exception ignored) {
            }

            // Update front-end
            handler.updateViews();

        });

        // User changes the outside temperature
        handler.addEventListener("setOutsideTemp", (event) -> {

            // Get value
            float value = Float.parseFloat(event.get("value").toString());

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("value", value);

            // Set outside temperature
            simulation.setTemperatureOutside(value);

            // Update front-end
            handler.updateViews();

        });

        // User changes the inside temperature
        handler.addEventListener("setInsideTemp", (event) -> {

            // Get value
            float value = Float.parseFloat(event.get("value").toString());

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("value", value);

            // Set outside temperature
            simulation.setTemperatureInside(value);

            // Update front-end
            handler.updateViews();

        });

        // User toggles a light
        handler.addEventListener("toggleLightState", (event) -> {

            // Get payload
            String id = (String) event.get("id");
            boolean on = (boolean) event.get("on");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);
            eventMap.put("on", on);

            // Checks if it's a "Remote" command or not
            String currentUserRoomId = simulation.getUserLocation();
            simulation.getHouseLayout().getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(currentUserRoomId))
                    .findFirst()
                    .ifPresent(r -> {
                        Optional<Light> light = r.getLights().stream().filter(l -> l.getId().equals(id))
                                .findFirst();
                        if (light.isPresent()) {
                            shc.executeCommand("ControlLights", eventMap, true);
                        } else {
                            shc.executeCommand("RemoteControlLights", eventMap, true);
                        }
                    });

            // Update front-end
            handler.updateViews();

        });

        // User toggles a door
        handler.addEventListener("toggleDoorState", (event) -> {

            // Get payload
            String id = (String) event.get("id");
            boolean locked = (boolean) event.get("locked");

            // Door cannot be open if locked
            boolean open = !locked && (boolean) event.get("open");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);
            eventMap.put("locked", locked);
            eventMap.put("open", open);

            // Checks if it's a "Remote" command or not
            String currentUserRoomId = simulation.getUserLocation();
            simulation.getHouseLayout().getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(currentUserRoomId))
                    .findFirst()
                    .ifPresent(r -> {
                        Optional<Door> door = r.getDoors().stream().filter(d -> d.getId().equals(id))
                                .findFirst();
                        if (door.isPresent()) {
                            shc.executeCommand("ControlDoors", eventMap, true);
                        } else {
                            shc.executeCommand("RemoteControlDoors", eventMap, true);
                        }
                    });

            // Update front-end
            handler.updateViews();

        });

        // User toggles a window
        handler.addEventListener("toggleWindowState", (event) -> {

            // Get payload
            String id = (String) event.get("id");
            boolean blocked = (boolean) event.get("blocked");
            boolean open = (boolean) event.get("open");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);
            eventMap.put("blocked", blocked);
            eventMap.put("open", open);

            // Checks if it's a "Remote" command or not
            String currentUserRoomId = simulation.getUserLocation();
            simulation.getHouseLayout().getRooms()
                    .stream()
                    .filter(r -> r.getId().equals(currentUserRoomId))
                    .findFirst()
                    .ifPresent(r -> {
                        Optional<Window> window = r.getWindows().stream().filter(w -> w.getId().equals(id))
                                .findFirst();
                        if (window.isPresent()) {
                            shc.executeCommand("ControlWindows", eventMap, true);
                        } else {
                            shc.executeCommand("RemoteControlWindows", eventMap, true);
                        }
                    });

            // Update front-end
            handler.updateViews();

        });

        // User sets their location in the house
        handler.addEventListener("setUserLocation", (event) -> {

            // Get location
            String userLocation = (String) event.get("userLocation");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("userLocation", userLocation);

            String currentUserRoomId = simulation.getUserLocation();

            // Set the user's location in the house
            simulation.setUserLocation(userLocation);

            // this helps to implement to "auto mode"
            updateRooms(currentUserRoomId, userLocation);

            // Update front-end
            handler.updateViews();

        });

        // User adds a new person to the simulation
        handler.addEventListener("addPerson", (event) -> {

            // Get payload
            String name = (String) event.get("name");
            String roomId = (String) event.get("roomId");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("name", name);
            eventMap.put("roomId", roomId);



            // Add person to simulation
            simulation.getPeople()
                    .add(new Person(name, roomId));

            // this helps to implement to "auto mode"
            updateRooms(null, roomId);

            // Update front-end
            handler.updateViews();

        });

        // User moves a person to a new location
        handler.addEventListener("movePerson", (event) -> {

            // Get payload
            String id = (String) event.get("id");
            String roomId = (String) event.get("roomId");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);
            eventMap.put("roomId", roomId);

            AtomicReference<String> currentUserRoomId = null;

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
            updateRooms(currentUserRoomId.get(), roomId);

            // Update front-end
            handler.updateViews();

        });

        // User removes a person from the simulation
        handler.addEventListener("deletePerson", (event) -> {

            // Get payload
            String id = (String) event.get("id");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("id", id);

            simulation.getPeople()
                    .stream()
                    .filter(person -> person.getId().equals(id))
                    .findFirst()
                    .ifPresent(p -> {
                        simulation.getPeople().remove(p);
                        updateRooms(p.getRoomId(), null);
                    });

            // Update front-end
            handler.updateViews();

        });

        //User sets lights to stay on during away mode
        handler.addEventListener("setAwayLights", (event) -> {
            // Get payload

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();

            // Get module and pass command
            Module module = simulation.getModule("SetAwayLights");
            if (module != null)
                module.executeCommand("SetAwayLights", eventMap, true);

            // Update front-end
            handler.updateViews();
        });

        //User enable the "auto mode"
        handler.addEventListener("toggleAutoMode", (event) -> {
            // Get payload
            boolean on = (Boolean) event.get("on");

            // Create Argument Map for module command execution
            HashMap eventMap = new HashMap<String, Object>();
            eventMap.put("on", on);

            shc.executeCommand(SHC.P_CONTROL_AUTO_MODE, eventMap, true);

            // Update front-end
            handler.updateViews();
        });

    }

    private static void updateRooms(String oldRoom, String newRoom) {
        // for the room that we are leaving
        simulation.getHouseLayout().getRooms()
                .stream()
                .filter(room -> room.getId().equals(oldRoom) || room.getId().equals(newRoom))
                .forEach(room -> shc.updateRoom(room));
    }



}