package com.theodore.tests;

import com.theodore.aero.components.*;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Debug;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Input;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.g3d.Attenuation;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.SkyGradient;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.math.MathUtils;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class RenderTest extends Screen {

    private GameObject cameraObject;

    private PointLight fire;
    private ParticleEmitter pe;

    private GameObject spotLightObject;
    private GameObject directionalLightObject;
    private SkyGradient skyGradient;

    private Debug debugger;

    public void init() {
        super.init();

//        Aero.graphics.currentRenderingState = RenderingState.SIMPLE;

        Material floorMat = new Material();
        Texture.load("grass", Aero.files.internal("renderTest/textures/grass.png"));
        Texture.load("grass_normal", Aero.files.internal("renderTest/textures/grass_normal.png"));
        floorMat.setDiffuseTexture(Texture.get("grass"));
        floorMat.setNormalTexture(Texture.get("grass_normal"));
        floorMat.setTextureRepeat(50);
        floorMat.setSpecularIntensity(0);

        GameObject floor = new GameObject();
        floor.addComponent(new MeshRenderer(Mesh.get("cube"), floorMat));
        floor.getTransform().setPosition(new Vector3(0, -1, 0));
        floor.getTransform().setScale(new Vector3(150, 1, 150));
        addObject(floor);

        /*PhysicsEngine physicsEngine = new PhysicsEngine();

        physicsEngine.addPhysicsObject(new PhysicsObject(new BoundingSphere(new Vector3(-2, 1, -2.1f), 1), new Vector3(0.4f, 0.1f, 0)));
        physicsEngine.addPhysicsObject(new PhysicsObject(new BoundingSphere(new Vector3(2, 1, -3.9f), 1), new Vector3(-0.4f, 0.1f, 0)));

        PhysicsEngineComponent pec = new PhysicsEngineComponent(physicsEngine);

        for (int i = 0; i < pec.getPhysicsEngine().getObjects().size(); i++) {
            Material axeMat = new Material();
            GameObject sphere = new GameObject();
            sphere.addComponent(new PhysicsObjectComponent(pec.getPhysicsEngine().getObjects().get(i)));
            sphere.addComponent(new MeshRenderer(Mesh.get("sphere.obj"), axeMat));
            sphere.getTransform().setScale(new Vector3(1f, 1f, 1f));
            addObject(sphere);
        }

        addObject(new GameObject().addComponent(pec));*/

        addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(0, 0.2f, 0), new Quaternion());
        addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(2f, 0.2f, 0), new Quaternion(new Vector3(0, 1, 0), (float)Math.toRadians(45)));
        addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1f, 1.7f, 0), new Quaternion(new Vector3(0, 1, 0), (float)Math.toRadians(-45)));
        addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(-3, 0.2f, 6), new Quaternion(new Vector3(0, 1, 0), (float)Math.toRadians(20)));

        addObject("tent.obj", "tent.png", "tent_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1, -0.5f, 11), new Quaternion());
        addObject("bedroll.obj", "bedroll.png", "bedroll_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1, -0.4f, 12), new Quaternion());
        addObject("log.obj", "log.png", "log_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(4.5f, -0.2f, 4.5f), new Quaternion(new Vector3(0, 1, 0), (float)Math.toRadians(-20)));
        addObject("stump.obj", "stump.png", "stump_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(4f, -0.5f, 2.5f), new Quaternion());
        addObject("firepit.obj", "firepit.png", "firepit_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(0f, -0.4f, 4f), new Quaternion());

        addObject("wagon.obj", "wagon.png", "wagon_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(-3.5f, -0.7f, 4f), new Quaternion(new Vector3(0, 0, 1), (float)Math.toRadians(-20)));
        addObject("wheel.obj", "wheel.png", "wheel_normal.png", new Vector3(0.3f, 0.3f, 0.3f), new Vector3(-4.7f, 0.7f, -0.9f), new Quaternion(new Vector3(0, 0, 1), (float)Math.toRadians(-20)));

        addObject("small_axe.obj", "small_axe.png", "small_axe_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(4f, 1f, 2.5f), new Quaternion(new Vector3(1, 0, 0), (float)Math.toRadians(-135)));
        addObject("large_sword.obj", "large_sword.png", "large_sword_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1.2f, -0.1f, 0.85f), new Quaternion((float)Math.toRadians(20), (float)Math.toRadians(-20), 0));
        addObject("medium_sword.obj", "medium_sword.png", "medium_sword_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1.1f, -0.45f, 1f), new Quaternion((float)Math.toRadians(45), (float)Math.toRadians(90), (float)Math.toRadians(90)));
        addObject("large_shield.obj", "large_shield.png", "large_shield_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(0.8f, 0.2f, 1f), new Quaternion((float)Math.toRadians(-90), (float)Math.toRadians(20), 0));

        Camera camera = new Camera(new Matrix4().initPerspective((float) Math.toRadians(91),
                (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f));
        cameraObject = new GameObject().addComponent(new FreeLook(0.3f)).addComponent(new FreeMove(5)).addComponent(camera);
        cameraObject.getTransform().setPosition(new Vector3(0, 2, -7));
        addObject(cameraObject);

        fire = new PointLight(new Vector3(0.61f, 0.16f, 0), 1, new Attenuation(0, 0, 0.2f), 0);
        GameObject fireObject = new GameObject();
        fireObject.addComponent(fire);
        fireObject.getTransform().setPosition(new Vector3(0f, 0.1f, 4f));

        directionalLightObject = new GameObject();
        DirectionalLight directionalLight = new DirectionalLight(new Vector3(1, 1, 1), 0.5f, 4096, 80);
        directionalLightObject.addComponent(directionalLight);
        directionalLightObject.addComponent(new DayNightCycle(directionalLight, 290, 1));
//        directionalLightObject.getTransform().setRotation(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(-120)));


        skyGradient = new SkyGradient(directionalLightObject.getTransform().getTransformedRot().getForward());
        setSkyGradient(skyGradient);

        spotLightObject = new GameObject();
        spotLightObject.addComponent(new SpotLight(new Vector3(1, 1, 1), 1f, new Attenuation(0, 0, 0.2f), 8, (float)Math.toRadians(91)));

        Texture.load("smokeparticle", Aero.files.internal("default/textures/smokeparticle.png"));

        GameObject particleEmitterObject = new GameObject();
        pe = new ParticleEmitterBuilder().setLookAtCamera(true).setSpawningRate(0.1f).setInitialVelocity(new Vector3(-0.02f, 0, -0.02f)).setMaterial(new Material(Texture.get("smokeparticle"))).createParticleEmitter();
        particleEmitterObject.addComponent(pe);
        particleEmitterObject.getTransform().setPosition(fireObject.getTransform().getPosition());

//        addObject(particleEmitterObject);

        addObject(fireObject);
        addObject(directionalLightObject);
//        addObject(spotLightObject);

        debugger = new Debug();
    }

    @Override
    public void input(float delta) {
        super.input(delta);

        if(Aero.input.getKeyDown(Input.KEY_F1)){
            Graphics.FXAA_QUALITY = 0;
        }else if(Aero.input.getKeyDown(Input.KEY_F2)){
            Graphics.FXAA_QUALITY = 4;
        }else if(Aero.input.getKeyDown(Input.KEY_F3)){
            Graphics.FXAA_QUALITY = 8;
        }else if(Aero.input.getKeyDown(Input.KEY_F4)){
            Graphics.FXAA_QUALITY = 16;
        }

        if(Aero.input.getKeyDown(Input.KEY_ESCAPE)){
            if(!Aero.input.isMouseGrabbed()){
                System.exit(0);
            }
        }

        if(Aero.input.getKeyDown(Input.KEY_E)){
            addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), cameraObject.getTransform().getPosition(), new Quaternion());
        }

        debugger.input(delta);
    }


    @Override
    public void update(float delta) {
        super.update(delta);

        skyGradient.update(directionalLightObject.getTransform().getTransformedRot().getForward());

        fire.setIntensity(MathUtils.approach(MathUtils.random(0.5f, 1), fire.getIntensity(), delta * 7));

        debugger.update(delta);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        debugger.draw();
    }

    @Override
    public void resized(int width, int height) {
        super.resized(width, height);
    }

    public void addObject(String mesh, String diff, String normal, Vector3 scale, Vector3 pos, Quaternion rotation){
        GameObject object = new GameObject();
        Material mat = new Material();
        Texture.load(diff, Aero.files.internal("renderTest/textures/" + diff));
        Texture.load(normal, Aero.files.internal("renderTest/textures/" + normal));
        mat.setDiffuseTexture(Texture.get(diff));
        mat.setNormalTexture(Texture.get(normal));
        object.addComponent(new MeshRenderer(Mesh.load(mesh, Aero.files.internal("renderTest/models/" + mesh)), mat));
        object.getTransform().setScale(scale);
        object.getTransform().setPosition(pos);
        object.getTransform().setRotation(rotation);

        addObject(object);
    }

}
