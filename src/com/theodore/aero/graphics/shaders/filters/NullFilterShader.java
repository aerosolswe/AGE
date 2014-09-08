package com.theodore.aero.graphics.shaders.filters;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class NullFilterShader extends Shader {
    private static final NullFilterShader instance = new NullFilterShader();

    public static NullFilterShader getInstance() {
        return instance;
    }

    private NullFilterShader() {
        super();

        String vertexShaderText = loadShader("filters/filter-null.vs");
        String fragmentShaderText = loadShader("filters/filter-null.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();

        addUniform("MVP");
        addUniform("filterTexture");
    }

    @Override
    public void updateTextureUniform(String uniformName, int texture) {
        setUniformi(uniformName, texture);
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

        setUniform("MVP", MVPMatrix);
    }
}
