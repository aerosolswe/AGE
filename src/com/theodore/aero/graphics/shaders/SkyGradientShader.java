package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class SkyGradientShader extends Shader {

    public SkyGradientShader() {
        super("skygradient");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);

        compileShader();

        addUniform("MVP");
        addUniform("glow");
        addUniform("color");
        addUniform("lightPos");
    }


    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);
        setUniformi("color", graphics.getSamplerSlot("diffuse"));
        setUniformi("glow", graphics.getSamplerSlot("normalMap"));
        setUniform("lightPos", graphics.getVector3("lightPos"));
    }

}
