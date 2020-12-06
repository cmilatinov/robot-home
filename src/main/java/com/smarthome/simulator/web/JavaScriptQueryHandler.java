package com.smarthome.simulator.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.models.HVAC;
import com.smarthome.simulator.models.Room;
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

    /**
     * Object mapper, used to convert Java class structures to JSON.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * JSON parser, used to parse incoming events with their payload.
     */
    private final JSONParser parser = new JSONParser();

    /**
     * Browser instance, used to execute JavaScript on the web page.
     */
    private final CefBrowser browser;

    /**
     * Simulation instance, used to set the state in the web page.
     */
    private final Simulation simulation;

    /**
     * List of event listeners attached to this handler.
     */
    private final Map<String, Consumer<JSONObject>> listeners = new HashMap<>();

    /**
     * Creates a JavaScript query handler for the given {@link CefBrowser} and {@link Simulation} instances.
     *
     * @param browser    The {@link CefBrowser} instance used to access the web page JavaScript.
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
     * Updates the frontend component views to properly display any changes made to object instances from the Java side.
     */
    public void updateViews() {
        try {
            String state = mapper.writeValueAsString(simulation);
            browser.executeJavaScript("window.vm.$store.commit('setSimulation', JSON.parse('" + state + "'))", browser.getURL(), 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the frontend logs to properly display any changes made from the Java side.
     */
    public void updateLogs() {
        try {
            String state = mapper.writeValueAsString(SmartHomeSimulator.LOGGER.getLogs());
            browser.executeJavaScript("window.vm.$store.commit('setLogs', JSON.parse(`" + state + "`))", browser.getURL(), 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Only update the room temperatures in the front end.
     */
    public void updateRoomTemps(HVAC hvac) {
        Map<Room, HVAC.HVACState> roomStates = hvac.getHVACRoomStates();
        String state = "[" + simulation.getHouseLayout().getRooms()
                .stream()
                .map(r -> "{\"id\":\"" + r.getId() + "\",\"temperature\":" + r.getTemperature() + ",\"hvac\":" + roomStates.get(r).equals(HVAC.HVACState.RUNNING) + "}")
                .reduce("", (str, r) -> str + ", " + r)
                .substring(2) + "]";
        browser.executeJavaScript("window.vm.$store.commit('setRoomTemperatures', JSON.parse('" + state + "'))", browser.getURL(), 0);
    }

    /**
     * Registers an event listener executed when an event with the specified type is caught by the Java part of the app.
     *
     * @param event    The name of the event to listen for. (Ex. "toggleSimulation")
     * @param listener The method executed whenever the event is fired by the frontend.
     */
    public void addEventListener(String event, Consumer<JSONObject> listener) {
        listeners.put(event, listener);
    }

    /**
     * Unused.
     */
    public void onQueryCanceled(CefBrowser cefBrowser, CefFrame cefFrame, long l) {
    }

    /**
     * Unused.
     */
    public void setNativeRef(String s, long l) {
    }

    /**
     * Unused.
     */
    public long getNativeRef(String s) {
        return 0;
    }

}
