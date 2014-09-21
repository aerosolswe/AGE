package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class NullShader extends Shader {
    private static final NullShader instance = new NullShader();

    public static NullShader getInstance() {
        return instance;
    }

    private NullShader() {
        super();

        addVertexShaderFromFile("null/null.vs");
        addFragmentShaderFromFile("null/null.fs");

        compileShader();

        addUniform("MVP");
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
