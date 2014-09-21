package com.theodore.aero.graphics.shaders.shadow;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class PointLightShadowShader extends Shader {
    private static final PointLightShadowShader instance = new PointLightShadowShader();

    public static PointLightShadowShader getInstance() {
        return instance;
    }

    private PointLightShadowShader() {
        super();

        String vertexShaderText = loadShader("shadow/point-shadow-map.vs");
        String fragmentShaderText = loadShader("shadow/point-shadow-map.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();

        addUniform("MVP");
        addUniform("model");
        addUniform("lightWorldPos");
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
        setUniform("model", worldMatrix);
        setUniform("lightWorldPos", graphics.getActiveLight().getTransform().getTransformedPos());
    }
}
