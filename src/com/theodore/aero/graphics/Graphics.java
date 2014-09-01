package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.g2d.gui.Gui;
import com.theodore.aero.graphics.g3d.lighting.BaseLight;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.graphics.shaders.forward.ForwardAmbientShader;
import com.theodore.aero.graphics.g2d.Sprite;
import com.theodore.aero.graphics.g2d.gui.Widget;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {

    private int shadowSize = 1024;
    private int maxFps = 3000;

    private Vector3 ambientLight;

    private RenderingState currentRenderingState = RenderingState.FORWARD;

    private ArrayList<GameObject> objects;
    private ArrayList<Sprite> sprites;
    private Gui gui;

    private Camera camera;

    private int FramesPerSeconds;

    /**
     * Temporary RTT stuff
     */
    private Camera altCam;
    private FrameBuffer tempBuffer;
    private GameObject go;

    public void initGraphics() {
        Aero.graphicsUtil.init();
    }

    public void init() {
        ambientLight = new Vector3(0.08f, 0.09f, 0.1f);
        Aero.graphicsUtil.setClearColor(ambientLight.x, ambientLight.y, ambientLight.z, 0);

        camera = new Camera();

        Mesh.generatePrimitives();
        Material.generateDefaultMaterials();
        Transform.setProjection(Transform.getPerspectiveMatrix());

        // temp init
        int width = Window.getWidth();
        int height = Window.getHeight();
        int dataSize = width * height * 4;

        ByteBuffer buffer = Util.createByteBuffer(dataSize);

        Texture texture = new Texture(width, height, buffer, GL_TEXTURE_2D, GL_NEAREST, GL_RGBA, GL_RGBA, false, false);

        altCam = new Camera();
        altCam.setPosition(new Vector3(0, 100, -1));
        altCam.setRotation(new Quaternion(new Vector3(0, 1, -1)));

        go = new GameObject(Mesh.get("plane"), new Material(texture));
        go.getTransform().rotate(new Vector3(-90, 0, 0));
        go.getTransform().setScale(2, 1, 1);

        tempBuffer = new FrameBuffer(width, height, new Texture[]{texture}, new int[]{GL30.GL_COLOR_ATTACHMENT0});
    }

    public void fullRender() {
        Aero.graphicsUtil.clearColorAndDepth();

        Transform.calcView();
        objects = Aero.getActiveScreen().getGameObjects();
        sprites = Aero.getActiveScreen().getSprites();
        gui = Aero.getActiveScreen().getGui();

        switch (currentRenderingState) {
            case SIMPLE:
                simpleRenderingPass();
                break;
            case FORWARD:
                forwardRenderingPass();
                break;
            case DEFERRED:
                deferredRenderingPass();
                break;
        }

        orthographicPass();
    }

    private void forwardRenderingPass() {
        Transform.setProjection(Transform.getPerspectiveMatrix());
        Shader ambientShader = ForwardAmbientShader.getInstance();

        for (GameObject object : objects) {
            ambientShader.bind();
            ambientShader.updateUniforms(object.getTransform().calcModel(), object.getTransform().calcMVP(), object.getMaterial());
            object.render();
        }


        for (GameObject object : objects) {
            for (BaseLight light : Aero.getActiveScreen().getLights()) {
                if(light.getShadowMap() != null){
                    light.render(objects);
                }
                Aero.getActiveScreen().setActiveLight(light);

                Aero.graphicsUtil.enableBlending(GL_ONE, GL_ONE);
                Aero.graphicsUtil.setDepthMask(false);
                Aero.graphicsUtil.setDepthFunc(GL_EQUAL);

                if(light.getShadowMap() != null){
                    light.apply(object);
                }
                light.getShader().bind();
                light.getShader().updateUniforms(object.getTransform().calcModel(), object.getTransform().calcMVP(), object.getMaterial());
                object.render();

                Aero.graphicsUtil.setDepthFunc(GL_LESS);
                Aero.graphicsUtil.setDepthMask(true);
                Aero.graphicsUtil.disableBlending();

            }
        }


    }

    private void deferredRenderingPass() {
        /** Render scene to framebuffer */
        tempBuffer.bindAsRenderTarget();
        Aero.graphicsUtil.clearColorAndDepth();

        simpleRenderingPass();

        /** Render quad with framebuffer texture */ /** WORK IN PROGRESS!! NOT COMPLETE */
        FrameBuffer.unbindRenderTarget();

        Aero.graphicsUtil.setClearColor(0.1f, 0.1f, 0.5f, 1);
        Aero.graphicsUtil.clearColorAndDepth();

        Camera temp = Transform.getCamera();
        Transform.setCamera(altCam);

        Transform.calcView();
        Transform.setProjection(Transform.getPerspectiveMatrix());

        BasicShader shader = BasicShader.getInstance();
        shader.bind();
        shader.updateUniforms(go.getTransform().calcModel(), go.getTransform().calcMVP(), go.getMaterial());
        go.render();

        Transform.setCamera(temp);
    }

    private void orthographicPass() {
        Transform.pushCamera(camera);
        Transform.pushProjection(Transform.getOrthographicMatrix(0, Window.getWidth(), 0, Window.getHeight(), 1, -1));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);

        for (Sprite sprite : sprites) {
            sprite.draw();
        }

        for (Widget widget : gui.getWidgets()) {
            if (widget.isVisible())
                widget.draw();
        }

//        Aero.graphicsUtil.clearColorAndDepth();
//        image.draw();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        Transform.popCamera();
        Transform.popProjection();
    }

    private void simpleRenderingPass() {
        Transform.setProjection(Transform.getPerspectiveMatrix());
        BasicShader shader = BasicShader.getInstance();
        for (GameObject object : objects) {
            shader.bind();
            shader.updateUniforms(object.getTransform().calcModel(), object.getTransform().calcMVP(), object.getMaterial());
            object.render();
        }
    }

    public RenderingState getCurrentRenderingState() {
        return currentRenderingState;
    }

    public void setCurrentRenderingState(RenderingState currentRenderingState) {
        this.currentRenderingState = currentRenderingState;
    }

    public void setSimpleRendering() {
        setCurrentRenderingState(RenderingState.SIMPLE);
    }

    public void setForwardRendering() {
        setCurrentRenderingState(RenderingState.FORWARD);
    }

    public void setDeferredRendering() {
        setCurrentRenderingState(RenderingState.DEFERRED);
    }

    public Vector3 getAmbientLight() {
        return ambientLight;
    }

    public void setShadowMapSize(int size) {
        shadowSize = size;
    }

    public int getShadowSize() {
        return shadowSize;
    }

    public void setAmbientLight(Vector3 ambientLight) {
        this.ambientLight = ambientLight;
        Aero.graphicsUtil.setClearColor(ambientLight.x, ambientLight.y, ambientLight.z, 0);
    }

    public int getMaxFps() {
        return maxFps;
    }

    public void setMaxFps(int maxFps) {
        this.maxFps = maxFps;
    }

    public int getFramesPerSeconds() {
        return FramesPerSeconds;
    }

    public void setFramesPerSeconds(int framesPerSeconds) {
        FramesPerSeconds = framesPerSeconds;
    }
}
