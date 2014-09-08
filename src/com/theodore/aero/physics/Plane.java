package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class Plane {

    private Vector3 normal;
    private float distance;

    public Plane(Vector3 normal, float distance) {
        this.normal = normal;
        this.distance = distance;
    }

    public IntersectData intersectSphere(BoundingSphere other) {
        float distanceFromSphereCenter = Math.abs(normal.dot(other.getCenter()) + distance);
        float distanceFromSphere = distanceFromSphereCenter - other.getRadius();

        return new IntersectData(distanceFromSphere < 0, distanceFromSphere);
    }

    public Plane normalized() {
        float length = normal.length();

        return new Plane(normal.div(length), distance /= length);
    }

    public Vector3 getNormal() {
        return normal;
    }

    public void setNormal(Vector3 normal) {
        this.normal = normal;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
