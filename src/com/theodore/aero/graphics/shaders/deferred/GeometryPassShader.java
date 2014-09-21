package com.theodore.aero.graphics.shaders.deferred;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class GeometryPassShader extends Shader {

    private static final GeometryPassShader instance = new GeometryPassShader();

    public static GeometryPassShader getInstance() {
        return instance;
    }

    private GeometryPassShader() {
        super();

        String vertexShaderText = loadShader("deferredrendering/geometry-pass.vs");
        String fragmentShaderText = loadShader("deferredrendering/geometry-pass.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();

        addUniform("MVP");
        addUniform("model");
        addUniform("colorMap");
    }

    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);
        setUniform("model", worldMatrix);

        setUniformi("colorMap", Texture.GBUFFER_TEXTURE_TYPE_POSITION);
    }
}
