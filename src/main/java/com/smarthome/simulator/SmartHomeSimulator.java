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

import java.util.Arrays;

public class SmartHomeSimulator extends Application {

    private final WebServer server = new WebServer();

    private final JavaBridge javaBridge = new JavaBridge();

    @Override
    public void start(Stage primaryStage) {
        server.start();

        WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();

        Scene scene = new Scene(webView, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        javaBridge.addEventListener("login.click", (event) -> System.out.println(Arrays.toString(javaBridge.array)));

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED == newValue) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", javaBridge);
            }
        });

        webEngine.load("http://localhost:8080/");
        WebConsoleListener.setDefaultListener((view, message, lineNumber, sourceId) -> System.out.println(message));

    }

}