package com.theodore.aero.graphics.shaders.filters;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;

public class FxaaFilterShader extends Shader {

    private static final FxaaFilterShader instance = new FxaaFilterShader();

    public static FxaaFilterShader getInstance() {
        return instance;
    }

    private FxaaFilterShader() {
        super();

        String vertexShaderText = loadShader("filters/filter-fxaa.vs");
        String fragmentShaderText = loadShader("filters/filter-fxaa.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();

        addUniform("MVP");
        addUniform("filterTexture");
        addUniform("inverseFilterTextureSize");
        addUniform("fxaaSpanMax");
        addUniform("fxaaReduceMin");
        addUniform("fxaaReduceMul");
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

        float fxaaAspectDistortion = 150.0f;
        float displayTextureAspect = (float) graphics.displayTexture.getWidth() / (float) graphics.displayTexture.getHeight();
        float displayTextureHeightAdditive = displayTextureAspect * fxaaAspectDistortion;

        setUniformf("fxaaSpanMax", Graphics.FXAA_QUALITY);
        setUniformf("fxaaReduceMin", 1.0f / 128.0f);
        setUniformf("fxaaReduceMul", 1.0f / Graphics.FXAA_QUALITY);
        setUniform("inverseFilterTextureSize", new Vector3(1.0f / (float) graphics.displayTexture.getWidth(), 1.0f / ((float) graphics.displayTexture.getHeight() + displayTextureHeightAdditive), 0.0f));

        setUniform("MVP", MVPMatrix);
    }
}
