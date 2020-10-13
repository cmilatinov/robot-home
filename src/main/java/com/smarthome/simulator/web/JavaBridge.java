package com.smarthome.simulator.web;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.smarthome.simulator.models.HouseLayout;

public class JavaBridge {

    private static final Map<String, Consumer<JSObject>> listeners = new HashMap<>();

    private final WebEngine engine;

    public JavaBridge(WebEngine engine) {
        this.engine = engine;
    }

    /**
     * Registers an event listener executed when an event with the specified type is caught by the Java part of the app.
     *
     * @param event    The name of the event to listen for. (Ex. "simulation.start")
     * @param listener The method executed whenever the event is fired by the frontend.
     */
    public void addEventListener(String event, Consumer<JSObject> listener) {
        listeners.put(event, listener);
    }

    /**
     * This function is called by the frontend code with the correct parameters. Do NOT call this function from Java code.
     * This function will trigger the corresponding listener for the event if it exists.
     *
     * @param event   The name of the event to trigger.
     * @param payload A JavaScript object containing the payload of the event. Can be null.
     */
    public void dispatchEvent(String event, JSObject payload) {
        Consumer<JSObject> listener = listeners.get(event);
        if (listener != null)
            listener.accept(payload);
    }

    /**
     * Updates the frontend component views to properly display any changes made to object instances from the Java side.
     */
    public void updateViews() {
        engine.executeScript("window.vm.$store.commit('update');");
    }

    /**
     * Sets a Vuex Store property to the corresponding Java object.
     * @param prop The property name (ex: "houseLayout").
     * @param obj The object instance (ex: a {@link HouseLayout} instance).
     */
    public void setStoreProperty(String prop, Object obj) {
        JSObject storeState = (JSObject) engine.executeScript("window.vm.$store.state");
        storeState.setMember(prop, obj);
    }

}
