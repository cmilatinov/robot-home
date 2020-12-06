package com.smarthome.simulator.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Serves static resources from the specified root folder.
 */
public class ServerResourceHandler implements HttpHandler {

    /**
     * The path to the directory of static files to serve.
     */
    private final String rootPath;

    /**
     * The map associating paths to resources to serve.
     */
    private final Map<String, Resource> resources = new HashMap<>();

    /**
     * Creates a new {@link ServerResourceHandler} instance serving the given root directory.
     *
     * @param rootPath The path to the static file directory.
     * @throws IOException If processing a file in the directory fails.
     */
    public ServerResourceHandler(String rootPath) throws IOException {
        this.rootPath = rootPath.endsWith("/") ? rootPath : rootPath + "/";

        File[] files = new File(rootPath).listFiles();
        if (files == null) {
            throw new IllegalStateException("Couldn't find webroot: " + rootPath);
        }
        for (File f : files) {
            processFile("", f);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestPath = httpExchange.getRequestURI().getPath();
        serveResource(httpExchange, requestPath);
    }

    /**
     * Represents a server resource.
     */
    private static class Resource {
        public final byte[] content;

        public Resource(byte[] content) {
            this.content = content;
        }
    }

    /**
     * Adds a file to the list of files to serve.
     *
     * @param path The path to the file relative to the root directory.
     * @param file The file to process.
     * @throws IOException If the file resource is inaccessible.
     */
    private void processFile(String path, File file) throws IOException {
        if (!file.isDirectory()) {
            resources.put(path + file.getName(), new Resource(readResource(new FileInputStream(file))));
        }

        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                processFile(path + file.getName() + "/", sub);
            }
        }
    }

    /**
     * Read the specified resource into a byte array.
     *
     * @param in The input stream to the corresponding resource.
     * @return byte[] The content of the resource.
     * @throws IOException If the resource is inaccessible.
     */
    private byte[] readResource(final InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutputStream gout = new DataOutputStream(bout);
        byte[] bs = new byte[4096];
        int r;
        while ((r = in.read(bs)) >= 0) {
            gout.write(bs, 0, r);
        }
        gout.flush();
        gout.close();
        in.close();
        return bout.toByteArray();
    }

    /**
     * Serves the requested resource in response to an HTTP request.
     *
     * @param httpExchange The {@link HttpExchange} taking place.
     * @param requestPath  The requested URL path relative to the root.
     * @throws IOException If the resource cannot be accessed.
     */
    private void serveResource(HttpExchange httpExchange, String requestPath) throws IOException {
        requestPath = requestPath.substring(1);
        requestPath = requestPath.replaceAll("//", "/");
        if (!requestPath.contains(".")) {
            requestPath = "index.html";
        }
        serveFile(httpExchange, rootPath + requestPath);
    }

    /**
     * Serves the requested file.
     *
     * @param httpExchange The {@link HttpExchange} taking place.
     * @param resourcePath The requested resource path relative to the root directory.
     * @throws IOException If the file cannot be accessed.
     */
    private void serveFile(HttpExchange httpExchange, String resourcePath) throws IOException {
        File file = new File(resourcePath);
        if (file.exists()) {
            InputStream in = new FileInputStream(resourcePath);

            Resource res;

            if (resources.get(resourcePath) == null) {
                res = new Resource(readResource(in));
            } else {
                res = resources.get(resourcePath);
            }

            String mimeType = getFileMimeType(resourcePath);
            writeOutput(httpExchange, res.content.length, res.content, mimeType);
        } else {
            showError(httpExchange);
        }
    }

    /**
     * Writes the correct HTTP response headers and body in response to a request.
     *
     * @param httpExchange  The {@link HttpExchange} taking place.
     * @param contentLength The size of the content in bytes.
     * @param content       The content itself.
     * @param contentType   The mime type of the content.
     * @throws IOException If the response fails.
     */
    private void writeOutput(HttpExchange httpExchange, int contentLength, byte[] content, String contentType)
            throws IOException {
        if (httpExchange.getRequestMethod().equals("HEAD")) {
            Set<Map.Entry<String, List<String>>> entries = httpExchange.getRequestHeaders().entrySet();
            StringBuilder response = new StringBuilder();
            for (Map.Entry<String, List<String>> entry : entries) {
                response.append(entry.toString()).append("\n");
            }
            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            httpExchange.sendResponseHeaders(200, response.length());
            httpExchange.getResponseBody().write(response.toString().getBytes());
        } else {
            httpExchange.getResponseHeaders().set("Content-Type", contentType);
            httpExchange.sendResponseHeaders(200, contentLength);
            httpExchange.getResponseBody().write(content);
        }
        httpExchange.getResponseBody().close();
    }

    /**
     * Displays a 404 error if the requested resource is not found.
     *
     * @param httpExchange The {@link HttpExchange} taking place.
     * @throws IOException If the response fails.
     */
    private void showError(HttpExchange httpExchange) throws IOException {
        String message = "HTTP error " + 404 + ": " + "The requested resource was not found on server";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(404, messageBytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(messageBytes);
        os.close();
    }

    /**
     * Returns the file extension from a file path.
     *
     * @param path The full path to the file.
     * @return String The extension of the file (ex: "/some/path/file.txt" would return "txt").
     */
    public static String getFileExt(final String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex >= 0) {
            return path.substring(dotIndex + 1);
        } else {
            return "";
        }
    }

    /**
     * Mime type map for files.
     */
    private static final Map<String, String> MIME_MAP = new HashMap<>();

    static {
        {
            MIME_MAP.put("css", "text/css");
            MIME_MAP.put("html", "text/html");
            MIME_MAP.put("js", "application/javascript");
            MIME_MAP.put("jpg", "image/jpeg");
            MIME_MAP.put("jpeg", "image/jpeg");
            MIME_MAP.put("png", "image/png");
            MIME_MAP.put("svg", "image/svg+xml");
        }
    }

    /**
     * Returns the mime type corresponding to a specific file.
     *
     * @param path The full path to the file.
     * @return String The mime type of the file (ex: "/some/path/index.html" would return "text/html").
     */
    private static String getFileMimeType(final String path) {
        String ext = getFileExt(path).toLowerCase();
        return MIME_MAP.getOrDefault(ext, "application/octet-stream");
    }

}