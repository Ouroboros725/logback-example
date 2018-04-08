package com.ouroboros;

import liquibase.logging.LogLevel;
import liquibase.logging.core.DefaultLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by zhanxies on 8/1/2017.
 *
 */
public class LogbackLoggerTest {

    private LogbackLogger logger;

    @BeforeClass
    public void test_setName() {
        logger = new LogbackLogger();

        String name = "logger_test";
        logger.setName(name);

        Assert.assertEquals(name, logger.getName());
    }

    @DataProvider(name = "logLevel")
    public Object[][] createLogLevelTestData() {
        return new Object[][]{
                {LogLevel.DEBUG, LogLevel.DEBUG},
                {LogLevel.INFO, LogLevel.INFO},
                {LogLevel.WARNING, LogLevel.WARNING},
                {LogLevel.SEVERE, LogLevel.SEVERE},
                {LogLevel.OFF, LogLevel.OFF}
        };
    }

    @DataProvider(name = "logLevelString")
    public Object[][] createLogLevelStringTestData() {
        return new Object[][]{
                {"debug", LogLevel.DEBUG},
                {"info", LogLevel.INFO},
                {"warning", LogLevel.WARNING},
                {"severe", LogLevel.SEVERE},
                {"off", LogLevel.OFF},
                {"DEBUG", LogLevel.DEBUG},
                {"INFO", LogLevel.INFO},
                {"WARNING", LogLevel.WARNING},
                {"SEVERE", LogLevel.SEVERE},
                {"OFF", LogLevel.OFF}
        };
    }


    @Test(dataProvider = "logLevel")
    public void test_setLogLevel_getLogLevel(LogLevel setter, LogLevel getter) {
        logger.setLogLevel(setter);
        Assert.assertEquals(logger.getLogLevel(), getter);
    }

    @Test(dataProvider = "logLevelString")
    public void test_setLogLevelString_getLogLevel(String setter, LogLevel getter) {
        logger.setLogLevel(setter);
        Assert.assertEquals(logger.getLogLevel(), getter);
    }

    @Test(dataProvider = "logLevelString")
    public void test_setLogLevelString_getLogLevel_2(String setter, LogLevel getter) {
        logger.setLogLevel(setter, null);
        Assert.assertEquals(logger.getLogLevel(), getter);
    }

    @Test
    public void test_getPriority() {
        Assert.assertTrue(logger.getPriority() > new DefaultLogger().getPriority());
    }

    @Test
    public void test_severe() throws IOException {
        logger.setLogLevel(LogLevel.SEVERE);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.severe("778899");
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("SEVERE"));
        } finally {
            System.setOut(out);
        }
    }

    @Test
    public void test_severe_1() throws IOException {
        logger.setLogLevel(LogLevel.SEVERE);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.severe("778899", new Throwable("12345"));
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("SEVERE") && content.contains("12345"));
        } finally {
            System.setOut(out);
        }
    }

    @Test
    public void test_warning() throws IOException {
        logger.setLogLevel(LogLevel.WARNING);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.warning("778899");
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("WARNING"));
        } finally {
            System.setOut(out);
        }
    }

    @Test
    public void test_warning_1() throws IOException {
        logger.setLogLevel(LogLevel.WARNING);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.warning("778899", new Throwable("12345"));
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("WARNING") && content.contains("12345"));
        } finally {
            System.setOut(out);
        }
    }

    @Test
    public void test_info() throws IOException {
        logger.setLogLevel(LogLevel.INFO);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.info("778899");
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("INFO"));
        } finally {
            System.setOut(out);
        }
    }

    @Test
    public void test_info_1() throws IOException {
        logger.setLogLevel(LogLevel.INFO);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.info("778899", new Throwable("12345"));
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("INFO") && content.contains("12345"));
        } finally {
            System.setOut(out);
        }
    }

    @Test
    public void test_debug() throws IOException {
        logger.setLogLevel(LogLevel.DEBUG);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.debug("778899");
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("DEBUG"));
        } finally {
            System.setOut(out);
        }
    }

    @Test
    public void test_debug_1() throws IOException {
        logger.setLogLevel(LogLevel.DEBUG);

        PrintStream out = System.out;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);
            logger.debug("778899", new Throwable("12345"));
            System.out.flush();

            String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Assert.assertTrue(content.contains("778899") && content.contains("logger_test")
                    && content.contains("DEBUG") && content.contains("12345"));
        } finally {
            System.setOut(out);
        }
    }
}
