package com.theodore.tests;

import com.theodore.aero.components.*;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Debug;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Input;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.filters.FxaaFilter;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Primitives;
import com.theodore.aero.graphics.sky.SkyAtmosphere;
import com.theodore.aero.graphics.sky.SkyBox;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.sky.SkyGradient;
import com.theodore.aero.math.*;

public class RenderTest extends Screen {

    private GameObject cameraObject;

    private GameObject directionalLightObject;

    private Debug debugger;

    private SkyAtmosphere sky;

    public void init() {
        super.init();

//        Aero.graphics.currentRenderingState = RenderingState.SIMPLE;
//        Aero.graphics.setVector3("ambientLight", new Vector3(1, 1, 1));


        Material floorMat = new Material(
                new Texture("cobble_diffuse", Aero.files.internal("default/textures/test/cobble_diffuse.png")),
                new Texture("cobble_normal", Aero.files.internal("default/textures/test/cobble_normal.png")),
                new Texture("cobble_disp", Aero.files.internal("default/textures/test/cobble_disp.png")),  0.04f, -1.0f
        );

        floorMat.setInteger("textureRepeat", 75);
        floorMat.setFloat("specularIntensity", 0.5f);

        GameObject floor = new GameObject();

        floor.addComponent(new MeshRenderer(new Mesh("plane"), floorMat));
        floor.getTransform().setPosition(new Vector3(0, 0, 0));
        floor.getTransform().setScale(new Vector3(150, 1, 150));
        addObject(floor);

        Camera camera = new Camera(new Matrix4().initPerspective((float) Math.toRadians(91),
                (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f));
        cameraObject = new GameObject().addComponent(new FreeLook(0.3f)).addComponent(new FreeMove(5f)).addComponent(camera);
        cameraObject.getTransform().setPosition(new Vector3(0, 2, 0));
        addObject(cameraObject);

        directionalLightObject = new GameObject();
        DirectionalLight directionalLight = new DirectionalLight(new Vector3(1, 1, 1), 0.7f, 2048, 60);
        directionalLightObject.addComponent(directionalLight);
        directionalLightObject.addComponent(new DayNightCycle(directionalLight, 300, 0));
//        directionalLightObject.getTransform().setRotation(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(-120)));

        addObject(directionalLightObject);

        sky = new SkyAtmosphere();

        addObject(new GameObject().addComponent(new SkyComponent(sky)));

        /*Mesh mesh = new Mesh("cube");
        Material material = new Material();

        for(int i = 0; i < 500; i++){
            GameObject cube = new GameObject();

            cube.addComponent(new MeshRenderer(mesh, material));

            Vector3 spawnPos = new Vector3(MathUtils.random(0, 50), MathUtils.random(0, 50), MathUtils.random(0, 50));
            Quaternion spawnRot = new Quaternion(MathUtils.random(0, 360), MathUtils.random(0, 360), MathUtils.random(0, 360));

            cube.getTransform().setPosition(spawnPos);
            cube.getTransform().setRotation(spawnRot);
            addObject(cube);
        }*/

        addObject(new GameObject().addComponent(new FilterComponent(new FxaaFilter(16))));

        debugger = new Debug();

    }

    @Override
    public void input(float delta) {
        super.input(delta);

        if (Aero.input.getKeyDown(Input.KEY_F1)) {
            Aero.graphics.setFloat("fxaaSpanMax", 0);
        } else if (Aero.input.getKeyDown(Input.KEY_F2)) {
            Aero.graphics.setFloat("fxaaSpanMax", 4);
        } else if (Aero.input.getKeyDown(Input.KEY_F3)) {
            Aero.graphics.setFloat("fxaaSpanMax", 8);
        } else if (Aero.input.getKeyDown(Input.KEY_F4)) {
            Aero.graphics.setFloat("fxaaSpanMax", 16);
        }

        if (Aero.input.getKeyDown(Input.KEY_ESCAPE)) {
            if (!Aero.input.isMouseGrabbed()) {
                System.exit(0);
            }
        }


        if (Aero.input.getKeyDown(Input.KEY_R)) {
            GameObject cube = new GameObject();

            cube.addComponent(new MeshRenderer(new Mesh("cube"), new Material()));

            Vector3 spawnPos = cameraObject.getTransform().getTransformedPos().add(cameraObject.getTransform().getRotation().getForward().mul(3));
            Quaternion spawnRot = cameraObject.getTransform().getTransformedRot();

            cube.getTransform().setPosition(spawnPos);
            cube.getTransform().setRotation(spawnRot);
            addObject(cube);
        }

        debugger.input(delta);
    }


    @Override
    public void update(float delta) {
        super.update(delta);



        debugger.update(delta);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        sky.setVector3("lightDir", directionalLightObject.getTransform().getTransformedRot().getForward());
        debugger.render();
    }

    @Override
    public void resized(int width, int height) {
        super.resized(width, height);
    }

}
