package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class ShadowBlurFilterShader extends Shader {

    public ShadowBlurFilterShader() {
        super("filter-gausBlur7x1");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);

        compileShader();

        addUniform("MVP");
        addUniform("filterTexture");
        addUniform("blurScale");
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

        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));

        setUniform("MVP", MVPMatrix);

        setUniform("blurScale", graphics.getVector3("blurScale"));
    }

}
