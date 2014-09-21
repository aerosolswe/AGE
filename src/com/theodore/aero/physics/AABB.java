package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class AABB {

    private Vector3 minExtents;
    private Vector3 maxExtents;

    public AABB(Vector3 minExtents, Vector3 maxExtents) {
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }

    public IntersectData intersectAABB(AABB other) {
        Vector3 distance0 = other.getMinExtents().sub(maxExtents);
        Vector3 distance1 = minExtents.sub(other.maxExtents);
        Vector3 distance = distance0.max(distance1);

        float maxDistance = distance.max();

        boolean collison = maxDistance < 0;

        return new IntersectData(collison, distance);
    }

    public Vector3 getMinExtents() {
        return minExtents;
    }

    public void setMinExtents(Vector3 minExtents) {
        this.minExtents = minExtents;
    }

    public Vector3 getMaxExtents() {
        return maxExtents;
    }

    public void setMaxExtents(Vector3 maxExtents) {
        this.maxExtents = maxExtents;
    }
}
