package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class PhysicsObject {

    private Vector3 position;
    private Vector3 velocity;

    public PhysicsObject(Vector3 position, Vector3 velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public void update(float delta) {
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        position.z += velocity.z * delta;
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
