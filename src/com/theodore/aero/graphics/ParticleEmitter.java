package com.theodore.aero.graphics;

import com.theodore.aero.components.GameComponent;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.MathUtils;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class ParticleEmitter extends GameComponent {
    private static Random randomGenerator = new Random();
    private final List<Particle> particles;

    private Mesh mesh;
    private Material material;

    private Vector3 gravity;
    private Vector3 initialVelocity;
    private Vector3 scale;

    private boolean lookAtCamera;

    private float velocityModifier;
    private float spawningRate;
    private float randomness;

    private float particleLifeTime;

    public ParticleEmitter() {
        this(new Mesh("rectangle"), new Material(), 0.2f, 2, new Vector3(0.3f, 0.3f, 0.3f), new Vector3(0, 0.0009f, 0), new Vector3(-0.01f, 0, -0.01f), 1, 1, false);
//        this(Mesh.get("rectangle"), new Material(), 0.2f, 2, new Vector3(0.3f, 0.3f, 0.3f), new Vector3(0, 0.0009f, 0), new Vector3(-0.01f, 0, -0.01f), 1, 1, false);
    }

    /**
     * @param spawningRate     the amount of particles generated every call to 'ParticleEmitter.update()'
     * @param particleLifeTime the life time of the particle in calls to 'ParticleEmitter.update()'
     * @param gravity          the gravity acceleration applied to all the particles each call to 'ParticleEmitter.update()'
     * @param initialVelocity  the base initial velocity
     * @param velocityModifier the particle velocity modifier
     */
    public ParticleEmitter(Mesh mesh, Material material, float spawningRate, float particleLifeTime, Vector3 scale, Vector3 gravity,
                           Vector3 initialVelocity, float velocityModifier, float randomness, boolean lookAtCamera) {
        this.mesh = mesh;
        this.material = material;
        this.spawningRate = spawningRate;
        this.particleLifeTime = particleLifeTime;
        this.scale = scale;
        this.gravity = gravity;
        this.particles = new ArrayList<Particle>((int) spawningRate * (int) particleLifeTime);
        this.initialVelocity = initialVelocity;
        this.velocityModifier = velocityModifier;
        this.randomness = randomness;
        this.lookAtCamera = lookAtCamera;
    }

    public float getVelocityModifier() {
        return velocityModifier;
    }

    public void setVelocityModifier(float velocityModifier) {
        this.velocityModifier = velocityModifier;
    }

    public float getSpawningRate() {
        return spawningRate;
    }

    public void setSpawningRate(float spawningRate) {
        this.spawningRate = spawningRate;
    }

    public Vector3 getGravity() {
        return gravity;
    }

    public void setGravity(Vector3 gravity) {
        this.gravity = gravity;
    }

    public float getParticleLifeTime() {
        return particleLifeTime;
    }

    public void setParticleLifeTime(int particleLifeTime) {
        this.particleLifeTime = particleLifeTime;
    }

    public Vector3 getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(Vector3 initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public boolean getLookAtCamera() {
        return lookAtCamera;
    }

    public void setLookAtCamera(boolean lookAtCamera) {
        this.lookAtCamera = lookAtCamera;
    }

    public void setParticleLifeTime(float particleLifeTime) {
        this.particleLifeTime = particleLifeTime;
    }

    private Particle generateNewParticle() {
        Vector3 particleLocation = new Vector3(getTransform().getPosition());
        Vector3 particleVelocity = new Vector3();
        float randomX = MathUtils.random(-randomness, randomness);
        float randomY = MathUtils.random(0.5f, randomness);
        float randomZ = MathUtils.random(-randomness, randomness);

        particleVelocity.x = (randomX * initialVelocity.x);
        particleVelocity.y = (randomY * initialVelocity.y);
        particleVelocity.z = (randomZ * initialVelocity.z);
        particleVelocity.mul(velocityModifier);
        return new Particle(particleLocation, scale, particleVelocity, particleLifeTime, lookAtCamera);
    }

    private float i;

    @Override
    public void update(float delta) {
        super.update(delta);

        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.update(gravity, delta);
            if (particle.isDestroyed()) {
                particles.remove(i);
                i--;
            }
        }

        if (i >= spawningRate) {
            particles.add(generateNewParticle());
            i = 0;
        } else {
            i += 1 * delta;
        }

        Collections.sort(particles, new Comparator<Particle>() {
            @Override
            public int compare(Particle p, Particle p0) {
                return p.compareTo(p0);
            }
        });
    }

    @Override
    public void render(Shader shader, Graphics graphics) {
        super.render(shader, graphics);

        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);

            particle.draw(shader, mesh, material, graphics);
        }
    }


    private static class Particle implements Comparable {

        public Transform transform;
        public Vector3 velocity;
        public boolean lookAtCamera;
        public float expireTime;
        public float lifeTime;
        public float cameraDistance;

        private Particle(Vector3 position, Vector3 scale, Vector3 velocity, float expireTime, boolean lookAtCamera) {
            this.transform = new Transform();
            this.transform.setPosition(position);
            this.transform.setScale(scale);
            this.velocity = velocity;
            this.expireTime = expireTime;
            this.lifeTime = expireTime;
            this.lookAtCamera = lookAtCamera;
        }

        public void draw(Shader shader, Mesh m, Material mat, Graphics graphics) {
            mat.setFloat("alpha", expireTime / lifeTime);
            shader.bind();
            shader.updateUniforms(transform, mat, graphics);
            Aero.graphicsUtil.disableCullFace();
            m.draw(GL11.GL_TRIANGLES);
            Aero.graphicsUtil.enableCullFace(GL11.GL_BACK);
        }

        public boolean isDestroyed() {
            return expireTime <= 0;
        }

        public void update(Vector3 gravity, float delta) {
            if (lookAtCamera) {
                transform.lookAt(Aero.graphics.getMainCamera().getTransform().getTransformedPos(), new Vector3(0, 1, 0));
            }

            cameraDistance = Aero.graphics.getMainCamera().getTransform().getPosition().distance(transform.getPosition());

            Vector3 newVel = velocity;
            newVel.x += gravity.x * delta;
            newVel.y += gravity.y * delta;
            newVel.z += gravity.z * delta;

            Vector3 position = new Vector3(transform.getPosition());
            position.x += newVel.x * delta;
            position.y += newVel.y * delta;
            position.z += newVel.z * delta;
            transform.setPosition(position);

            expireTime -= 1 * delta;
        }

        @Override
        public String toString() {
            return "Particle{" +
                    "position=" + transform.getPosition() +
                    ", velocity=" + velocity +
                    ", expireTime=" + expireTime +
                    '}';
        }

        @Override
        public int compareTo(Object o) {
            Particle other = (Particle) o;

            if (other.cameraDistance < this.cameraDistance) {
                return -1;
            } else if (other.cameraDistance > this.cameraDistance) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
