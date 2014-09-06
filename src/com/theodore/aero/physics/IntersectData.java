package com.theodore.aero.physics;

public class IntersectData {

    private boolean isIntersecting;
    private float distance;

    public IntersectData(boolean isIntersecting, float distance) {
        this.isIntersecting = isIntersecting;
        this.distance = distance;
    }

    public boolean isIntersecting() {
        return isIntersecting;
    }

    public void setIntersecting(boolean isIntersecting) {
        this.isIntersecting = isIntersecting;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
