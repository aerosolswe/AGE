package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public abstract class Collider {

    public enum Type {
        SPHERE,
        AABB,
        PLANE
    }

    private Type type;

    public Collider(Type type) {
        this.type = type;
    }

    public IntersectData intersect(Collider other) {
        if (type == Type.SPHERE && other.getType() == Type.SPHERE) {
            BoundingSphere self = (BoundingSphere) this;

            return self.intersectBoundingSphere((BoundingSphere) other);
        }

        if (type == Type.AABB && other.getType() == Type.AABB) {
            AABB self = (AABB) this;

            return self.intersectAABB((AABB) other);
        }

        System.out.println("Error in physics engine, collisions not yet implemented between specified colliders!");

        return new IntersectData(false, new Vector3(0, 0, 0));
    }

    public abstract void transform(Vector3 translation);

    public Vector3 getPosition() {
        return new Vector3(0, 0, 0);
    }

    public void setPosition(Vector3 position) {

    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
