package com.theodore.aero.math;

import java.util.Random;

public class MathUtils {

    public static float clamp(float a, float min, float max) {
        if (a < min) return min;
        else if (a > max) return max;
        else return a;
    }

    public static float approach(float goal, float current, float delta) {
        float difference = goal - current;

        if (difference > delta)
            return current + delta;
        if (difference < -delta)
            return current - delta;

        return goal;
    }

    public static float random(float min, float max) {
        return min + (float) (Math.random() * ((max - min) + 1));
    }

}
