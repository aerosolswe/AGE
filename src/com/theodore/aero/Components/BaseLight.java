package com.theodore.aero.Components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.g3d.ShadowMap;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Vector3;

public class BaseLight extends GameComponent {

    private Vector3 color;
    private float intensity;
    private Shader shader;
    private ShadowMap shadowMap;

    public BaseLight(Vector3 color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public void render(GameObject object) {
        shadowMap.render(object);
    }

    public void apply(GameObject object) {
        shadowMap.applyCameraTransform(object);
    }

    @Override
    public void addToEngine() {
        Aero.graphics.addLight(this);
    }

    public void setShader(Shader shader) {
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

    public void setShadowMap(ShadowMap shadowMap) {
        this.shadowMap = shadowMap;
    }

    public ShadowMap getShadowMap() {
        return shadowMap;
    }
}
