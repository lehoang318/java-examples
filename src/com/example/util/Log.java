package com.example.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class Log {
    private static AtomicBoolean DebugMode = new AtomicBoolean(false);

    private Log() {}

    public static void i(String TAG, String message) {
        System.out.print(String.format(Locale.US, "[%s] %s\n", TAG, message));
    }

    public static void e(String TAG, String message) {
        System.out.print(String.format(Locale.US, "[%s] %s\n", TAG, message));
    }

    public static void e(String TAG, String message, Exception e) {
        final String INDENT = "  ";

        String trace = "";

        if (null != e) {
            try (BufferedReader reader = new BufferedReader(new StringReader(e.getMessage()));) {
                String line;

                while(null != (line = reader.readLine())) {
                    trace += INDENT + line + "\n";
                }
            } catch (IOException exc) {
                // do nothing
            }
        }

        System.out.print(String.format(Locale.US, "[%s] %s. Trace:\n%s\n", TAG, message, trace));
    }

    public static void enableDebugMode() {
        DebugMode.set(true);
    }

    public static void disableDebugMode() {
        DebugMode.set(false);
    }

    public static void d(String TAG, String message) {
        if (DebugMode.get()) {
            System.out.print(String.format(Locale.US, "[%s] %s\n", TAG, message));
        }
    }
}
