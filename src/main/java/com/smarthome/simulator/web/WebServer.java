package com.smarthome.simulator.web;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hosts a HTTP web server locally using the contents of the "public" folder.
 */
public class WebServer extends Thread {

    /**
     * The server's public directory.
     */
    private static final String SERVER_HOME = "./public";

    /**
     * The server port.
     */
    public static final int SERVER_PORT = 8081;

    /**
     * The {@link HttpServer} instance used to handle requests.
     */
    private HttpServer httpServer;

    /**
     * {@inheritDoc}
     */
    public void run() {
        try {

            // Allocate one thread
            ExecutorService executor = Executors.newFixedThreadPool(1);

            // Start the http server
            httpServer = HttpServer.create(new InetSocketAddress("localhost", SERVER_PORT), 0);
            httpServer.createContext("/", new ServerResourceHandler(SERVER_HOME));
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

    /**
     * Stops the web server and kills all threads allocated to it.
     */
    public void halt() {
        try {
            httpServer.stop(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (httpServer) {
            httpServer.notifyAll();
        }
    }

}
