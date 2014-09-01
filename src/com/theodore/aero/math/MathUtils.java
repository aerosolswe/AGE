package com.theodore.aero.math;

public class MathUtils {

    public static final float PI = 3.14159265358979323846f;

    public static float toRadians(float angdeg) {
        return angdeg / 180.0f * PI;
    }

    public static float toDegrees(float angrad) {
        return angrad * 180.0f / PI;
    }

    public static float asin(float a) {
        return (float) Math.asin(a);
    }

    public static float atan2(float y, float x) {
        return (float) Math.atan2(y, x);
    }

    public static float abs(float a) {
        return Math.abs(a);
    }

    public static float approach(float goal, float current, float delta) {
        float difference = goal - current;

        if (difference > delta)
            return current + delta;
        if (difference < -delta)
            return current - delta;

        return goal;
    }
}
