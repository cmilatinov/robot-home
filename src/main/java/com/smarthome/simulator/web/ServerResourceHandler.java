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

    private final String rootPath;
    private final Map<String, Resource> resources = new HashMap<>();

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

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestPath = httpExchange.getRequestURI().getPath();
        serveResource(httpExchange, requestPath);
    }

    private static class Resource {
        public final byte[] content;

        public Resource(byte[] content) {
            this.content = content;
        }
    }

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

    private void serveResource(HttpExchange httpExchange, String requestPath) throws IOException {
        requestPath = requestPath.substring(1);
        requestPath = requestPath.replaceAll("//", "/");
        if (!requestPath.contains(".")) {
            requestPath = "index.html";
        }
        serveFile(httpExchange, rootPath + requestPath);
    }

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

    private void writeOutput(HttpExchange httpExchange, int contentLength, byte[] content, String contentType)
            throws IOException {
        if (httpExchange.getRequestMethod().equals("HEAD")) {
            Set<Map.Entry<String, List<String>>> entries = httpExchange.getRequestHeaders().entrySet();
            String response = "";
            for (Map.Entry<String, List<String>> entry : entries) {
                response += entry.toString() + "\n";
            }
            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            httpExchange.sendResponseHeaders(200, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } else {
            httpExchange.getResponseHeaders().set("Content-Type", contentType);
            httpExchange.sendResponseHeaders(200, contentLength);
            httpExchange.getResponseBody().write(content);
        }
        httpExchange.getResponseBody().close();
    }

    private void showError(HttpExchange httpExchange) throws IOException {
        String message = "HTTP error " + 404 + ": " + "The requested resource was not found on server";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(404, messageBytes.length);

        OutputStream os = httpExchange.getResponseBody();
        os.write(messageBytes);
        os.close();
    }

    public static String getFileExt(final String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex >= 0) {
            return path.substring(dotIndex + 1);
        } else {
            return "";
        }
    }

    private static final Map<String, String> MIME_MAP = new HashMap<>();
    static {{
        MIME_MAP.put("appcache", "text/cache-manifest");
        MIME_MAP.put("css", "text/css");
        MIME_MAP.put("html", "text/html");
        MIME_MAP.put("js", "application/javascript");
        MIME_MAP.put("jpg", "image/jpeg");
        MIME_MAP.put("jpeg", "image/jpeg");
        MIME_MAP.put("png", "image/png");
        MIME_MAP.put("svg", "image/svg+xml");
    }}

    private static String getFileMimeType(final String path) {
        String ext = getFileExt(path).toLowerCase();
        return MIME_MAP.getOrDefault(ext, "application/octet-stream");
    }

}