package com.theodore.aero.physics;

import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public class PhysicsEngine {

    private ArrayList<PhysicsObject> objects;

    public PhysicsEngine() {
        objects = new ArrayList<PhysicsObject>();
    }

    public void update(float delta) {
        for (int i = 0; i < objects.size(); i++) {
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
                        Vector3 direction = data0.getDirection().normalized();

                        objects.get(i).setVelocity(objects.get(i).getVelocity().reflect(direction));
                    }

                    if (data1.isIntersecting()) {
                        Vector3 direction = data1.getDirection().normalized();

                        objects.get(j).setVelocity(objects.get(j).getVelocity().reflect(direction));
                    }
                }
            }
        }
    }

    public void addPhysicsObject(PhysicsObject object) {
        objects.add(object);
    }

    public ArrayList<PhysicsObject> getObjects() {
        return objects;
    }

}
