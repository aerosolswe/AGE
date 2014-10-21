package com.theodore.aero.graphics;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.Camera;
import com.theodore.aero.components.MeshRenderer;
import com.theodore.aero.core.*;
import com.theodore.aero.graphics.filters.Filter;
import com.theodore.aero.graphics.filters.ShadowBlurFilter;
import com.theodore.aero.graphics.g3d.*;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.*;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.resourceManagement.MappedValues;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class Graphics extends MappedValues {

    /**
     * Always starts with forward unless stated otherwise
     */
    public RenderingState currentRenderingState = RenderingState.FORWARD;

    /**
     * Profile timer for calculating render time
     */
    private ProfileTimer renderTimer;
    private ProfileTimer syncTimer;


    /**
     * Contains all the light sources
     */
    private ArrayList<Filter> filters;
    private ArrayList<BaseLight> lights;
    private HashMap<String, Integer> samplerMap;

    /**
     * The active light
     */
    private BaseLight activeLight;


    /**
     * Shaders for rendering
     */
    private Shader ambientShader;
    private Shader shadowMapShader;

    /**
     * Main and alternative camera
     */
    private Camera mainCamera;
    private Camera altCamera;

    /**
     * Render to texture
     */
    private Material planeMaterial;
    private GameObject planeObject;

    public void initGraphics() {
        Aero.graphicsUtil.init();
    }

    public void init() {
        samplerMap = new HashMap<String, Integer>();

        filters = new ArrayList<Filter>();
        lights = new ArrayList<BaseLight>();

        samplerMap.put("diffuse", 0);
        samplerMap.put("normalMap", 1);
        samplerMap.put("dispMap", 2);
        samplerMap.put("shadowMap", 3);

        ambientShader = new ForwardAmbientShader();
        shadowMapShader = new ShadowMapShader();

        Mesh.generateBasicMesh();
        Texture.generateBaseTextures();

        setMatrix4("lightMatrix", new Matrix4().initScale(0, 0, 0));
        setMatrix4("biasMatrix", new Matrix4().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4().initTranslation(1.0f, 1.0f, 1.0f)));

        setVector3("ambientLight", new Vector3(0.08f, 0.09f, 0.1f));

        setBoolean("wireframe", false);
        setInteger("fps", 60);

        renderTimer = new ProfileTimer();
        syncTimer = new ProfileTimer();

        initDisplay(Window.getWidth(), Window.getHeight());

        altCamera = new Camera(new Matrix4().initIdentity());
        new GameObject().addComponent(altCamera);

    }

    public void fullRender(GameObject object) {
        switch (currentRenderingState) {
            case SIMPLE:
                simpleRenderingPass(object);
                break;
            case FORWARD:
                forwardRenderingPass(object);
                break;
            case DEFERRED:
                deferredRenderingPass(object);
                break;
        }

        float displayTextureAspect = (float) getTexture("displayTexture").getWidth() / (float) getTexture("displayTexture").getHeight();
        float displayTextureHeightAdditive = displayTextureAspect * getFloat("fxaaAspectDistortion");

        setVector3("inverseFilterTextureSize", new Vector3(1.0f / (float) getTexture("displayTexture").getWidth(),
                1.0f / ((float) getTexture("displayTexture").getHeight() + displayTextureHeightAdditive), 0.0f));

        syncTimer.startInvocation();
        for (Filter filter : filters) {
            filter.renderFilter(this, getTexture("displayTexture"), getTexture("displayTextureRenderTarget"), planeObject, altCamera);
            glDisable(GL_BLEND);
        }
        syncTimer.stopInvocation();
    }

    public void deferredRenderingPass(GameObject object) {

    }

    private void simpleRenderingPass(GameObject object) {
        renderTimer.startInvocation();
        if (filters.size() != 0)
            getTexture("displayTexture").bindAsRenderTarget();
        else
            Window.bindAsRenderTarget();

        Aero.graphicsUtil.setClearColor(0.0f, 0f, 0f, 0f);
        Aero.graphicsUtil.clearColorAndDepth();

        if (getBoolean("wireframe")) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        object.renderAll(ambientShader, this);

        if (getBoolean("wireframe")) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        renderTimer.stopInvocation();
    }


    private void forwardRenderingPass(GameObject object) {
        renderTimer.startInvocation();
        if (filters.size() != 0)
            getTexture("displayTexture").bindAsRenderTarget();
        else
            Window.bindAsRenderTarget();

        Aero.graphicsUtil.setClearColor(0.0f, 0.0f, 0.0f, 0f);
        Aero.graphicsUtil.clearColorAndDeptAndStencil();

        if (getBoolean("wireframe")) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        object.renderAll(ambientShader, this);

        if (getBoolean("wireframe")) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        for (BaseLight light : lights) {
            activeLight = light;

            ShadowInfo shadowInfo = light.getShadowInfo();

            if (shadowInfo != null) {
                setTexture("shadowMap", shadowInfo.getShadowMap());
                shadowInfo.getShadowMap().bindAsRenderTarget();
                shadowInfo.getShadowMap().bind(samplerMap.get("shadowMap"));
                Aero.graphicsUtil.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                Aero.graphicsUtil.clearColorAndDepth();
            }


            if (shadowInfo != null) {
                altCamera.setProjection(shadowInfo.getProjection());

                ShadowCameraTransform shadowCameraTransform = light.calcShadowCameraTransform(
                        mainCamera.getTransform().getTransformedPos(),
                        mainCamera.getTransform().getTransformedRot()
                );

                altCamera.getTransform().setPosition(shadowCameraTransform.position);
                altCamera.getTransform().setRotation(shadowCameraTransform.rotation);

                setMatrix4("lightMatrix", getMatrix4("biasMatrix").mul(altCamera.getViewProjection()));

                setFloat("shadowVarianceMin", shadowInfo.getMinVariance());
                setFloat("shadowLightBleedReduction", shadowInfo.getLightBleedReductionAmount());
                boolean flipFaces = shadowInfo.flipFaces();

                Camera tmp = mainCamera;
                mainCamera = altCamera;

                if (flipFaces) Aero.graphicsUtil.enableCullFace(GL_FRONT);

                Aero.graphicsUtil.setDepthClamp(true);
                object.renderAll(shadowMapShader, this);
                Aero.graphicsUtil.setDepthClamp(false);

                if (flipFaces) Aero.graphicsUtil.enableCullFace(GL_BACK);

                mainCamera = tmp;

                if (!shadowInfo.isInitGameObject()) {
                    shadowInfo.getGameObject().addComponent(new MeshRenderer(new Mesh("plane"), new Material(shadowInfo.getShadowMap())));
                    shadowInfo.setInitGameObject(true);
                }


                float shadowSoftness = light.getShadowInfo().getShadowSoftness();
                if (shadowSoftness != 0) {
                    ShadowBlurFilter blurFilter = new ShadowBlurFilter(shadowSoftness);
                    blurFilter.renderFilter(this, shadowInfo.getShadowMap(), shadowInfo.getShadowMapRenderTarget(), shadowInfo.getGameObject(), altCamera);
                }
            } else {
                setMatrix4("lightMatrix", new Matrix4().initScale(0, 0, 0));

                setFloat("shadowVarianceMin", 0.00002f);
                setFloat("shadowLightBleedReduction", 0.0f);
            }

            if (filters.size() != 0)
                getTexture("displayTexture").bindAsRenderTarget();
            else
                Window.bindAsRenderTarget();


            Aero.graphicsUtil.enableBlending(GL_ONE, GL_ONE);
            Aero.graphicsUtil.setDepthMask(false);
            Aero.graphicsUtil.setDepthFunc(GL_EQUAL);

            object.renderAll(activeLight.getShader(), this);

            Aero.graphicsUtil.setDepthFunc(GL_LESS);
            Aero.graphicsUtil.setDepthMask(true);
            Aero.graphicsUtil.disableBlending();
        }

        renderTimer.stopInvocation();
    }

    public void initDisplay(int width, int height) {
        ByteBuffer data = Util.createByteBuffer(width * height * 4);

        setTexture("displayTexture", new Texture(width, height, data, GL_TEXTURE_2D, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA, GL_RGBA, false, GL30.GL_COLOR_ATTACHMENT0));
        setTexture("displayTextureRenderTarget", new Texture(width, height, data, GL_TEXTURE_2D, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA, GL_RGBA, false, GL30.GL_COLOR_ATTACHMENT0));

        if (planeMaterial == null) {
            planeMaterial = new Material(getTexture("displayTexture"));

            planeObject = new GameObject().addComponent(new MeshRenderer(new Mesh("plane"), planeMaterial));
            planeObject.getTransform().rotate(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(90.0f)));
            planeObject.getTransform().rotate(new Quaternion(new Vector3(0, 0, 1), (float) Math.toRadians(180.0f)));
        } else {
            planeMaterial.setTexture("diffuse", getTexture("displayTexture"));
        }

    }

    public double getRenderTimeAndReset(double dividend) {
        return renderTimer.getTimeAndReset(dividend);
    }

    public double getSyncTimeAndReset(double dividend) {
        return syncTimer.getTimeAndReset(dividend);
    }

    public void addCamera(Camera camera) {
        mainCamera = camera;
    }

    public void addLight(BaseLight light) {
        lights.add(light);
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public static String GetOpenGLVersion() {
        return glGetString(GL_VERSION);
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

    public int getSamplerSlot(String samplerName) {
        return samplerMap.get(samplerName);
    }

}
