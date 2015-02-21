package com.theodore.aero.graphics.g3d;

import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.resources.MappedValues;

public class Material extends MappedValues {


    public Material() {
        this(new Texture("default"), Texture.NORMAL_UP, Texture.WHITE_PIXEL, Texture.WHITE_PIXEL, 0.04f, -1.0f);
    }

    public Material(Texture diffuse) {
        this(diffuse, Texture.NORMAL_UP, Texture.NORMAL_UP, Texture.WHITE_PIXEL, 0.04f, -1.0f);
    }

    public Material(Texture diffuse, Texture normalMap) {
        this(diffuse, normalMap, Texture.NORMAL_UP, Texture.WHITE_PIXEL, 0.04f, -1.0f);
    }


    public Material(Texture diffuse, Texture normalMap, Texture specularMap) {
        this(diffuse, normalMap, specularMap, Texture.WHITE_PIXEL, 0.04f, -1.0f);
    }

    public Material(Texture diffuse, Texture normalMap, Texture specularMap, Texture dispMap, float dispMapScale, float dispMapOffset) {
        setTexture("diffuse", diffuse);
        setTexture("normalMap", normalMap);
        setTexture("specularMap", specularMap);
        setTexture("dispMap", dispMap);

        setVector3("color", new Vector3(1, 1, 1));

        setFloat("shininess", 64);
        setFloat("alpha", 1);

        float baseBias = dispMapScale / 2.0f;
        setFloat("scale", dispMapScale);
        setFloat("bias", -baseBias + baseBias * dispMapOffset);

        setInteger("textureRepeat", 1);
        setInteger("numberOfRows", 1);

        setTexture("blendMap", Texture.BLACK_PIXEL);

        setTexture("rdiffuse", Texture.BLACK_PIXEL);
        setTexture("rnormalMap", Texture.NORMAL_UP);
        setTexture("rspecularMap", Texture.NORMAL_UP);
        setTexture("rdispMap", Texture.WHITE_PIXEL);

        setTexture("gdiffuse", Texture.BLACK_PIXEL);
        setTexture("gnormalMap", Texture.NORMAL_UP);
        setTexture("gspecularMap", Texture.NORMAL_UP);
        setTexture("gdispMap", Texture.WHITE_PIXEL);

        setTexture("bdiffuse", Texture.BLACK_PIXEL);
        setTexture("bnormalMap", Texture.NORMAL_UP);
        setTexture("bspecularMap", Texture.NORMAL_UP);
        setTexture("bdispMap", Texture.WHITE_PIXEL);
    }
}
