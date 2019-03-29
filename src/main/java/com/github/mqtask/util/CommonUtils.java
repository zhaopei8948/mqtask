package com.github.mqtask.util;

import org.apache.commons.logging.Log;
import org.springframework.cglib.util.StringSwitcher;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonUtils {

    public static void logError(Log log, Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        log.error(stringWriter.toString());
    }
}
