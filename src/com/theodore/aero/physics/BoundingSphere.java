package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class BoundingSphere extends Collider {

    private Vector3 center;
    private float radius;

    public BoundingSphere(Vector3 center, float radius) {
        super(Type.SPHERE);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void transform(Vector3 translation) {
        center.add(translation);
    }

    public IntersectData intersectBoundingSphere(BoundingSphere other) {
        float radiusDistance = radius + other.getRadius();
        Vector3 direction = other.getPosition().sub(center);
        float centerDistance = direction.length();
        direction.div(centerDistance);

        float distance = centerDistance - radiusDistance;

        return new IntersectData(centerDistance < radiusDistance, direction.mul(distance));
    }

    @Override
    public Vector3 getPosition() {
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
