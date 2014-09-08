package com.theodore.aero.physics;

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

    public void addPhysicsObject(PhysicsObject object) {
        objects.add(object);
    }

    public ArrayList<PhysicsObject> getObjects() {
        return objects;
    }

}
