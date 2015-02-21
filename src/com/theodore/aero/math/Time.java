package com.theodore.aero.math;

public class Time {
    private static final long SECOND = 1000000000L;

    public static long getLongTime() {
        return System.nanoTime() / SECOND;
    }

    public static double getDoubleTime() {
        return (double) System.nanoTime() / (double) SECOND;
    }

}
