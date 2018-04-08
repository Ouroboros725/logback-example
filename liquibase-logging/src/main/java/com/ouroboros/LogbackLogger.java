package com.ouroboros;

import ch.qos.logback.classic.Level;
import liquibase.logging.LogLevel;
import liquibase.logging.core.AbstractLogger;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by zhanxies on 8/1/2017.
 *
 */
public class LogbackLogger extends AbstractLogger {

    private ch.qos.logback.classic.Logger logger;

    @Override
    public void setName(String name) {
        logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(name);
    }

    public String getName() {
        if (logger != null) {
            return logger.getName();
        }

        return null;
    }

    @Override
    public void setLogLevel(String logLevel, String logFile) {
        setLogLevel(logLevel);
    }

    @Override
    public void setLogLevel(String level) {
        LogLevel logLevel = LogLevel.valueOf(level.toUpperCase());
        setLogLevel(logLevel);
    }

    @Override
    public void setLogLevel(LogLevel level) {
        switch (level) {
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case WARNING:
                logger.setLevel(Level.WARN);
                break;
            case SEVERE:
                logger.setLevel(Level.ERROR);
                break;
            case OFF:
                logger.setLevel(Level.OFF);
                break;
            default:
                logger.setLevel(Level.INFO);
        }
    }

    @Override
    public LogLevel getLogLevel() {
        Level level = logger.getLevel();

        switch (level.toInt()) {
            case Level.OFF_INT:
                return LogLevel.OFF;
            case Level.ERROR_INT:
                return LogLevel.SEVERE;
            case Level.WARN_INT:
                return LogLevel.WARNING;
            case Level.INFO_INT:
                return LogLevel.INFO;
            case Level.DEBUG_INT:
                return LogLevel.DEBUG;
            case Level.TRACE_INT:
                return LogLevel.DEBUG;
            case Level.ALL_INT:
                return LogLevel.DEBUG;
            default:
                return LogLevel.INFO;
        }
    }

    @Override
    public void severe(String message) {
        logger.error(composeMessage(LogLevel.SEVERE, message));
    }

    @Override
    public void severe(String message, Throwable e) {
        logger.error(composeMessage(LogLevel.SEVERE, message), e);
    }

    @Override
    public void warning(String message) {
        logger.warn(composeMessage(LogLevel.WARNING, message));
    }

    @Override
    public void warning(String message, Throwable e) {
        logger.warn(composeMessage(LogLevel.WARNING, message), e);
    }

    @Override
    public void info(String message) {
        logger.info(composeMessage(LogLevel.INFO, message));
    }

    @Override
    public void info(String message, Throwable e) {
        logger.info(composeMessage(LogLevel.INFO, message), e);
    }

    @Override
    public void debug(String message) {
        logger.debug(composeMessage(LogLevel.DEBUG, message));
    }

    @Override
    public void debug(String message, Throwable e) {
        logger.debug(composeMessage(LogLevel.DEBUG, message), e);
    }

    @Override
    public int getPriority() {
        return 10;
    }

    private String composeMessage(LogLevel logLevel, String message) {
        return logLevel + " " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()) + ": " + logger.getName() + ": " + buildMessage(message);
    }
}
