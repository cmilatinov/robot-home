package com.smarthome.simulator.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.simulator.models.Simulation;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class allows Java code to interact with the JavaScript in the browser.
 */
public class JavaScriptQueryHandler implements CefMessageRouterHandler {

    // JSON parser and mapper
    private final ObjectMapper mapper = new ObjectMapper();
    private final JSONParser parser = new JSONParser();

    // Browser and simulation instances
    private final CefBrowser browser;
    private final Simulation simulation;

    // Listener list
    private final Map<String, Consumer<JSONObject>> listeners = new HashMap<>();

    /**
     * Creates a JavaScript
     *
     * @param browser    The {@link CefBrowser} instance used to access the web page.
     * @param simulation The {@link Simulation} instance to synchronize with the web page.
     */
    public JavaScriptQueryHandler(CefBrowser browser, Simulation simulation) {
        this.browser = browser;
        this.simulation = simulation;
    }

    /**
     * {@inheritDoc}
     */
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryID, String request, boolean persistent, CefQueryCallback callback) {

        System.out.println("Query " + queryID + ": " + request);

        int eventIndex = request.indexOf(",");
        String event = request.substring(0, eventIndex);

        String strPayload = request.substring(eventIndex + 1);
        try {

            JSONObject jsonObj = (JSONObject) parser.parse(strPayload);

            Consumer<JSONObject> listener = listeners.get(event);
            if (listener != null)
                listener.accept(jsonObj);

            callback.success("");
            return true;

        } catch (ParseException e) {
            callback.failure(1, "Invalid payload");
            return false;
        }

    }

    /**
     * {@inheritDoc}
     */
    public void onQueryCanceled(CefBrowser cefBrowser, CefFrame cefFrame, long l) {

    }

    /**
     * {@inheritDoc}
     */
    public void setNativeRef(String s, long l) {

    }

    /**
     * {@inheritDoc}
     */
    public long getNativeRef(String s) {
        return 0;
    }

    /**
     * Updates the frontend component views to properly display any changes made to object instances from the Java side.
     */
    public void updateViews() {
        try {
            String obj = mapper.writeValueAsString(simulation);
            System.out.println(obj);
            browser.executeJavaScript("window.vm.$store.commit('setSimulation', JSON.parse('" + obj + "'))", browser.getURL(), 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers an event listener executed when an event with the specified type is caught by the Java part of the app.
     *
     * @param event    The name of the event to listen for. (Ex. "simulation.start")
     * @param listener The method executed whenever the event is fired by the frontend.
     */
    public void addEventListener(String event, Consumer<JSONObject> listener) {
        listeners.put(event, listener);
    }

}
