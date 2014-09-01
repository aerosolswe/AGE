package com.theodore.aero.graphics.g3d.lighting;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Shader;
import com.theodore.aero.graphics.g3d.ShadowMap;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public class BaseLight {

    private Transform transform;
    private Vector3 color;
    private float intensity;
    private String name;
    private Shader shader;
    private ShadowMap shadowMap;

    public BaseLight(Vector3 color, float intensity, Shader shader) {
        this.color = color;
        this.intensity = intensity;
        this.name = "name";
        this.shader = shader;
        this.transform = new Transform();
    }

    public void render(ArrayList<GameObject> objects) {
        shadowMap.render(objects);
    }

    public void apply(GameObject object) {
        shadowMap.applyCameraTransform(object);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shader getShader() {
        return shader;
    }

    protected void setShader(Shader shader) {
        this.shader = shader;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public void setShadowMap(ShadowMap shadowMap) {
        this.shadowMap = shadowMap;
    }

    public ShadowMap getShadowMap() {
        return shadowMap;
    }
}
