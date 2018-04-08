package com.ouroboros;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by zhanxies on 10/18/2017.
 *
 */
public class LogRestErrorResponseFilter extends OncePerRequestFilter {

    private static Logger LOGGER = LoggerFactory.getLogger("HttpRestError");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);

            boolean restfulResponseError = Boolean.parseBoolean(MDC.get("responseError"));

            if (restfulResponseError) {
                logError(request, response);
            }
        } finally {
            // Added by LogRestErrorResponseAspect
            // Used and removed by LogRestErrorResponseFilter
            MDC.remove("responseError");
            MDC.remove("responseBody");
        }
    }

    private void logError(HttpServletRequest request, HttpServletResponse response) {
        boolean removeCorrelationId = false;
        try {
            if (Strings.isNullOrEmpty(MDC.get("correlationId"))) {
                String correlationId = response.getHeader("correlationId");
                if (!Strings.isNullOrEmpty(correlationId)) {
                    removeCorrelationId = true;
                    MDC.put("correlationId", correlationId);
                }
            }

            String requestURI = request.getRequestURI();
            String requestMethod = request.getMethod();
            String requestContentType = request.getContentType();
            String requestHeaders = new ServletServerHttpRequest(request).getHeaders().entrySet()
                    .stream().map(entry -> entry.getKey() + ": " + String.join("; ", entry.getValue()))
                    .collect(Collectors.joining("\\r\\n"));
            String requestB;
            try {
                requestB = IOUtils.toString(request.getInputStream());
            } catch (IOException e) {
                requestB = "Failed to parse request body due to error: " + e.getMessage();
            }
            String requestBody = requestB;

            String responseStatus = Integer.toString(response.getStatus());
            String responseContentType = response.getContentType();
            String responseHeaders = response.getHeaderNames().stream()
                    .map(hn -> hn + ": " + String.join("; ", response.getHeaders(hn))).collect(Collectors.joining("\\r\\n"));

            MDC.put("requestURI", requestURI);
            MDC.put("requestMethod", requestMethod);
            MDC.put("requestContentType", requestContentType);
            MDC.put("requestHeaders", requestHeaders);
            MDC.put("requestBody", requestBody);

            MDC.put("responseStatus", responseStatus);
            MDC.put("responseContentType", responseContentType);
            MDC.put("responseHeaders", responseHeaders);

            LOGGER.error("Http rest request failed with error.");
        } finally {
            if (removeCorrelationId) {
                MDC.remove("correlationId");
            }

            MDC.remove("requestURI");
            MDC.remove("requestMethod");
            MDC.remove("requestContentType");
            MDC.remove("requestHeaders");
            MDC.remove("requestBody");

            MDC.remove("responseStatus");
            MDC.remove("responseContentType");
            MDC.remove("responseHeaders");
        }

    }
}
