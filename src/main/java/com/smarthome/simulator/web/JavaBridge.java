package com.smarthome.simulator.web;

import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JavaBridge {

    private static final Map<String, Consumer<JSObject>> listeners = new HashMap<>();

    public Example[] array = { new Example(1), new Example(3), new Example(5), new Example(7) };

    public void addEventListener(String event, Consumer<JSObject> listener) {
        listeners.put(event, listener);
    }

    public void dispatchEvent(String event, JSObject payload) {
        Consumer<JSObject> listener = listeners.get(event);
        if (listener != null)
            listener.accept(payload);
    }

    public void toLowerCase(String value) {
        if (value != null) {
            System.out.println(value.toLowerCase());
        }
    }

    public static class Example {
        public int a;
        public Example(int a) {
            this.a = a;
        }
    }

}
