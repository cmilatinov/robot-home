package com.smarthome.simulator.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthome.simulator.models.Simulation;
import org.json.simple.parser.JSONParser;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JavaScriptQueryHandler implements CefMessageRouterHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JSONParser parser = new JSONParser();

    private final CefBrowser browser;
    private final Simulation simulation;

    private final Map<String, Consumer<JSONObject>> listeners = new HashMap<>();

    public JavaScriptQueryHandler(CefBrowser browser, Simulation simulation) {
        this.browser = browser;
        this.simulation = simulation;
    }

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

    public void onQueryCanceled(CefBrowser cefBrowser, CefFrame cefFrame, long l) {

    }

    public void setNativeRef(String s, long l) {

    }

    public long getNativeRef(String s) {
        return 0;
    }

    public void updateViews() {
        try {
            String obj = mapper.writeValueAsString(simulation);
            System.out.println(obj);
            browser.executeJavaScript("window.vm.$store.commit('setSimulation', JSON.parse('" + obj + "'))", browser.getURL(), 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void addEventListener(String event, Consumer<JSONObject> listener) {
        listeners.put(event, listener);
    }

}
