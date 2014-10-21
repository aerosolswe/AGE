package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class SkyBoxShader extends Shader {

    public SkyBoxShader() {
        super("skybox");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);

        compileShader();

        addUniform("MVP");
        addUniform("cubeMapTexture");
        addUniform("color");
    }


    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);
        setUniform("color", material.getVector3("color"));
        setUniformi("cubeMapTexture", graphics.getSamplerSlot("diffuse"));
    }

}
