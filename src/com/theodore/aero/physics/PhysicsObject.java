package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class PhysicsObject {

    private Collider collider;

    private Vector3 position;
    private Vector3 oldPosition;
    private Vector3 velocity;

    public PhysicsObject(Collider collider, Vector3 velocity) {
        this.collider = collider;
        this.position = collider.getPosition();
        this.oldPosition = position;
        this.velocity = velocity;
    }

    public void update(float delta) {
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        position.z += velocity.z * delta;
    }

    public Collider getCollider() {
        Vector3 translation = position.sub(oldPosition);
        oldPosition = position;
        collider.transform(translation);

        return collider;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }
}
