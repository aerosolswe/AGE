package com.theodore.aero.graphics;

import com.theodore.aero.Components.BaseLight;
import com.theodore.aero.Components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.Mesh;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.graphics.shaders.forward.ForwardAmbientShader;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {

    private int shadowSize = 1024;
    private int maxFps = 3000;
    private int currentFps = 0;

    private Vector3 ambientLight;

    public RenderingState currentRenderingState = RenderingState.FORWARD;

    private ArrayList<BaseLight> lights;

    private BaseLight activeLight;

    private Shader ambientShader;
    private Shader basicShader;

    private Camera mainCamera;

    public void initGraphics() {
        Aero.graphicsUtil.init();
    }

    public void init() {
        ambientLight = new Vector3(0.08f, 0.09f, 0.1f);
        Aero.graphicsUtil.setClearColor(ambientLight.x, ambientLight.y, ambientLight.z, 0);

        lights = new ArrayList<BaseLight>();

        ambientShader = ForwardAmbientShader.getInstance();
        basicShader = BasicShader.getInstance();

        Mesh.generatePrimitives();
        Material.generateDefaultMaterials();
    }

    public void fullRender(GameObject object) {
        if (getMainCamera() == null) {
            System.err.println("Error! Main camera not found. This is very very big bug, and game will crash.");
            System.exit(1);
        }
        Aero.graphicsUtil.clearColorAndDepth();

        switch (currentRenderingState) {
            case SIMPLE:
                simpleRenderingPass(object);
                break;
            case FORWARD:
                forwardRenderingPass(object);
                break;
            case DEFERRED:
                break;
        }
    }


    private void simpleRenderingPass(GameObject object) {
        object.renderAll(basicShader, this);
    }

    private void forwardRenderingPass(GameObject object) {
        object.renderAll(ambientShader, this);

        Aero.graphicsUtil.enableBlending(GL_ONE, GL_ONE);
        Aero.graphicsUtil.setDepthMask(false);
        Aero.graphicsUtil.setDepthFunc(GL_EQUAL);

        for (BaseLight light : lights) {
            if (light.getShadowMap() != null) {
                light.render(object);
            }

            activeLight = light;


            if (light.getShadowMap() != null) {
                light.apply(object);
            }

            object.renderAll(light.getShader(), this);

        }


        Aero.graphicsUtil.setDepthFunc(GL_LESS);
        Aero.graphicsUtil.setDepthMask(true);
        Aero.graphicsUtil.disableBlending();
    }

    public void addCamera(Camera camera) {
        mainCamera = camera;
    }

    public static String GetOpenGLVersion() {
        return glGetString(GL_VERSION);
    }

    public void addLight(BaseLight light) {
        lights.add(light);
    }

    public BaseLight getActiveLight() {
        return activeLight;
    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public void setMainCamera(Camera mainCamera) {
        this.mainCamera = mainCamera;
    }

    public Vector3 getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3 ambientLight) {
        this.ambientLight = ambientLight;
    }

    public int getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    public int getMaxFps() {
        return maxFps;
    }

    public void setMaxFps(int maxFps) {
        this.maxFps = maxFps;
    }

    public int getCurrentFps() {
        return currentFps;
    }

    public void setCurrentFps(int currentFps) {
        this.currentFps = currentFps;
    }
}
