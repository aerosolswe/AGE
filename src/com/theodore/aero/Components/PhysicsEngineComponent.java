package com.theodore.aero.components;

import com.theodore.aero.physics.PhysicsEngine;

public class PhysicsEngineComponent extends GameComponent {

    private PhysicsEngine physicsEngine;

    public PhysicsEngineComponent(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    @Override
    public void update(float delta) {
        physicsEngine.update(delta);
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }
}
