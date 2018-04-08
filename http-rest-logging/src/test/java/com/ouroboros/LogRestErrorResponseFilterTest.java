package com.ouroboros;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

/**
 * Created by zhanxies on 10/24/2017.
 *
 */
public class LogRestErrorResponseFilterTest {

    private Logger realLogger;

    private List<String> inMDC = new ArrayList<>();

    @BeforeMethod
    public void mockLogger() {
        try {
            Field loggerField = LogRestErrorResponseFilter.class.getDeclaredField("LOGGER");
            loggerField.setAccessible(true);

            realLogger = (Logger) loggerField.get(null);

            inMDC.clear();

            Logger mockLogger = Mockito.mock(Logger.class);
            doAnswer(invocation -> {
                inMDC.add(MDC.get("correlationId"));
                inMDC.add(MDC.get("responseError"));
                inMDC.add(MDC.get("responseBody"));
                inMDC.add(MDC.get("requestContentType"));
                inMDC.add(MDC.get("requestMethod"));
                inMDC.add(MDC.get("requestURI"));
                inMDC.add(MDC.get("requestHeaders"));
                inMDC.add(MDC.get("requestBody"));
                inMDC.add(MDC.get("responseStatus"));
                inMDC.add(MDC.get("responseContentType"));
                inMDC.add(MDC.get("responseHeaders"));

                return null;
            }).when(mockLogger).error(anyString());

            loggerField.set(null, mockLogger);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Assert.fail("failed to mock logger of the class", e);
        }
    }

    @AfterMethod
    public void fixLogger() {
        try {
            Field loggerField = LogRestErrorResponseFilter.class.getDeclaredField("LOGGER");
            loggerField.setAccessible(true);

            loggerField.set(null, realLogger);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Assert.fail("failed to mock logger of the class", e);
        }
    }

    @Test
    public void test_log_error() {
        LogRestErrorResponseFilter filter = new LogRestErrorResponseFilter();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        MDC.clear();

        MDC.put("correlationId", "2234");
        MDC.put("responseError", "true");
        MDC.put("responseBody", "123");
        servletRequest.setContentType("234");
        servletRequest.setMethod("345");
        servletRequest.setRequestURI("456");
        servletRequest.addHeader("567", "567");
        servletRequest.setContent("678".getBytes());
        servletResponse.setStatus(789);
        servletResponse.setContentType("890");
        servletResponse.addHeader("901", "901");

        try {
            filter.doFilterInternal(servletRequest, servletResponse, filterChain);
        } catch (ServletException | IOException e) {
            Assert.fail(e.getMessage(), e);
        }

        Assert.assertEquals(inMDC.get(0), "2234");
        Assert.assertEquals(inMDC.get(1), "true");
        Assert.assertEquals(inMDC.get(2), "123");
        Assert.assertEquals(inMDC.get(3), "234");
        Assert.assertEquals(inMDC.get(4), "345");
        Assert.assertEquals(inMDC.get(5), "456");
        Assert.assertTrue(inMDC.get(6).contains("567: 567"));
        Assert.assertEquals(inMDC.get(7), "678");
        Assert.assertEquals(inMDC.get(8), "789");
        Assert.assertEquals(inMDC.get(9), "890");
        Assert.assertTrue(inMDC.get(10).contains("901: 901"));

        Assert.assertEquals(MDC.get("correlationId"), "2234");
        Assert.assertNull(MDC.get("responseError"));
        Assert.assertNull(MDC.get("responseBody"));
        Assert.assertNull(MDC.get("requestContentType"));
        Assert.assertNull(MDC.get("requestMethod"));
        Assert.assertNull(MDC.get("requestURI"));
        Assert.assertNull(MDC.get("requestHeaders"));
        Assert.assertNull(MDC.get("requestBody"));
        Assert.assertNull(MDC.get("responseStatus"));
        Assert.assertNull(MDC.get("responseContentType"));
        Assert.assertNull(MDC.get("responseHeaders"));
    }

    @Test
    public void test_log_error_correlationInHeader() {
        LogRestErrorResponseFilter filter = new LogRestErrorResponseFilter();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        MDC.clear();

        MDC.put("responseError", "true");
        MDC.put("responseBody", "123");
        servletRequest.setContentType("234");
        servletRequest.setMethod("345");
        servletRequest.setRequestURI("456");
        servletRequest.addHeader("567", "567");
        servletRequest.setContent("678".getBytes());
        servletResponse.setStatus(789);
        servletResponse.setContentType("890");
        servletResponse.addHeader("901", "901");
        servletResponse.addHeader("correlationId", "2234");

        try {
            filter.doFilterInternal(servletRequest, servletResponse, filterChain);
        } catch (ServletException | IOException e) {
            Assert.fail(e.getMessage(), e);
        }

        Assert.assertEquals(inMDC.get(0), "2234");
        Assert.assertEquals(inMDC.get(1), "true");
        Assert.assertEquals(inMDC.get(2), "123");
        Assert.assertEquals(inMDC.get(3), "234");
        Assert.assertEquals(inMDC.get(4), "345");
        Assert.assertEquals(inMDC.get(5), "456");
        Assert.assertTrue(inMDC.get(6).contains("567: 567"));
        Assert.assertEquals(inMDC.get(7), "678");
        Assert.assertEquals(inMDC.get(8), "789");
        Assert.assertEquals(inMDC.get(9), "890");
        Assert.assertTrue(inMDC.get(10).contains("901: 901"));

        Assert.assertNull(MDC.get("correlationId"));
        Assert.assertNull(MDC.get("responseError"));
        Assert.assertNull(MDC.get("responseBody"));
        Assert.assertNull(MDC.get("requestContentType"));
        Assert.assertNull(MDC.get("requestMethod"));
        Assert.assertNull(MDC.get("requestURI"));
        Assert.assertNull(MDC.get("requestHeaders"));
        Assert.assertNull(MDC.get("requestBody"));
        Assert.assertNull(MDC.get("responseStatus"));
        Assert.assertNull(MDC.get("responseContentType"));
        Assert.assertNull(MDC.get("responseHeaders"));
    }

    @Test
    public void test_log_noError() {
        LogRestErrorResponseFilter filter = new LogRestErrorResponseFilter();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        MDC.clear();

        MDC.put("correlationId", "2234");
        MDC.put("responseError", "false");
        MDC.put("responseBody", "123");
        servletRequest.setContentType("234");
        servletRequest.setMethod("345");
        servletRequest.setRequestURI("456");
        servletRequest.addHeader("567", "567");
        servletRequest.setContent("678".getBytes());
        servletResponse.setStatus(789);
        servletResponse.setContentType("890");
        servletResponse.addHeader("901", "901");

        try {
            filter.doFilterInternal(servletRequest, servletResponse, filterChain);
        } catch (ServletException | IOException e) {
            Assert.fail(e.getMessage(), e);
        }

        Assert.assertTrue(inMDC.isEmpty());

        Assert.assertEquals(MDC.get("correlationId"), "2234");
        Assert.assertNull(MDC.get("responseError"));
        Assert.assertNull(MDC.get("responseBody"));
        Assert.assertNull(MDC.get("requestContentType"));
        Assert.assertNull(MDC.get("requestMethod"));
        Assert.assertNull(MDC.get("requestURI"));
        Assert.assertNull(MDC.get("requestHeaders"));
        Assert.assertNull(MDC.get("requestBody"));
        Assert.assertNull(MDC.get("responseStatus"));
        Assert.assertNull(MDC.get("responseContentType"));
        Assert.assertNull(MDC.get("responseHeaders"));
    }
}
