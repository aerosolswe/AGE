package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;
import com.theodore.aero.math.Vector3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Material {

    public static final String DIRECTORY = "materials/";

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

    public Material(String fileName) {

        this.diffuse = Texture.get("default.png");
        this.normal = Texture.NORMAL_UP;
        this.bumpMap = Texture.WHITE_PIXEL;

        this.heightScale = 0;
        this.heightBias = 0;
        this.specularIntensity = 0;
        this.specularPower = 8;
        this.textureRepeat = 1;
        this.alpha = 1;

        this.color = new Vector3(1, 1, 1);

        try {
            BufferedReader in = new BufferedReader(new FileReader(Aero.getResourcePath(DIRECTORY + fileName + ".mat")));

            String line;
            while ((line = in.readLine()) != null) {
                String[] s = line.split(" ");

                if (s[0].equals("setDiffuse")) {
                    diffuse = Texture.get(s[1]);
                } else if (s[0].equals("setNormal")) {
                    normal = Texture.get(s[1]);
                } else if (s[0].equals("setBump")) {
                    bumpMap = Texture.get(s[1]);
                } else if (s[0].equals("setHeightScale")) {
                    setHeightScale(Float.valueOf(s[1]));
                } else if (s[0].equals("setHeightBias")) {
                    setHeightBias(Float.valueOf(s[1]));
                } else if (s[0].equals("specularIntensity")) {
                    setSpecularIntensity(Float.valueOf(s[1]));
                } else if (s[0].equals("specularPower")) {
                    setSpecularPower(Float.valueOf(s[1]));
                } else if (s[0].equals("textureRepeat")) {
                    setTextureRepeat(Integer.valueOf(s[1]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

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
