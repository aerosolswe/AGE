package com.theodore.aero.graphics.g3d;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.shaders.ShadowMapShader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;

public class ShadowMap {

    public float[] LIGHT_VIEW_SIZE;//5,15,60,100 (CascadeValues, divided by 2 in current light view implementation)

    public Matrix4[] lightMatrix = new Matrix4[]{new Matrix4().initIdentity(), new Matrix4().initIdentity(), new Matrix4().initIdentity(), new Matrix4().initIdentity()};
    public Matrix4[] projMatrix = new Matrix4[4];

    public Texture[] shadowDepthTexture = new Texture[4];
    public FrameBuffer[] shadowFrameBuffers = new FrameBuffer[4];
    public Camera[] shadowCamera = new Camera[4];

    public Quaternion direction;

    public ShadowMap(Matrix4[] projectionMatrix, float[] LIGHT_VIEW_SIZE) {
        direction = new Quaternion();
        this.LIGHT_VIEW_SIZE = LIGHT_VIEW_SIZE;

        int shadowSize = Aero.graphics.getShadowSize();

        for (int i = 0; i < 4; i++) {
            this.projMatrix[i] = projectionMatrix[i];

            FloatBuffer data = Util.createFloatBuffer(shadowSize * shadowSize);

            for (int j = 0; j < shadowSize * shadowSize; j++)
                data.put(1);
            data.flip();

            shadowDepthTexture[i] = new Texture(shadowSize, shadowSize, data, GL_LINEAR, GL_CLAMP_TO_EDGE);
            shadowFrameBuffers[i] = new FrameBuffer();
            shadowFrameBuffers[i].createFrameBuffer(shadowDepthTexture[i], GL_DEPTH_ATTACHMENT);

            shadowCamera[i] = new Camera(new Vector3(0, 0, 0), this.direction);
        }
        FrameBuffer.unbindRenderTarget();
    }

    public void render(ArrayList<GameObject> objects) {
        for (int i = 0; i < 4; i++) {
            Vector3 shadowCameraPosition = Transform.getCamera().getPosition().add(Transform.getCamera().getForward().mul(LIGHT_VIEW_SIZE[i]));
            shadowCamera[i].setPosition(shadowCameraPosition);
            shadowCamera[i].setRotation(this.direction);

            Transform.pushProjection(projMatrix[i]);
            Transform.pushCamera(shadowCamera[i]);

            shadowFrameBuffers[i].bindAsRenderTarget();
            Aero.graphicsUtil.clearDepth();

            ShadowMapShader.getInstance().bind();

            for (GameObject object : objects) {
                if (!object.canCastShadows())
                    continue;

                Mesh tempMesh = object.getMesh();
                Transform tempTransform = object.getTransform();

                ShadowMapShader.getInstance().updateUniforms(tempTransform.calcModel(), tempTransform.getMVP(), Material.getDefaultMaterials());
                tempMesh.draw();
            }

            Transform.popCamera();
            Transform.popProjection();
        }

        FrameBuffer.unbindRenderTarget();
    }

    public void applyCameraTransform(GameObject object) {
        Matrix4 biasMatrix = new Matrix4(new float[][]{
                {0.5f, 0.0f, 0.0f, 0.5f},
                {0.0f, 0.5f, 0.0f, 0.5f},
                {0.0f, 0.0f, 0.5f, 0.5f},
                {0.0f, 0.0f, 0.0f, 1.0f}});

        for (int i = 0; i < 4; i++) {
            Transform.pushCamera(shadowCamera[i]);
            Transform.pushProjection(projMatrix[i]);

            object.getTransform().calcModel();
            lightMatrix[i] = biasMatrix.mul(object.getTransform().getMVP());

            Transform.popCamera();
            Transform.popProjection();
        }
    }

    public Quaternion getDirection() {
        return direction;
    }

    public void setDirection(Quaternion direction) {
        this.direction = direction;
    }
}
