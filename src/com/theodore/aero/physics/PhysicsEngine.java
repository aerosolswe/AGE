package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public class PhysicsEngine {

    private float gravity;

    private ArrayList<PhysicsObject> objects;

    public PhysicsEngine(float gravity) {
        this.gravity = gravity;

        objects = new ArrayList<PhysicsObject>();
    }

    public PhysicsEngine() {
        this(-9.81f);
    }

    public void update(float delta) {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).setGravity(gravity);
            objects.get(i).update(delta);
        }
    }

    public void handleCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i; j < objects.size(); j++) {
                if (i != j) {
                    IntersectData data0 = objects.get(i).getCollider().intersect(objects.get(j).getCollider());
                    IntersectData data1 = objects.get(j).getCollider().intersect(objects.get(i).getCollider());

                    if (data0.isIntersecting()) {
                        handleCollision(objects.get(i), data0);
                    }

                    if (data1.isIntersecting()) {
                        handleCollision(objects.get(j), data1);
                    }
                }
            }
        }
    }

    private void handleCollision(PhysicsObject physicsObject, IntersectData data) {
        Vector3 direction = data.getDirection();
//        physicsObject.setVelocity(physicsObject.getVelocity().reflect(direction));

        if (direction.x < 1) {
            physicsObject.setVelocity(physicsObject.getVelocity().mul(new Vector3(0 - physicsObject.getRestitution(), 1, 1)));
        }
        if (direction.y < 1) {
            physicsObject.setVelocity(physicsObject.getVelocity().mul(new Vector3(1, -physicsObject.getVelocity().lengthSquared(), 1)));
        }
        if (direction.z < 1) {
            physicsObject.setVelocity(physicsObject.getVelocity().mul(new Vector3(1, 1, 0 - physicsObject.getRestitution())));
        }
//        owner.position.Y -=owner.y_velocity*penetration_length/(owner.velocity);

    }

    public void addPhysicsObject(PhysicsObject object) {
        objects.add(object);
    }

    public ArrayList<PhysicsObject> getObjects() {
        return objects;
    }

}
