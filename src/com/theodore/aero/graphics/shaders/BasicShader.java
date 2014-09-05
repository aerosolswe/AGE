package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
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
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        if (material.getDiffuseTexture() != null)
            material.getDiffuseTexture().bind(Texture.DIFFUSE_TEXTURE);
        else
            Texture.unbind();

        setUniform("transform", MVPMatrix);
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
