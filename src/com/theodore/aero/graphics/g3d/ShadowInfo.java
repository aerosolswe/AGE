package com.theodore.aero.graphics.g3d;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class ShadowInfo {

    private Matrix4 projection;
    private boolean flipFaces;
    private boolean initGameObject = false;

    private GameObject gameObject;
    private Texture shadowMap;
    private Texture shadowMapRenderTarget;

    private float shadowSoftness;
    private float lightBleedReductionAmount;
    private float minVariance;

    private int shadowMapSize;

    public ShadowInfo(Matrix4 projection, boolean flipFaces, int shadowMapSize, float shadowSoftness, float lightBleedReductionAmount, float minVariance) {
        this.projection = projection;
        this.flipFaces = flipFaces;
        this.shadowMapSize = shadowMapSize;
        this.shadowSoftness = shadowSoftness;
        this.lightBleedReductionAmount = lightBleedReductionAmount;
        this.minVariance = minVariance;

        ByteBuffer buffer = Util.createByteBuffer(shadowMapSize * shadowMapSize * 4);
        shadowMap = new Texture(shadowMapSize, shadowMapSize, buffer, GL_TEXTURE_2D, GL_LINEAR, GL30.GL_RG32F, GL_RGBA, true, GL30.GL_COLOR_ATTACHMENT0);
        shadowMapRenderTarget = new Texture(shadowMapSize, shadowMapSize, buffer, GL_TEXTURE_2D, GL_LINEAR, GL30.GL_RG32F, GL_RGBA, true, GL30.GL_COLOR_ATTACHMENT0);

        gameObject = new GameObject();
        gameObject.getTransform().rotate(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(90)));
        gameObject.getTransform().rotate(new Quaternion(new Vector3(0, 0, 1), (float) Math.toRadians(180)));
    }

    public Texture getShadowMap() {
        return shadowMap;
    }

    public Texture getShadowMapRenderTarget() {
        return shadowMapRenderTarget;
    }

    public boolean isInitGameObject() {
        return initGameObject;
    }

    public void setInitGameObject(boolean initGameObject) {
        this.initGameObject = initGameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public int getShadowMapSize() {
        return shadowMapSize;
    }

    public void setShadowMapSize(int shadowMapSize) {
        this.shadowMapSize = shadowMapSize;
    }

    public Matrix4 getProjection() {
        return projection;
    }

    public void setProjection(Matrix4 projection) {
        this.projection = projection;
    }

    public boolean flipFaces() {
        return flipFaces;
    }

    public void flipFaces(boolean flipFaces) {
        this.flipFaces = flipFaces;
    }

    public float getShadowSoftness() {
        return shadowSoftness;
    }

    public void setShadowSoftness(float shadowSoftness) {
        this.shadowSoftness = shadowSoftness;
    }

    public float getLightBleedReductionAmount() {
        return lightBleedReductionAmount;
    }

    public void setLightBleedReductionAmount(float lightBleedReductionAmount) {
        this.lightBleedReductionAmount = lightBleedReductionAmount;
    }

    public float getMinVariance() {
        return minVariance;
    }

    public void setMinVariance(float minVariance) {
        this.minVariance = minVariance;
    }
}
