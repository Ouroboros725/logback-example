package com.ouroboros;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhanxies on 7/3/2017.
 *
 */
public class HttpRestLogConverter extends ClassicConverter {

    private static final String OMIT_MESSAGE = "Data too big. Omitted ...";

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        if (iLoggingEvent.getLoggerName().contains("org.apache.http.wire")
                && iLoggingEvent.getMessage().length() > 1000) {
            return processMessage(iLoggingEvent.getFormattedMessage());
        } else {
            return iLoggingEvent.getFormattedMessage();
        }
    }

    private String processMessage(String message) {
        String msg = Stream.of(message.replaceAll("\'", "\"").split("\""))
                .map(s -> s.length() > 150 ? OMIT_MESSAGE : s).collect(Collectors.joining("\""));

        msg = message.startsWith("\"") || message.startsWith("\'") ? ("\"" + msg) : msg;
        msg = message.endsWith("\"") || message.endsWith("\'") ? (msg + "\"") : msg;

        return msg;
    }
}
