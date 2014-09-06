package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class BoundingSphere {

    private Vector3 center;
    private float radius;

    public BoundingSphere(Vector3 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public IntersectData intersectBoundingSphere(BoundingSphere other){
        float radiusDistance = radius + other.getRadius();
        float centerDistance = other.getCenter().sub(center).length();

        float distance = centerDistance - radiusDistance;

        return new IntersectData(centerDistance < radiusDistance, distance);
    }

    public Vector3 getCenter() {
        return center;
    }

    public void setCenter(Vector3 center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
