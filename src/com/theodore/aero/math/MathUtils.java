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

    public static float barryCentric(Vector3 p1, Vector3 p2, Vector3 p3, Vector2 pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static float random(float min, float max) {
        return min + (float) (Math.random() * ((max - min) + 1));
    }

}
