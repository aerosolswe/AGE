package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class IntersectData {

    private Vector3 direction;

    private boolean isIntersecting;

    public IntersectData(boolean isIntersecting, Vector3 direction) {
        this.isIntersecting = isIntersecting;
        this.direction = direction;
    }

    public boolean isIntersecting() {
        return isIntersecting;
    }

    public void setIntersecting(boolean isIntersecting) {
        this.isIntersecting = isIntersecting;
    }

    public float getDistance() {
        return direction.length();
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }
}
