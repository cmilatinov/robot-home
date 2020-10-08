package com.smarthome.simulator.web;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer extends Thread {

    private HttpServer httpServer;

    private static final String serverHome = "./public";
    private static final int port = 8080;

    public void run() {
        try {

            // Allocate one thread
            ExecutorService executor = Executors.newFixedThreadPool(1);

            // Start the http server
            httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            httpServer.createContext("/", new ServerResourceHandler(serverHome));
            httpServer.setExecutor(executor);
            httpServer.start();

            // Wait here until shutdown
            synchronized (this) {
                try {
                    this.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void halt() {
        try {
            httpServer.stop(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (this) {
            httpServer.notifyAll();
        }
    }

}
