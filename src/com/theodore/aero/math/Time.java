package com.theodore.aero.math;

public class Time {
    private static final long SECOND = 1000000000L;

    public static double getTime() {
        return (double) System.nanoTime() / (double) SECOND;
    }

}
