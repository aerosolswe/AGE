package com.theodore.tests;

import com.theodore.aero.components.*;
import com.theodore.aero.core.*;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.filters.DisplayFilter;
import com.theodore.aero.graphics.filters.FxaaFilter;
import com.theodore.aero.graphics.g2d.gui.Button;
import com.theodore.aero.graphics.g2d.gui.Gui;
import com.theodore.aero.graphics.g2d.gui.Label;
import com.theodore.aero.graphics.g3d.Attenuation;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.sky.Sky;
import com.theodore.aero.graphics.sky.SkyAtmosphere;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.input.KeyCallback;
import com.theodore.aero.input.Mapping;
import com.theodore.aero.math.*;
import com.theodore.aero.graphics.terrain.Terrain;

import java.util.ArrayList;

public class RenderTest extends Screen {

    private static Mesh cubeMesh = new Mesh("cube");
    private static Material cubeMat = new Material();
    private static MeshRenderer meshRenderer = new MeshRenderer(cubeMesh, cubeMat);

    private GameObject cameraObject;
    private Camera camera;

    private GameObject directionalLightObject;
    private Sky sky;
    private Terrain terrain;

    private Debug debugger;
    private Gui gui;
    private Button button;

    private GameObject fxaaObject;

    private GameObject plo;

    public void init() {
        super.init();

        Material material = new Material(
                new Texture("grass_diffuse", Aero.files.internal("default/textures/terrain/grass_diffuse.png")),
                new Texture("grass_normal", Aero.files.internal("default/textures/terrain/grass_normal.png"))
        );

        material.setTexture("bdiffuse", new Texture("dirt_diffuse", Aero.files.internal("default/textures/terrain/dirt_diffuse.png")));
        material.setTexture("bnormal", new Texture("dirt_normal", Aero.files.internal("default/textures/terrain/dirt_normal.png")));
        material.setTexture("gdiffuse", new Texture("rockygrass_diffuse", Aero.files.internal("default/textures/terrain/rockygrass_diffuse.png")));
        material.setTexture("gnormal", new Texture("rockygrass_normal", Aero.files.internal("default/textures/terrain/rockygrass_normal.png")));
        material.setTexture("rdiffuse", new Texture("rock_diffuse", Aero.files.internal("default/textures/terrain/rock_diffuse.png")));
        material.setTexture("rnormal", new Texture("rock_normal", Aero.files.internal("default/textures/terrain/rock_normal.png")));
        material.setTexture("blendMap", new Texture("blendMap", Aero.files.internal("default/textures/terrain/blendmap.png")));

        material.setInteger("textureRepeat", 100);


        camera = new Camera(new Matrix4().initPerspective((float) Math.toRadians(91),
                (float) Aero.window.getWidth() / (float) Aero.window.getHeight(), 0.1f, 500));
        cameraObject = new GameObject().addComponent(new FreeLook(0.3f)).addComponent(new FreeMove(5f)).addComponent(camera);
        cameraObject.getTransform().setPosition(new Vector3(0, 2, 0));
        addObject(cameraObject);

        directionalLightObject = new GameObject();
        DirectionalLight directionalLight = new DirectionalLight(new Vector3(0.752f, 0.749f, 0.678f), 1, 2048, 100, 2f);
        directionalLightObject.addComponent(directionalLight);
        directionalLightObject.addComponent(new DayNightCycle(10));

        addObject(directionalLightObject);
        sky = new SkyAtmosphere();

        addObject(new GameObject().addComponent(new SkyComponent(sky)));
        fxaaObject = new GameObject().addComponent(new FilterComponent(new DisplayFilter()));
//        addObject(fxaaObject);

        terrain = new Terrain(material).heightMap(Aero.files.internal("default/textures/terrain/heightmap.png"));
        GameObject terrainObject = new GameObject().addComponent(terrain);
        addObject(terrainObject);
        terrainObject.getTransform().setPosition(new Vector3(-Terrain.SIZE / 2, 0, -Terrain.SIZE / 2));

        debugger = new Debug();

        gui = new Gui();
//        gui.addWidget(new Label("Testing if I can press on TEXT", 200, 200, 20, 40, 28));
//        gui.addWidget(button = new Button(new Texture("button_up", Aero.files.internal("default/textures/gui/button_up.png")), new Texture("button_down", Aero.files.internal("default/textures/gui/button_down.png")), 200, 200, 200, 100));

        Aero.inputManager.addKeyMap("quit", Input.Keys.KEY_ESCAPE);
        Aero.inputManager.addKeyMap("spawnBox", Input.Keys.KEY_E);

    }

    @Override
    public void input(float delta) {
        super.input(delta);

        debugger.input(delta);
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        sky.setVector3("lightDir", directionalLightObject.getTransform().getTransformedRot().getForward());

        Mapping mapping = Aero.inputManager.getMapping("quit");
        if (mapping.isReleased()) {
            System.exit(0);
        }

        Mapping spawnBox = Aero.inputManager.getMapping("spawnBox");
        if (spawnBox.isReleased()) {
            GameObject boxObject = new GameObject();
            boxObject.addComponent(new MeshRenderer(new Mesh("cube"), new Material()));
            boxObject.getTransform().setPosition(cameraObject.getTransform().getPosition().add(cameraObject.getTransform().getRotation().getForward().mul(2)));
            boxObject.getTransform().setRotation(cameraObject.getTransform().getRotation());
            addObject(boxObject);
        }

        cameraObject.getTransform().getPosition().setY(terrain.getHeightOfPosition(cameraObject.getTransform().getPosition().x, cameraObject.getTransform().getPosition().z) + 1.7f);

        debugger.update(delta);
        gui.update(delta);

    }


    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        debugger.draw();
        gui.draw(graphics);
    }

    @Override
    public void resized(int width, int height) {
        super.resized(width, height);
        gui.setSize(width, height);
    }

    @Override
    public void dispose() {
    }
}
