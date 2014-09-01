package com.theodore.aero.graphics.shaders;

import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Shader;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Matrix4;

public class BasicShader extends Shader {

    private static final BasicShader instance = new BasicShader();

    public static BasicShader getInstance() {
        return instance;
    }

    private BasicShader() {
        super();

        addVertexShaderFromFile("basicrendering/basicVertex.vs");
        addFragmentShaderFromFile("basicrendering/basicFragment.fs");
        compileShader();

        addUniform("transform");
        addUniform("color");
        addUniform("alpha");
        addUniform("textureRepeat");
    }

    @Override
    public void updateUniforms(Matrix4 worldMatrix, Matrix4 projectedMatrix, Material material) {
        if (material.getDiffuseTexture() != null)
            material.getDiffuseTexture().bind(Texture.DIFFUSE_TEXTURE);
        else
            Texture.unbind();

        setUniform("transform", projectedMatrix);
        setUniform("color", material.getColor());
        setUniformf("alpha", material.getAlpha());
        setUniformi("textureRepeat", material.getTextureRepeat());
    }

    @Override
    public void updateUniforms(Material material) {
        if (material.getDiffuseTexture() != null)
            material.getDiffuseTexture().bind(Texture.DIFFUSE_TEXTURE);
        else
            Texture.unbind();

        setUniform("color", material.getColor());
        setUniformf("alpha", material.getAlpha());
        setUniformi("textureRepeat", material.getTextureRepeat());
    }

    @Override
    public void updateUniformProjectedMatrix(Matrix4 projectedMatrix) {
        setUniform("transform", projectedMatrix);
    }

    @Override
    public void updateUniformWorldMatrix(Matrix4 worldMatrix) {
        setUniform("transform", worldMatrix);
    }
}
