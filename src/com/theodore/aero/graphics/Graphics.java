package com.theodore.aero.graphics;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.Camera;
import com.theodore.aero.components.MeshRenderer;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.ShadowCameraTransform;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.graphics.shaders.Shadow.ShadowMapShader;
import com.theodore.aero.graphics.shaders.filters.GausFilterShader;
import com.theodore.aero.graphics.shaders.filters.NullFilterShader;
import com.theodore.aero.graphics.shaders.forward.ForwardAmbientShader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL12;
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
    private NullFilterShader nullFilterShader;
    private GausFilterShader gausFilterShader;

    private Camera mainCamera;
    private Camera altCamera;

    private Texture[] shadowMaps;
    private Texture[] shadowMapRenderTargets;

    private GameObject[] planes;

    private Mesh planeMesh;
    private Material planeMaterial;
    private Texture planeTexture;
    private Transform planeTransform;


    private Matrix4 lightMatrix = new Matrix4().initIdentity();
    private Matrix4 biasMatrix = new Matrix4().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4().initTranslation(1.0f, 1.0f, 1.0f));

    private Vector3 ambientLight;

    private int numShadowMaps = 10;
    private int maxFps = 3000;
    private int currentFps = 0;

    public void initGraphics() {
        Aero.graphicsUtil.init();
    }

    public void init() {
        ambientLight = new Vector3(0.16f, 0.18f, 0.2f);

        shadowMaps = new Texture[numShadowMaps];
        shadowMapRenderTargets = new Texture[numShadowMaps];
        planes = new GameObject[numShadowMaps];

        lights = new ArrayList<BaseLight>();

        ambientShader = ForwardAmbientShader.getInstance();
        basicShader = BasicShader.getInstance();
        shadowMapShader = ShadowMapShader.getInstance();
        nullFilterShader = NullFilterShader.getInstance();
        gausFilterShader = GausFilterShader.getInstance();

        Mesh.generatePrimitives();
        Material.generateDefaultMaterials();

        int w = Window.getWidth();
        int h = Window.getHeight();

        ByteBuffer b = Util.createByteBuffer(w * h * 4);

        planeMesh = Mesh.get("plane");
        planeTexture = new Texture(w, h, b, GL_TEXTURE_2D, GL_LINEAR, GL30.GL_RG32F, GL_RGBA, GL12.GL_CLAMP_TO_EDGE, GL30.GL_COLOR_ATTACHMENT0);
        planeMaterial = new Material(planeTexture);
        planeTransform = new Transform();

        lightMatrix = new Matrix4().initScale(0, 0, 0);

        for (int i = 0; i < numShadowMaps; i++) {
            int shadowMapSize = 1 << (i + 1);

            ByteBuffer buffer = Util.createByteBuffer(shadowMapSize * shadowMapSize * 4);
            shadowMaps[i] = new Texture(shadowMapSize, shadowMapSize, buffer, GL_TEXTURE_2D, GL_LINEAR, GL30.GL_RG32F, GL_RGBA, GL12.GL_CLAMP_TO_EDGE, GL30.GL_COLOR_ATTACHMENT0);
            shadowMapRenderTargets[i] = new Texture(shadowMapSize, shadowMapSize, buffer, GL_TEXTURE_2D, GL_LINEAR, GL30.GL_RG32F, GL_RGBA, GL12.GL_CLAMP_TO_EDGE, GL30.GL_COLOR_ATTACHMENT0);

            planes[i] = new GameObject().addComponent(new MeshRenderer(Mesh.get("plane"), new Material(shadowMaps[i])));
            planes[i].getTransform().rotate(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(90)));
            planes[i].getTransform().rotate(new Quaternion(new Vector3(0, 0, 1), (float) Math.toRadians(180)));
        }

        altCamera = new Camera(new Matrix4().initIdentity());
        GameObject altCameraObject = new GameObject().addComponent(altCamera);
    }

    public void blurShadowMap(int shadowMapIndex, float blurAmount) {
        Texture shadowMap = shadowMaps[shadowMapIndex];
        Texture tempTarget = shadowMapRenderTargets[shadowMapIndex];

        gausFilterShader.setUniform("blurScale", new Vector3(0.0f, blurAmount / (shadowMap.getHeight()), 0.0f));
        applyFilter(gausFilterShader, tempTarget, shadowMap);

        gausFilterShader.setUniform("blurScale", new Vector3(blurAmount / (shadowMap.getWidth()), 0.0f, 0.0f));
        applyFilter(gausFilterShader, shadowMap, tempTarget);
    }

    public void applyFilter(Shader filter, Texture source, Texture dest) {
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
        filter.bind();
        filter.updateUniforms(planeTransform, planeMaterial, this);
        planeMesh.draw();
//        planes[planeIndex].renderAll(filter, this);

        mainCamera = tmp;

        filter.updateTextureUniform("filterTexture", 0);

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
                break;
        }
    }


    private void simpleRenderingPass(GameObject object) {
        object.renderAll(basicShader, this);
    }

    private void forwardRenderingPass(GameObject object) {
        Window.bindAsRenderTarget();

        Aero.graphicsUtil.setClearColor(0.32f, 0.5f, 0.8f, 1.0f);
        Aero.graphicsUtil.clearColorAndDepth();
        object.renderAll(ambientShader, this);

        for (BaseLight light : lights) {
            activeLight = light;
            ShadowInfo shadowInfo = activeLight.getShadowInfo();

            int shadowMapIndex = 0;
            if (shadowInfo != null)
                shadowMapIndex = shadowInfo.getShadowMapPowerOf2() - 1;

            shadowMaps[shadowMapIndex].bindAsRenderTarget();
            shadowMaps[shadowMapIndex].bind(Texture.SHADOW_MAP_TEXTURE);
            Aero.graphicsUtil.setClearColor(1.0f, 1.0f, 0.0f, 0.0f);
            Aero.graphicsUtil.clearColorAndDepth();

            if (shadowInfo != null) {
                altCamera.setProjection(shadowInfo.getProjection());

                ShadowCameraTransform shadowCameraTransform = activeLight.calcShadowCameraTransform(
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

                float shadowSoftness = activeLight.getShadowInfo().getShadowSoftness();
                if (shadowSoftness != 0)
                    blurShadowMap(shadowMapIndex, shadowSoftness);
            } else {
                lightMatrix = new Matrix4().initScale(0, 0, 0);

                activeLight.getShader().setUniformf("shadowVarianceMin", 0.00002f);
                activeLight.getShader().setUniformf("shadowLightBleedReduction", 0.0f);
            }

            Window.bindAsRenderTarget();

            Aero.graphicsUtil.enableBlending(GL_ONE, GL_ONE);
            Aero.graphicsUtil.setDepthMask(false);
            Aero.graphicsUtil.setDepthFunc(GL_EQUAL);

            object.renderAll(activeLight.getShader(), this);

            Aero.graphicsUtil.setDepthFunc(GL_LESS);
            Aero.graphicsUtil.setDepthMask(true);
            Aero.graphicsUtil.disableBlending();
        }

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
