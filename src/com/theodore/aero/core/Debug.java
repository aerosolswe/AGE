package com.theodore.aero.core;

public class Debug {

    private static final boolean DISPLAY_MESSAGE_LOG = false;

    public static void assertError(boolean condition, String errorMessage) {
        if (!condition) {
            System.err.println(errorMessage);
            System.exit(1);
        }
    }

    public static void println(String text) {
        if (DISPLAY_MESSAGE_LOG)
            System.out.println(text);
    }
}
