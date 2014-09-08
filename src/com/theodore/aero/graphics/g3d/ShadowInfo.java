package com.theodore.aero.graphics.g3d;

import com.theodore.aero.math.Matrix4;

public class ShadowInfo {

    private Matrix4 projection;
    private boolean flipFaces;

    private float shadowSoftness;
    private float lightBleedReductionAmount;
    private float minVariance;

    private int shadowMapPowerOf2;

    public ShadowInfo(Matrix4 projection, boolean flipFaces, int shadowMapPowerOf2, float shadowSoftness, float lightBleedReductionAmount, float minVariance) {
        this.projection = projection;
        this.flipFaces = flipFaces;
        this.shadowMapPowerOf2 = shadowMapPowerOf2;
        this.shadowSoftness = shadowSoftness;
        this.lightBleedReductionAmount = lightBleedReductionAmount;
        this.minVariance = minVariance;
    }

    public int getShadowMapPowerOf2() {
        return shadowMapPowerOf2;
    }

    public void setShadowMapPowerOf2(int shadowMapPowerOf2) {
        this.shadowMapPowerOf2 = shadowMapPowerOf2;
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
