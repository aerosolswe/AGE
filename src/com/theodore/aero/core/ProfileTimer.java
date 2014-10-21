package com.theodore.aero.core;

import com.theodore.aero.math.Time;

public class ProfileTimer {

    private int invocations;
    private double totalTime;
    private double startTime;
    public String message;

    public ProfileTimer() {
        invocations = 0;
        totalTime = 0;
        startTime = 0;
        message = "";
    }

    public void startInvocation() {
        startTime = Time.getDoubleTime();
    }

    public void stopInvocation() {
        if (startTime == 0) {
            System.out.println("Error: stopInvocation called without ever calling startInvocation!");
        } else {
            invocations++;
            totalTime += (Time.getDoubleTime() - startTime);
            startTime = 0.0;
        }
    }

    public double getTimeAndReset(double inDiv) {
        double dividend = inDiv;

        if (dividend == 0) {
            dividend = invocations;
        }

        double time;

        if (dividend == 0) {
            time = 0;
        } else {
            time = (1000.0 * totalTime) / dividend;
        }

        totalTime = 0;
        invocations = 0;

        return time;
    }

    public double displayAndReset(String message, double inDiv) {
        double dividend = inDiv;

        if (dividend == 0) {
            dividend = invocations;
        }

        double time;

        if (dividend == 0) {
            time = 0;
        } else {
            time = (1000.0 * totalTime) / dividend;
        }

        this.message = message + time + " ms";
        System.out.println(this.message);

        totalTime = 0;
        invocations = 0;

        return time;
    }

}
