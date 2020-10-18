package com.smarthome.simulator;

import com.smarthome.simulator.models.*;
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
import java.util.ArrayList;

public class SmartHomeSimulator {

    private static JavaScriptQueryHandler handler;
    private static JFrame frame;

    private static final Simulation simulation = new Simulation();
    private static final WebServer server = new WebServer();

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

        // Add load handler to browser
        client.getCefClient().addLoadHandler(new CefLoadHandler() {
            public void onLoadingStateChange(CefBrowser cefBrowser, boolean b, boolean b1, boolean b2) {

            }

            public void onLoadStart(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest.TransitionType transitionType) {

            }

            public void onLoadEnd(CefBrowser cefBrowser, CefFrame cefFrame, int i) {
                handler.updateViews();
            }

            public void onLoadError(CefBrowser cefBrowser, CefFrame cefFrame, ErrorCode errorCode, String s, String s1) {

            }
        });

        // Create and display JFrame with browser
        frame = new JFrame();
        frame.getContentPane().add(browser.toAWTComponent(), BorderLayout.CENTER);

        // Close operation
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
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
                }catch (Exception e){}
            }

            // Update front-end
            handler.updateViews();

        });

        // User edits a profile
        handler.addEventListener("editProfile", (event) -> {

            // Get attributes
            String id = (String) event.get("id");
            String name = (String) event.get("name");

            // Update profile if exists and name not empty
            if (name.length() > 0)
                simulation.getUserProfiles()
                    .stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .ifPresent(p -> {
                        try {
                            p.setName(name);
                        }catch(Exception e){}
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
                    .ifPresent(room -> room.setDimensions(new RoomDimensions((float)x, (float)y, width, height)));

            // Update front-end
            handler.updateViews();

        });

        // User changes the date or time of the simulation
        handler.addEventListener("setSimulationDateTime", (event) -> {

            // Get date string
            String value = (String) event.get("value");

            System.out.println(value);
            // Set date time
            simulation.setDateTime(value);

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

        // User changes the inside temperature
        handler.addEventListener("setInsideTemp", (event) -> {

            // Get value
            float value = Float.parseFloat(event.get("value").toString());

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

            // Change light state if exists
            simulation.getHouseLayout()
                    .getRooms()
                    .stream()
                    .map(Room::getLights)
                    .reduce(new ArrayList<>(), (list, roomLights) -> {
                        list.addAll(roomLights);
                        return list;
                    })
                    .stream()
                    .filter(l -> l.getId().equals(id))
                    .findFirst()
                    .ifPresent(l -> l.setOn(on));

            // Update front-end
            handler.updateViews();

        });

        // User toggles a door
        handler.addEventListener("toggleDoorState", (event) -> {

            // Get payload
            String id = (String) event.get("id");
            boolean locked = (boolean) event.get("locked");
            boolean open = !locked && (boolean) event.get("open");

            // Change door state if exists
            simulation.getHouseLayout()
                    .getRooms()
                    .stream()
                    .map(Room::getDoors)
                    .reduce(new ArrayList<>(), (list, roomDoors) -> {
                        list.addAll(roomDoors);
                        return list;
                    })
                    .stream()
                    .filter(d -> d.getId().equals(id))
                    .findFirst()
                    .ifPresent(d -> {
                        d.setOpen(open);
                        d.setLocked(locked);
                    });

            // Update front-end
            handler.updateViews();

        });

        // User toggles a window
        handler.addEventListener("toggleWindowState", (event) -> {

            // Get payload
            String id = (String) event.get("id");
            boolean blocked = (boolean) event.get("blocked");
            boolean open = !blocked && (boolean) event.get("open");

            // Change window state if exists
            simulation.getHouseLayout()
                    .getRooms()
                    .stream()
                    .map(Room::getWindows)
                    .reduce(new ArrayList<>(), (list, roomWindows) -> {
                        list.addAll(roomWindows);
                        return list;
                    })
                    .stream()
                    .filter(w -> w.getId().equals(id))
                    .findFirst()
                    .ifPresent(w -> {
                        w.setOpen(open);
                        w.setBlocked(blocked);
                    });

            // Update front-end
            handler.updateViews();

        });

    }

}