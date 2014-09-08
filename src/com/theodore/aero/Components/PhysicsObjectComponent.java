package com.theodore.aero.components;

import com.theodore.aero.physics.PhysicsObject;

public class PhysicsObjectComponent extends GameComponent {

    private PhysicsObject physicsObject;

    public PhysicsObjectComponent(PhysicsObject physicsObject) {
        this.physicsObject = physicsObject;
    }

    @Override
    public void update(float delta) {
        getTransform().setPosition(physicsObject.getPosition());
    }
}
