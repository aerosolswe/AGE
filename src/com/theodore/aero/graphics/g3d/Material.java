package com.theodore.aero.graphics.g3d;

import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Vector3;

public class Material {

    private static Material defaultMaterial;

    private Texture diffuse;
    private Texture normal;
    private Texture bumpMap;

    private Vector3 color;

    private float heightScale;
    private float heightBias;
    private float specularIntensity;
    private float specularPower;
    private float alpha;

    private int textureRepeat;

    public static void generateDefaultMaterials() {
        defaultMaterial = new Material();
    }

    public static Material getDefaultMaterials() {
        return defaultMaterial;
    }

    public Material() {
        this(Texture.get("default.png"));
    }

    public Material(Texture diffuse) {
        this.diffuse = diffuse;
        this.normal = Texture.NORMAL_UP;
        this.bumpMap = Texture.WHITE_PIXEL;

        this.heightScale = 0;
        this.heightBias = 0;
        this.specularIntensity = 0;
        this.specularPower = 8;
        this.textureRepeat = 1;
        this.alpha = 1;

        this.color = new Vector3(1, 1, 1);
    }

    public Texture getBumpTexture() {
        return bumpMap;
    }

    public void setBumpTexture(Texture texture) {
        this.bumpMap = texture;
        if (getHeightScale() == 0 && getHeightBias() == 0) {
            setHeightScale(0.04f);
            setHeightBias(-0.03f);
        }
    }

    public Texture getNormalTexture() {
        return normal;
    }

    public void setNormalTexture(Texture texture) {
        this.normal = texture;
    }

    public Texture getDiffuseTexture() {
        return diffuse;
    }

    public void setDiffuseTexture(Texture texture) {
        this.diffuse = texture;
    }

    public float getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(float heightScale) {
        this.heightScale = heightScale;
    }

    public float getHeightBias() {
        return heightBias;
    }

    public void setHeightBias(float heightBias) {
        this.heightBias = heightBias;
    }

    public float getSpecularIntensity() {
        return specularIntensity;
    }

    public void setSpecularIntensity(float specularIntensity) {
        this.specularIntensity = specularIntensity;
    }

    public float getSpecularPower() {
        return specularPower;
    }

    public void setSpecularPower(float specularPower) {
        this.specularPower = specularPower;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public int getTextureRepeat() {
        return textureRepeat;
    }

    public void setTextureRepeat(int textureRepeat) {
        this.textureRepeat = textureRepeat;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }

}
