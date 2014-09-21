package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.graphics.g3d.PointShadowInfo;
import com.theodore.aero.graphics.g3d.ShadowCameraTransform;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class BaseLight extends GameComponent {

    private Vector3 color;
    private float intensity;
    private Shader shader;
    private ShadowInfo shadowInfo;
    private PointShadowInfo pointShadowInfo;

    public BaseLight(Vector3 color, float intensity) {
        this.color = color;
        this.intensity = intensity;
        shadowInfo = null;
        pointShadowInfo = null;
    }

    public ShadowCameraTransform calcShadowCameraTransform(Vector3 mainCameraPos, Quaternion mainCameraRotation) {
        ShadowCameraTransform result = new ShadowCameraTransform();

        result.position = getTransform().getTransformedPos();
        result.rotation = getTransform().getTransformedRot();

        return result;
    }

    @Override
    public void addToEngine() {
        Aero.graphics.addLight(this);
    }

    public ShadowInfo getShadowInfo() {
        return shadowInfo;
    }

    protected void setShadowInfo(ShadowInfo shadowInfo) {
        this.shadowInfo = shadowInfo;
    }

    public PointShadowInfo getPointShadowInfo() {
        return pointShadowInfo;
    }

    protected void setPointShadowInfo(PointShadowInfo pointShadowInfo) {
        this.pointShadowInfo = pointShadowInfo;
    }

    protected void setShader(Shader shader) {
        this.shader = shader;
    }

    public Shader getShader() {
        return shader;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
