package com.smarthome.simulator;

import com.smarthome.simulator.web.JavaBridge;
import com.smarthome.simulator.web.WebServer;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class SmartHomeSimulator extends Application {

    private final WebServer server = new WebServer();

    private JavaBridge javaBridge;

    @Override
    public void start(Stage primaryStage) {
        server.start();

        WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        javaBridge = new JavaBridge(webEngine);

        Scene scene = new Scene(webView, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED == newValue) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", javaBridge);
            }
        });

        webEngine.load("http://localhost:" + WebServer.SERVER_PORT + "/");
        WebConsoleListener.setDefaultListener((view, message, lineNumber, sourceId) -> System.out.println(message));
    }

}