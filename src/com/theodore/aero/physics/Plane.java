package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class Plane extends Collider {

    private Vector3 normal;
    private float distance;

    public Plane(Vector3 normal, float distance) {
        super(Type.PLANE);
        this.normal = normal;
        this.distance = distance;
    }

    public IntersectData intersectBoundingSphere(BoundingSphere other) {
        float distanceFromSphereCenter = Math.abs(normal.dot(other.getPosition()) + distance);
        float distanceFromSphere = distanceFromSphereCenter - other.radius;

        boolean collision = distanceFromSphere < 0;
        Vector3 direction = normal.mul(distanceFromSphere);

        return new IntersectData(collision, direction);
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

    @Override
    public void transform(Vector3 translation) {
    }
}
