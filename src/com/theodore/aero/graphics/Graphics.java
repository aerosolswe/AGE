package com.theodore.aero.graphics;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.Camera;
import com.theodore.aero.components.MeshRenderer;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.g3d.*;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.graphics.shaders.shadow.ShadowMapShader;
import com.theodore.aero.graphics.shaders.filters.FxaaFilterShader;
import com.theodore.aero.graphics.shaders.filters.GausFilterShader;
import com.theodore.aero.graphics.shaders.filters.NullFilterShader;
import com.theodore.aero.graphics.shaders.forward.ForwardAmbientShader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {

    public RenderingState currentRenderingState = RenderingState.FORWARD;

    private ArrayList<BaseLight> lights;

    private BaseLight activeLight;

    private Shader ambientShader;
    private Shader basicShader;
    private Shader shadowMapShader;
    private Shader nullFilterShader;
    private Shader gausFilterShader;
    private Shader fxaaFilterShader;

    private Camera mainCamera;
    private Camera altCamera;

    public Texture displayTexture;

    private Material planeMaterial;
    private GameObject planeObject;

    private Matrix4 lightMatrix = new Matrix4().initIdentity();
    private Matrix4 biasMatrix = new Matrix4().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4().initTranslation(1.0f, 1.0f, 1.0f));

    private Vector3 ambientLight;

    private int maxFps = 3000;
    private int currentFps = 0;

    public float fxaaQuality = 8.0f;

    public void initGraphics() {
        Aero.graphicsUtil.init();
    }

    public void init() {
        ambientLight = new Vector3(0.08f, 0.09f, 0.1f);
//        ambientLight = new Vector3(1f, 1f, 1f);

        lights = new ArrayList<BaseLight>();

        ambientShader = ForwardAmbientShader.getInstance();
        basicShader = BasicShader.getInstance();
        shadowMapShader = ShadowMapShader.getInstance();
        nullFilterShader = NullFilterShader.getInstance();
        gausFilterShader = GausFilterShader.getInstance();
        fxaaFilterShader = FxaaFilterShader.getInstance();

        Mesh.generatePrimitives();
        Material.generateDefaultMaterials();

//        initDisplay();

        lightMatrix = new Matrix4().initScale(0, 0, 0);

        altCamera = new Camera(new Matrix4().initIdentity());
        GameObject altCameraObject = new GameObject().addComponent(altCamera);
    }

    public void blurShadowMap(Texture shadowMap, Texture shadowMapRenderTarget, float blurAmount, GameObject object) {
        gausFilterShader.setUniform("blurScale", new Vector3(0.0f, blurAmount / (shadowMap.getHeight()), 0.0f));
        applyFilter(gausFilterShader, shadowMapRenderTarget, shadowMap, object);

        gausFilterShader.setUniform("blurScale", new Vector3(blurAmount / (shadowMap.getWidth()), 0.0f, 0.0f));
        applyFilter(gausFilterShader, shadowMap, shadowMapRenderTarget, object);
    }

    public void applyFilter(Shader filter, Texture source, Texture dest, GameObject object) {
        assert (source != dest);
        if (dest == null)
            Window.bindAsRenderTarget();
        else
            dest.bindAsRenderTarget();


        filter.updateTextureUniform("filterTexture", source.getID());

        altCamera.setProjection(new Matrix4().initIdentity());
        altCamera.getTransform().setPosition(new Vector3());
        altCamera.getTransform().setRotation(new Quaternion(new Vector3(0, 1, 0), (float) Math.toRadians(180)));

        Camera tmp = mainCamera;
        mainCamera = altCamera;

        Aero.graphicsUtil.clearDepth();
        object.renderAll(filter, this);

        filter.updateTextureUniform("filterTexture", 0);

        mainCamera = tmp;

    }

    public void fullRender(GameObject object, SkyBox skyBox) {
        switch (currentRenderingState) {
            case SIMPLE:
                simpleRenderingPass(object, skyBox);
                break;
            case FORWARD:
                forwardRenderingPass(object, skyBox);
                break;
            case DEFERRED:
                break;
        }
    }


    private void simpleRenderingPass(GameObject object, SkyBox skyBox) {
        object.renderAll(basicShader, this);
        if (skyBox != null) {
            skyBox.render(this);
        }
    }

    private void forwardRenderingPass(GameObject object, SkyBox skyBox) {
        displayTexture.bindAsRenderTarget();

        Aero.graphicsUtil.setClearColor(0.32f, 0.5f, 0.8f, 1.0f);
        Aero.graphicsUtil.clearColorAndDepth();
        object.renderAll(ambientShader, this);

        for (BaseLight light : lights) {
            activeLight = light;
            ShadowInfo shadowInfo = light.getShadowInfo();

            if (shadowInfo != null) {
                shadowInfo.getShadowMap().bindAsRenderTarget();
                shadowInfo.getShadowMap().bind(Texture.SHADOW_MAP_TEXTURE);
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

                lightMatrix = biasMatrix.mul(altCamera.getViewProjection());
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
                    shadowInfo.getGameObject().addComponent(new MeshRenderer(Mesh.get("plane"), new Material(shadowInfo.getShadowMap())));
                    shadowInfo.setInitGameObject(true);
                }


                float shadowSoftness = light.getShadowInfo().getShadowSoftness();
                if (shadowSoftness != 0)
                    blurShadowMap(shadowInfo.getShadowMap(), shadowInfo.getShadowMapRenderTarget(), shadowSoftness, shadowInfo.getGameObject());
            } else {
                lightMatrix = new Matrix4().initScale(0, 0, 0);

//                light.getShader().setUniformf("shadowVarianceMin", 0.00002f);
//                light.getShader().setUniformf("shadowLightBleedReduction", 0.0f);
            }

            displayTexture.bindAsRenderTarget();
            if (skyBox != null) {
                skyBox.render(this);
            }

            Aero.graphicsUtil.enableBlending(GL_ONE, GL_ONE);
            Aero.graphicsUtil.setDepthMask(false);
            Aero.graphicsUtil.setDepthFunc(GL_EQUAL);

            object.renderAll(activeLight.getShader(), this);

            Aero.graphicsUtil.setDepthFunc(GL_LESS);
            Aero.graphicsUtil.setDepthMask(true);
            Aero.graphicsUtil.disableBlending();
        }

        applyFilter(fxaaFilterShader, displayTexture, null, planeObject);
    }

    public void initDisplay(int width, int height) {
        ByteBuffer bData = Util.createByteBuffer(width * height * 4);

        displayTexture = new Texture(width, height, bData, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA, GL_RGBA, false, GL30.GL_COLOR_ATTACHMENT0);

        planeMaterial = new Material(displayTexture);

        planeObject = new GameObject().addComponent(new MeshRenderer(Mesh.get("plane"), planeMaterial));
        planeObject.getTransform().rotate(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(90.0f)));
        planeObject.getTransform().rotate(new Quaternion(new Vector3(0, 0, 1), (float) Math.toRadians(180.0f)));
    }

    public void addCamera(Camera camera) {
        mainCamera = camera;
    }


    public void addLight(BaseLight light) {
        lights.add(light);
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

    public Vector3 getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3 ambientLight) {
        this.ambientLight = ambientLight;
    }

    public int getMaxFps() {
        return maxFps;
    }

    public void setMaxFps(int maxFps) {
        this.maxFps = maxFps;
    }

    public Matrix4 getLightMatrix() {
        return lightMatrix;
    }

    public int getCurrentFps() {
        return currentFps;
    }

    public void setCurrentFps(int currentFps) {
        this.currentFps = currentFps;
    }
}
