package com.smarthome.simulator.utils;

import com.smarthome.simulator.web.JavaScriptQueryHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Logger class logs the events of the modules.
 */
public class Logger {


    /**
     * Logging an information.
     */
    public static final int INFO = 0;

    /**
     * Logging while debugging.
     */
    public static final int DEBUG = 1;

    /**
     * Logging a warning.
     */
    public static final int WARN = 2;

    /**
     * Logging an error.
     */
    public static final int ERROR = 3;

    /**
     * Predefined prefixes to logging message.
     */
    private static final String[] LOG_PREFIXES = {
            "[$date] [$tag] INFO - ",
            "[$date] [$tag] DEBUG - ",
            "[$date] [$tag] WARNING - ",
            "[$date] [$tag] ERROR - "
    };

    /**
     * Formatter for the date values.
     */
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

    /**
     * Log file path.
     */
    private final String DEFAULT_LOG_FILEPATH = "./logs/" + new SimpleDateFormat("MMddyyyy-HHmmss").format(new Date())
            + ".log";

    /**
     * List of logs.
     */
    private final List<String> log = new ArrayList<>();

    /**
     * Log file.
     */
    private final File logFile;

    /**
     * Log file writer.
     */
    private PrintWriter writer;

    /**
     * Boolean to verify if the log file is closed yet.
     */
    private boolean closed = false;

    /**
     * A reference to the browser query handler so we can update the logs displayed.
     */
    private final JavaScriptQueryHandler handler;

    /**
     * Creates a new logger object with the default file path.
     */
    public Logger(JavaScriptQueryHandler handler) {
        this.handler = handler;
        this.logFile = new File(DEFAULT_LOG_FILEPATH);

        if (!createLogFile())
            throw new RuntimeException("Unable to create log file.");
    }

    /**
     * Creates the assigned log file for this logger.
     *
     * @return [<b>boolean</b>] True if creating the file and its parent directories
     * succeeds, false otherwise.
     */
    private boolean createLogFile() {
        try {

            // Make necessary directories
            if (!logFile.getParentFile().exists() && !logFile.getParentFile().mkdirs())
                return false;

            // Init print writer
            writer = new PrintWriter(new FileOutputStream(logFile));

            // Done!
            return true;

        } catch (Exception e) {

            // Error
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Logs the specified string to the console and to the log file with the specified logging level.
     *
     * @param logLevel The logging level to use ({@link Logger#INFO}, {@link Logger#DEBUG}, {@link Logger#WARN}, {@link Logger#ERROR}).
     * @param str      The string to log.
     */
    public synchronized void log(int logLevel, String tag, String str) {

        // Invalid log level or log closed.
        if (closed || logLevel < 0 || logLevel >= LOG_PREFIXES.length)
            return;

        // Log string
        String out = LOG_PREFIXES[logLevel].replace("$date", format.format(new Date())).replace("$tag", tag) + str;
        writer.println(out);
        writer.flush();
        System.out.println(out);

        // Add every line to the log
        log.addAll(Arrays.asList(out.split("\n")));

        // Remove from log until we have a maximum of 200 lines
        while (log.size() > 200) {
            log.remove(0);
        }

        // Update logs displayed in the frontend
        handler.updateLogs();

    }

    /**
     * Commits any pending log contents to the log file and closes the logger.
     */
    public void close() {

        // Log already closed.
        if (closed)
            return;

        writer.flush();
        writer.close();
        closed = true;
    }

    /**
     * Returns the current logs separated by newline characters.
     *
     * @return {@link List<String>} The logs in memory separated by newline characters.
     */
    public List<String> getLogs() {
        return log;
    }

}
