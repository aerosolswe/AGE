package com.theodore.aero.graphics.shaders.Shadow;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class ShadowMapShader extends Shader {

    private static final ShadowMapShader instance = new ShadowMapShader();

    public static ShadowMapShader getInstance() {
        return instance;
    }

    private ShadowMapShader() {
        super();

        String vertexShaderText = loadShader("shadow/shadow-map.vs");
        String fragmentShaderText = loadShader("shadow/shadow-map.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

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
