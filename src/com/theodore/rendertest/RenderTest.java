package com.theodore.rendertest;

import com.theodore.aero.Components.*;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Screen;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.graphics.g2d.BitmapFont;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.physics.*;

public class RenderTest extends Screen {

    private GameObject cameraObject;
    private Camera camera;
    private GameObject orthoCameraObject;
    private Camera orthoCam;
    private GameObject axe;

    private BitmapFont fpsText;

    public void init() {
        Material floorMat = new Material();
        floorMat.setTextureRepeat(50);
        GameObject floor = new GameObject();
        floor.addComponent(new MeshRenderer(Mesh.get("cube"), floorMat));
        floor.getTransform().setPosition(new Vector3(0, -1, 0));
        floor.getTransform().setScale(new Vector3(50, 1, 50));
        addObject(floor);

        PhysicsEngine physicsEngine = new PhysicsEngine();

        physicsEngine.addPhysicsObject(new PhysicsObject(new Vector3(0, 1, 3), new Vector3(-0.1f, 0.4f, 0)));
        physicsEngine.addPhysicsObject(new PhysicsObject(new Vector3(0, 1, 3), new Vector3(0.1f, 0.4f, 0)));

        PhysicsEngineComponent pec = new PhysicsEngineComponent(physicsEngine);

        for(int i = 0; i < pec.getPhysicsEngine().getObjects().size(); i++){
            Material axeMat = new Material();
            axeMat.setDiffuseTexture(Texture.get("axe/diff.png"));
            axe = new GameObject();
            axe.addComponent(new PhysicsObjectComponent(pec.getPhysicsEngine().getObjects().get(i)));
            axe.addComponent(new MeshRenderer(Mesh.get("axe/ax.obj"), axeMat));
            axe.getTransform().setScale(new Vector3(0.1f, 0.1f, 0.1f));
//          axe.getTransform().getPosition().set(0, 1, 3);
            addObject(axe);
        }

        addObject(new GameObject().addComponent(pec));

        GameObject cubeObject = new GameObject();
        cubeObject.addComponent(new MeshRenderer(Mesh.get("cube"), new Material()));
        cubeObject.getTransform().setPosition(new Vector3(2, 1, 2));
        addObject(cubeObject);

        camera = new Camera(new Matrix4().initPerspective((float) Math.toRadians(91),
                (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f));
        cameraObject = new GameObject().addComponent(new FreeLook(0.5f)).addComponent(new FreeMove(5)).addComponent(camera);
        cameraObject.getTransform().setPosition(new Vector3(0, 2, -5));
        addObject(cameraObject);

        GameObject directionalLightObject = new GameObject();
        directionalLightObject.addComponent(new DirectionalLight(new Vector3(1, 1, 1), 0.8f));
        directionalLightObject.getTransform().setRotation(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(-120)));
        addObject(directionalLightObject);

        orthoCam = new Camera(new Matrix4().initOrthographic(0, 976, 0, 518, -1, 1));
        orthoCameraObject = new GameObject().addComponent(orthoCam);

        fpsText = new BitmapFont("test");
        fpsText.setWidth(32);
        fpsText.setHeight(32);
        fpsText.setX(50);
        fpsText.setY(10);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        fpsText.setText("FPS: " + Aero.graphics.getCurrentFps());
        fpsText.draw(orthoCam);

    }

    public static void main(String[] args) {
        new Aero(1280, 720, "Render test", 120, false, 5).start(new RenderTest());
    }
}
