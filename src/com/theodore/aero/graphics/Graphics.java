package com.theodore.aero.graphics;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.Camera;
import com.theodore.aero.components.MeshRenderer;
import com.theodore.aero.core.*;
import com.theodore.aero.graphics.g3d.*;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.graphics.shaders.shadow.PointLightShadowShader;
import com.theodore.aero.graphics.shaders.shadow.ShadowMapShader;
import com.theodore.aero.graphics.shaders.filters.FxaaFilterShader;
import com.theodore.aero.graphics.shaders.filters.GausFilterShader;
import com.theodore.aero.graphics.shaders.filters.NullFilterShader;
import com.theodore.aero.graphics.shaders.forward.ForwardAmbientShader;
import com.theodore.aero.math.Frustum;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {

    /**
     * OPTIONS
     */
    public static float FXAA_QUALITY = 4.0f;
    public static boolean WIREFRAME_MODE = false;

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
    private ArrayList<BaseLight> lights;

    /**
     * The active light
     */
    private BaseLight activeLight;

    /**
     * Shaders for rendering
     */
    private Shader ambientShader;
    private Shader basicShader;
    private Shader shadowMapShader;
    private Shader pointShadowMapShader;
    private Shader nullFilterShader;
    private Shader gausFilterShader;
    private Shader fxaaFilterShader;

    /**
     * Main and alternative camera
     */
    private Camera mainCamera;
    private Camera altCamera;

    /**
     * Render to texture
     */
    public Texture displayTexture;
    private Material planeMaterial;
    private GameObject planeObject;

    /**
     * Light matrices
     */
    private Matrix4 lightMatrix = new Matrix4().initIdentity();
    private Matrix4 biasMatrix = new Matrix4().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4().initTranslation(1.0f, 1.0f, 1.0f));

    /**
     * Ambient light intensity/color
     */
    private Vector3 ambientLight;

    /**
     * Current frames per seconds
     */
    private int currentFps = 60;

    public void initGraphics() {
        Aero.graphicsUtil.init();
    }

    public void init() {
        ambientLight = new Vector3(0.08f, 0.09f, 0.1f);
//        ambientLight = new Vector3(0.9f, 0.9f, 0.9f);

        lights = new ArrayList<BaseLight>();

        ambientShader = ForwardAmbientShader.getInstance();
        basicShader = BasicShader.getInstance();
        shadowMapShader = ShadowMapShader.getInstance();
        pointShadowMapShader = PointLightShadowShader.getInstance();
        nullFilterShader = NullFilterShader.getInstance();
        gausFilterShader = GausFilterShader.getInstance();
        fxaaFilterShader = FxaaFilterShader.getInstance();

        Mesh.generatePrimitives();
        Texture.generateBaseTextures();

        renderTimer = new ProfileTimer();
        syncTimer = new ProfileTimer();

        lightMatrix = new Matrix4().initScale(0, 0, 0);

        initDisplay(Window.getWidth(), Window.getHeight());

        altCamera = new Camera(new Matrix4().initIdentity());
        GameObject altCameraObject = new GameObject().addComponent(altCamera);

    }

    public void blurShadowMap(Texture shadowMap, Texture shadowMapRenderTarget, float blurAmount, GameObject object) {
        gausFilterShader.setUniform("blurScale", new Vector3(blurAmount / (shadowMap.getHeight()), 0.0f, 0.0f));
        applyFilter(gausFilterShader, shadowMapRenderTarget, shadowMap, object);

        gausFilterShader.setUniform("blurScale", new Vector3(0.0f, blurAmount / (shadowMap.getWidth()), 0.0f));
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

    public void fullRender(GameObject object, SkyGradient skyGradient) {
        switch (currentRenderingState) {
            case SIMPLE:
                simpleRenderingPass(object, skyGradient);
                break;
            case FORWARD:
                forwardRenderingPass(object, skyGradient);
                break;
            case DEFERRED:
                deferredRenderingPass(object, skyGradient);
                break;
        }


        syncTimer.startInvocation();
        applyFilter(fxaaFilterShader, displayTexture, null, planeObject);
        syncTimer.stopInvocation();
    }

    public void deferredRenderingPass(GameObject object, SkyGradient skyGradient) {

    }

    private void simpleRenderingPass(GameObject object, SkyGradient skyGradient) {
        renderTimer.startInvocation();
        displayTexture.bindAsRenderTarget();

        Aero.graphicsUtil.setClearColor(0.0f, 0f, 0f, 0f);
        Aero.graphicsUtil.clearColorAndDepth();

        if (skyGradient != null) {
            skyGradient.render(this);
        }

        if (WIREFRAME_MODE) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        Frustum frustum = mainCamera.getFrustum();

        if (frustum.sphereIntersection(object.getTransform().getPosition(), object.getTransform().getScale().length())) {
            object.renderAll(ambientShader, this);
        }


        if (WIREFRAME_MODE) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        renderTimer.stopInvocation();
    }


    private void forwardRenderingPass(GameObject object, SkyGradient skyGradient) {
        renderTimer.startInvocation();
        displayTexture.bindAsRenderTarget();

        Aero.graphicsUtil.setClearColor(0.0f, 0.0f, 0.0f, 0f);
        Aero.graphicsUtil.clearColorAndDeptAndStencil();


        if (WIREFRAME_MODE) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        if (skyGradient != null) {
            skyGradient.render(this);
        }

        object.renderAll(ambientShader, this);

        if (WIREFRAME_MODE) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        for (BaseLight light : lights) {
            activeLight = light;

            PointShadowInfo pointShadowInfo = light.getPointShadowInfo();

            if(pointShadowInfo != null){
                for(int i = 0; i < 6; i++){


                }
            }

            if(pointShadowInfo != null){
                for(int i = 0; i < 6; i++){
//                    pointShadowInfo.getShadowMap().bindAsRenderTarget();
                    pointShadowInfo.getShadowMap().bindForWriting(pointShadowInfo.getCameraDirection(i).cubeMapFace);
                    pointShadowInfo.getShadowMap().bind(Texture.SHADOW_MAP_TEXTURE);

                    Aero.graphicsUtil.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                    Aero.graphicsUtil.clearColorAndDepth();

                    altCamera.setProjection(new Matrix4().initPerspective(90, 1, 0.1f, 100));
                    altCamera.getTransform().setPosition(light.getTransform().getPosition());
                    altCamera.getTransform().setRotation(new Quaternion(new Matrix4().initRotation(pointShadowInfo.getCameraDirection(i).getTarget(), pointShadowInfo.getCameraDirection(i).getUp())));

                    Camera tmp = mainCamera;
                    mainCamera = altCamera;

                    Aero.graphicsUtil.enableCullFace(GL_FRONT);
                    Aero.graphicsUtil.setDepthClamp(true);
                    object.renderAll(pointShadowMapShader, this);
                    Aero.graphicsUtil.setDepthClamp(false);
                    Aero.graphicsUtil.enableCullFace(GL_BACK);

                    mainCamera = tmp;

                    pointShadowInfo.getShadowMap().bindForReading(Texture.SHADOW_MAP_TEXTURE);

                }
            }


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
            }/* else {
                lightMatrix = new Matrix4().initScale(0, 0, 0);

                light.getShader().setUniformf("shadowVarianceMin", 0.00002f);
                light.getShader().setUniformf("shadowLightBleedReduction", 0.0f);
            }*/

            displayTexture.bindAsRenderTarget();


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
        ByteBuffer bData = Util.createByteBuffer(width * height * 4);

        displayTexture = new Texture(width, height, bData, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA, GL_RGBA, false, GL30.GL_COLOR_ATTACHMENT0);

        planeMaterial = new Material(displayTexture);

        planeObject = new GameObject().addComponent(new MeshRenderer(Mesh.get("plane"), planeMaterial));
        planeObject.getTransform().rotate(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(90.0f)));
        planeObject.getTransform().rotate(new Quaternion(new Vector3(0, 0, 1), (float) Math.toRadians(180.0f)));
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
