package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

public class PhysicsObject {

    private Collider collider;

    private Vector3 position;
    private Vector3 oldPosition;
    private Vector3 velocity;

    private float gravity = 0;
    private float mass = 1;
    private float restitution = 0.3f;

    public PhysicsObject(Collider collider) {
        this.collider = collider;
        this.position = collider.getPosition();
        this.oldPosition = position;
        this.velocity = new Vector3();
    }

    public void update(float delta) {
        position.x = position.x + velocity.x * delta;
        position.y = position.y + velocity.y * delta;
        position.z = position.z + velocity.z * delta;

        velocity.y += (gravity * mass);
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

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }
}
