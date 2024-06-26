package util;

import java.io.IOException;
import java.util.logging.*;

public class Logger {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

    public static void initialize(String logFile, String logLevel) throws IOException {
        FileHandler fileHandler = new FileHandler(logFile, true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.parse(logLevel));
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void error(String message) {
        logger.severe(message);
    }

    public static void debug(String message) {
        logger.fine(message);
    }

    public static void close() {
        for (Handler handler : logger.getHandlers()) {
            handler.close();
        }
    }
}
