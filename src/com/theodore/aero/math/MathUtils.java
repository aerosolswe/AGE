package com.theodore.aero.math;

public class MathUtils {

    public static float approach(float goal, float current, float delta) {
        float difference = goal - current;

        if (difference > delta)
            return current + delta;
        if (difference < -delta)
            return current - delta;

        return goal;
    }

}
