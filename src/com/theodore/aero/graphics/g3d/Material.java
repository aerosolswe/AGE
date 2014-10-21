package com.theodore.aero.graphics.g3d;

import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.resourceManagement.MappedValues;

import java.util.HashMap;

public class Material extends MappedValues {


    public Material() {
        this(new Texture("default"), Texture.NORMAL_UP, Texture.WHITE_PIXEL, 0.04f, -1.0f);
    }

    public Material(Texture diffuse) {
        this(diffuse, Texture.NORMAL_UP, Texture.WHITE_PIXEL, 0.04f, -1.0f);
    }

    public Material(Texture diffuse, Texture normalMap) {
        this(diffuse, normalMap, Texture.WHITE_PIXEL, 0.04f, -1.0f);
    }

    public Material(Texture diffuse, Texture normalMap, Texture dispMap, float dispMapScale, float dispMapOffset) {
        setTexture("diffuse", diffuse);
        setTexture("normalMap", normalMap);
        setTexture("dispMap", dispMap);

        setVector3("color", new Vector3(1, 1, 1));

        setFloat("specularIntensity", 1);
        setFloat("specularPower", 8);
        setFloat("alpha", 1);

        float baseBias = dispMapScale / 2.0f;
        setFloat("scale", dispMapScale);
        setFloat("bias", -baseBias + baseBias * dispMapOffset);

        setInteger("textureRepeat", 1);

    }

}
