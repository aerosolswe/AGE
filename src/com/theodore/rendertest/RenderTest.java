package com.theodore.rendertest;

import com.theodore.aero.components.*;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Input;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.g2d.BitmapFont;
import com.theodore.aero.graphics.g3d.Attenuation;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.physics.PhysicsEngine;
import com.theodore.aero.physics.PhysicsObject;

public class RenderTest extends Screen {

    private Camera orthoCam;

    private GameObject cameraObject;

    private GameObject directionalLightObject;

    private GameObject spotLightObject;

    private BitmapFont fpsText;

    public void init() {
        Aero.addResourceDirectory("renderTest/");

        Material floorMat = new Material();
        floorMat.setDiffuseTexture(Texture.get("grass.png"));
        floorMat.setNormalTexture(Texture.get("grass_normal.png"));
        floorMat.setTextureRepeat(50);

        GameObject floor = new GameObject();
        floor.addComponent(new MeshRenderer(Mesh.get("cube"), floorMat));
        floor.getTransform().setPosition(new Vector3(0, -1, 0));
        floor.getTransform().setScale(new Vector3(150, 1, 150));
        addObject(floor);

        /*PhysicsEngine physicsEngine = new PhysicsEngine();

        physicsEngine.addPhysicsObject(new PhysicsObject(new Vector3(0, 1, 3), new Vector3(-0.1f, 0.4f, 0)));
        physicsEngine.addPhysicsObject(new PhysicsObject(new Vector3(0, 1, 3), new Vector3(0.1f, 0.4f, 0)));

        PhysicsEngineComponent pec = new PhysicsEngineComponent(physicsEngine);

        for (int i = 0; i < pec.getPhysicsEngine().getObjects().size(); i++) {
            Material axeMat = new Material();
            axeMat.setDiffuseTexture(Texture.get("axe/diff.png"));
            GameObject axe = new GameObject();
            axe.addComponent(new PhysicsObjectComponent(pec.getPhysicsEngine().getObjects().get(i)));
            axe.addComponent(new MeshRenderer(Mesh.get("axe/ax.obj"), axeMat));
            axe.getTransform().setScale(new Vector3(0.1f, 0.1f, 0.1f));
            addObject(axe);
        }

        addObject(new GameObject().addComponent(pec));*/

        for(int i = 0; i < 4; i++){

            if(i == 0){
                addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(0, 0.2f, 0), new Quaternion());
            }else if(i == 1){
                addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(2f, 0.2f, 0), new Quaternion(new Vector3(0, 1, 0), (float)Math.toRadians(45)));
            }else if(i == 2){
                addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1f, 1.7f, 0), new Quaternion(new Vector3(0, 1, 0), (float)Math.toRadians(-45)));
            }else if(i == 3){
                addObject("crate.obj", "crate.png", "crate_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(-3, 0.2f, 6), new Quaternion(new Vector3(0, 1, 0), (float)Math.toRadians(20)));
            }
        }

        addObject("tent.obj", "tent.png", "tent_normal.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1, -0.5f, 11), new Quaternion());
        addObject("bedroll.obj", "bedroll.png", "bedroll.png", new Vector3(0.2f, 0.2f, 0.2f), new Vector3(1, -0.4f, 12), new Quaternion());
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
        cameraObject = new GameObject().addComponent(new FreeLook(0.5f)).addComponent(new FreeMove(5)).addComponent(camera);
        cameraObject.getTransform().setPosition(new Vector3(0, 2, -5));
        addObject(cameraObject);

        directionalLightObject = new GameObject();
        directionalLightObject.addComponent(new DirectionalLight(new Vector3(0.75f, 0.74f, 0.67f), 0.9f, 10, 40));
        directionalLightObject.getTransform().setRotation(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(-120)));

        spotLightObject = new GameObject();
        spotLightObject.addComponent(new SpotLight(new Vector3(1, 1, 1), 1f, new Attenuation(0, 0, 0.2f), 10, (float)Math.toRadians(91)));

        addObject(directionalLightObject);
        addObject(spotLightObject);


        orthoCam = new Camera(new Matrix4().initOrthographic(0, 976, 0, 518, -1, 1));
        GameObject orthoCameraObject = new GameObject().addComponent(orthoCam);

        fpsText = new BitmapFont("test");
        fpsText.setWidth(32);
        fpsText.setHeight(32);
        fpsText.setX(50);
        fpsText.setY(10);
    }

    float angle = -45;

    @Override
    public void input(float delta) {
        super.input(delta);

        if (Aero.input.getKey(Input.KEY_UP)) {
            angle += 50 * delta;
            System.out.println(angle);
        } else if (Aero.input.getKey(Input.KEY_DOWN)) {
            angle -= 50 * delta;
            System.out.println(angle);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(Aero.input.getKeyDown(Input.KEY_E)){
            spotLightObject.getTransform().setPosition(cameraObject.getTransform().getPosition());
            spotLightObject.getTransform().setRotation(cameraObject.getTransform().getRotation());
        }

        directionalLightObject.getTransform().setRotation(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(angle)));
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        fpsText.setText("FPS: " + Aero.graphics.getCurrentFps());
        fpsText.draw(orthoCam);

    }

    public void addObject(String mesh, String diff, String normal, Vector3 scale, Vector3 pos, Quaternion rotation){
        GameObject object = new GameObject();
        Material mat = new Material();
        mat.setDiffuseTexture(Texture.get(diff));
        mat.setNormalTexture(Texture.get(normal));
        object.addComponent(new MeshRenderer(Mesh.get(mesh), mat));
        object.getTransform().setScale(scale);
        object.getTransform().setPosition(pos);
        object.getTransform().setRotation(rotation);

        addObject(object);
    }

    public static void main(String[] args) {
        new Aero(1280, 720, "Render test", 120, false, 5).start(new RenderTest());
    }
}
