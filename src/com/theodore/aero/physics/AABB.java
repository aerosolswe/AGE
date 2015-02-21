package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class AABB extends Collider {

    public Vector3 minExtents;
    public Vector3 maxExtents;
    public Vector3 center;

    public AABB(Vector3 position, Vector3 minExtents, Vector3 maxExtents) {
        super(Type.AABB);
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
        this.center = position;
    }

    public IntersectData intersectAABB(AABB other) {
        Vector3 distance0 = other.minExtents.add(other.center).sub(maxExtents.add(center));
        Vector3 distance1 = minExtents.add(center).sub(other.maxExtents.add(other.center));
        Vector3 distance = distance0.max(distance1);

        float maxDistance = distance.max();

        return new IntersectData(maxDistance < 0, distance);
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
