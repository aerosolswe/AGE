package com.theodore.aero.graphics;

import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.mesh.Primitives;
import com.theodore.aero.math.Vector3;

public class ParticleEmitterBuilder {

    private Mesh mesh = new Mesh("rectangle");
    private Material material = new Material();

    private Vector3 gravity = new Vector3(0, -9f, 0);
    private Vector3 initialVelocity = new Vector3(1, 5, 1);
    private Vector3 scale = new Vector3(0.3f, 0.3f, 0.3f);

    private boolean lookAtCamera = false;

    private float velocityModifier = 1;
    private float spawningRate = 0.5f;
    private float randomness = 1f;

    private float particleLifeTime = 3;

    public Mesh getMesh() {
        return mesh;
    }

    public ParticleEmitterBuilder setMesh(Mesh mesh) {
        this.mesh = mesh;

        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public ParticleEmitterBuilder setMaterial(Material material) {
        this.material = material;

        return this;
    }

    public Vector3 getGravity() {
        return gravity;
    }

    public ParticleEmitterBuilder setGravity(Vector3 gravity) {
        this.gravity = gravity;

        return this;
    }

    public Vector3 getInitialVelocity() {
        return initialVelocity;
    }

    public ParticleEmitterBuilder setInitialVelocity(Vector3 initialVelocity) {
        this.initialVelocity = initialVelocity;

        return this;
    }

    public Vector3 getScale() {
        return scale;
    }

    public ParticleEmitterBuilder setScale(Vector3 scale) {
        this.scale = scale;

        return this;
    }

    public boolean getLookAtCamera() {
        return lookAtCamera;
    }

    public ParticleEmitterBuilder setLookAtCamera(boolean lookAtCamera) {
        this.lookAtCamera = lookAtCamera;

        return this;
    }

    public float getVelocityModifier() {
        return velocityModifier;
    }

    public ParticleEmitterBuilder setVelocityModifier(float velocityModifier) {
        this.velocityModifier = velocityModifier;

        return this;
    }

    public float getSpawningRate() {
        return spawningRate;
    }

    public ParticleEmitterBuilder setSpawningRate(float spawningRate) {
        this.spawningRate = spawningRate;

        return this;
    }

    public float getParticleLifeTime() {
        return particleLifeTime;
    }

    public ParticleEmitterBuilder setParticleLifeTime(float particleLifeTime) {
        this.particleLifeTime = particleLifeTime;

        return this;
    }

    public ParticleEmitter createParticleEmitter() {
        return new ParticleEmitter(mesh, material, spawningRate, particleLifeTime, scale, gravity, initialVelocity, velocityModifier, randomness, lookAtCamera);
    }
}
