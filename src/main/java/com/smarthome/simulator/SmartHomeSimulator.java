package com.smarthome.simulator;

import com.smarthome.simulator.models.HouseLayout;
import com.smarthome.simulator.models.Simulation;
import com.smarthome.simulator.web.JavaBridge;
import com.smarthome.simulator.web.WebServer;
import com.sun.javafx.event.EventQueue;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class SmartHomeSimulator extends Application {

    private final WebServer server = new WebServer();

    private JavaBridge javaBridge;

    private final Simulation simulation = new Simulation();

    private Stage primaryStage;

    /**
     * {@inheritDoc}
     */
    public void start(Stage primaryStage) {

        // Save an instance to the primary stage
        this.primaryStage = primaryStage;

        // Start web server, serves the frontend at http://localhost:<WebServer.SERVER_PORT>/
        server.start();

        // Create web view
        WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        javaBridge = new JavaBridge(webEngine);

        // JavaFX set the scene as the webview
        Scene scene = new Scene(webView, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set javaBridge reference in the page's JavaScript
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED == newValue) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", javaBridge);
            }
        });

        // Print console messages from JavaScript
        WebConsoleListener.setDefaultListener((view, message, lineNumber, sourceId) -> System.out.println(message));

        // Load http://localhost:<WebServer.SERVER_PORT>/
        webEngine.load("http://localhost:" + WebServer.SERVER_PORT + "/");

        // Add listeners for UI functionality
        addListeners();

    }

    /**
     * Registers all listeners for events fired from the front-end.
     */
    private void addListeners() {

        // User clicks on "Load layout" button
        javaBridge.addEventListener("uploadHouseLayout", (event) -> {
            Platform.runLater(() -> {
                HouseLayout layout = HouseLayout.promptForLayout(primaryStage);
                if (layout != null) {
                    simulation.setHouseLayout(layout);
                    javaBridge.setStoreProperty("houseLayout", layout);
                    javaBridge.updateViews();
                }
            });
        });

        javaBridge.addEventListener("editContextParameters", (event) -> {

            //example: time
            String time = (String) event.getMember("time");
            System.out.println("time is: " + time);

            //TODO: date, in/outside temperature

        });

        javaBridge.addEventListener("toggleSimulation", (event) -> {
            System.out.println("Toggling Simulation");
            //TODO: toggle simulation on/off
        });

    }

}