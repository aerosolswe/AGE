package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class BasicShader extends Shader {

    public BasicShader() {
        super("basic");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);

        compileShader();

        addUniform("diffuse");

        addUniform("MVP");
        addUniform("color");
        addUniform("alpha");
        addUniform("textureRepeat");
    }


    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));

        setUniform("MVP", MVPMatrix);
        setUniformi("textureRepeat", material.getInteger("textureRepeat"));
        setUniform("color", material.getVector3("color"));
        setUniformf("alpha", material.getFloat("alpha"));

        setUniformi("diffuse", graphics.getSamplerSlot("diffuse"));
    }

}
