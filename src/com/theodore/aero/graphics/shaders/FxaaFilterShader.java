package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class FxaaFilterShader extends Shader {

    public FxaaFilterShader() {
        super("filter-fxaa");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);

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

        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));

        setUniformf("fxaaSpanMax", graphics.getFloat("fxaaSpanMax"));
        setUniformf("fxaaReduceMin", graphics.getFloat("fxaaReduceMin"));
        setUniformf("fxaaReduceMul", graphics.getFloat("fxaaReduceMul"));
        setUniform("inverseFilterTextureSize", graphics.getVector3("inverseFilterTextureSize"));

        setUniform("MVP", MVPMatrix);
    }

}
