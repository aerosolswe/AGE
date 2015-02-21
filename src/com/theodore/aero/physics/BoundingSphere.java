package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class BoundingSphere extends Collider {

    public Vector3 center;
    public float radius;

    public BoundingSphere(Vector3 center, float radius) {
        super(Type.SPHERE);
        this.center = center;
        this.radius = radius;
    }

    public IntersectData intersectBoundingSphere(BoundingSphere other) {
        float radiusDistance = radius + other.radius;
        Vector3 direction = other.getPosition().sub(center);
        float centerDistance = direction.length();
        direction.div(centerDistance);

        float distance = centerDistance - radiusDistance;

        return new IntersectData(centerDistance < radiusDistance, direction.mul(distance));
    }

    @Override
    public void transform(Vector3 translation) {
        center.add(translation);
    }

    @Override
    public Vector3 getPosition() {
        return center;
    }

}
