package com.theodore.aero.graphics.shaders;

import com.theodore.aero.components.PointLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class ForwardPointShader extends Shader {

    public ForwardPointShader() {
        super("forward-point");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);
        setAttribLocation("normal", 2);
        setAttribLocation("tangent", 3);

        compileShader();

        addUniform("model");
        addUniform("MVP");
//        addUniform("lightMatrix");

        addUniform("diffuse");
        addUniform("normalMap");
        addUniform("bumpMap");

        addUniform("textureRepeat");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("scale");
        addUniform("bias");
        addUniform("color");
        addUniform("alpha");
        addUniform("eyePos");

        addUniform("pointLight.base.color");
        addUniform("pointLight.base.intensity");
        addUniform("pointLight.atten.constant");
        addUniform("pointLight.atten.linear");
        addUniform("pointLight.atten.exponent");

        addUniform("pointLight.position");
        addUniform("pointLight.range");
    }


    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);
        Matrix4 lightMatrix = graphics.getMatrix4("lightMatrix").mul(worldMatrix);

        setUniform("model", worldMatrix);
        setUniform("MVP", MVPMatrix);
//        setUniform("lightMatrix", lightMatrix);

        setUniform("color", material.getVector3("color"));
        setUniformf("specularIntensity", material.getFloat("specularIntensity"));
        setUniformf("specularPower", material.getFloat("specularPower"));
        setUniformf("scale", material.getFloat("scale"));
        setUniformf("bias", material.getFloat("bias"));
        setUniformi("textureRepeat", material.getInteger("textureRepeat"));
        setUniformf("alpha", material.getFloat("alpha"));
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());

        setUniformi("diffuse", graphics.getSamplerSlot("diffuse"));
        setUniformi("normalMap", graphics.getSamplerSlot("normalMap"));
        setUniformi("dispMap", graphics.getSamplerSlot("dispMap"));

        setUniformPointLight("pointLight", (PointLight) graphics.getActiveLight());
    }

}
