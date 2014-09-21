package com.theodore.tools.modelviewer;

import com.theodore.aero.components.*;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Debug;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Input;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.g2d.gui.Gui;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;
import org.lwjgl.LWJGLException;



public class ModelViewer extends Screen {

    private GameObject cameraObject;
    private GameObject modelObject;

    private Debug debugger;

    private Gui gui;

    @Override
    public void init() {
        super.init();

        Aero.graphics.setAmbientLight(new Vector3(0.8f, 0.8f, 0.8f));

        Aero.graphics.currentRenderingState = RenderingState.SIMPLE;

        Camera camera = new Camera(new Matrix4().initPerspective((float) Math.toRadians(91),
                (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f));
        cameraObject = new GameObject().addComponent(camera);
        cameraObject.getTransform().setPosition(new Vector3(0, 5, -7));
        addObject(cameraObject);

        modelObject = new GameObject();
        modelObject.addComponent(new FreeLook(0.1f));
        addObject(modelObject);

        gui = new Gui(1280, 720);

        debugger = new Debug();
    }

    @Override
    public void input(float delta) {
        super.input(delta);

        if(Aero.input.getKeyDown(Input.KEY_ESCAPE)){
            if(!Aero.input.isMouseGrabbed()){
                System.exit(0);
            }
        }

        if(Aero.input.getKeyDown(Input.KEY_F1)){
            String m = "res/modelviewer/models/";
            String t = "res/modelviewer/textures/";
            modelObject.addComponent(new MeshRenderer(
                    new Mesh[]{ Mesh.get(m + "o_backpack.obj"), Mesh.get(m+"o_body.obj"), Mesh.get(m+"o_armshead.obj") },
                    new Material[]{ new Material(Texture.get(t+"o_backpack.png"), Texture.get(t+"o_backpack_normal.png")), new Material(Texture.get(t+"o_body.png"), Texture.get(t+"o_body_normal.png")), new Material(Texture.get(t+"o_headarms.png"), Texture.get(t+"o_headarms_normal.png")) }
            ));
        }

        debugger.input(delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        debugger.update(delta);
        gui.update(delta);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        debugger.draw();
        gui.draw(graphics);
    }
}
