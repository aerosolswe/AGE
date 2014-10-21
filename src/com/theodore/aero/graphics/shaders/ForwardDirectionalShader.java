package com.theodore.aero.graphics.shaders;

import com.theodore.aero.components.DirectionalLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.math.Matrix4;

public class ForwardDirectionalShader extends Shader {

    public ForwardDirectionalShader() {
        super("forward-directional");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);
        setAttribLocation("normal", 2);
        setAttribLocation("tangent", 3);

        compileShader();

        addUniform("model");
        addUniform("MVP");
        addUniform("lightMatrix");

        addUniform("diffuse");
        addUniform("normalMap");
        addUniform("dispMap");
        addUniform("shadowMap");

        addUniform("shadowVarianceMin");
        addUniform("shadowLightBleedReduction");

        addUniform("textureRepeat");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("scale");
        addUniform("bias");
        addUniform("color");
        addUniform("alpha");

        addUniform("eyePos");

        addUniform("directionalLight.base.color");
        addUniform("directionalLight.base.intensity");
        addUniform("directionalLight.direction");
    }

    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);
        Matrix4 lightMatrix = graphics.getMatrix4("lightMatrix").mul(worldMatrix);

        setUniform("model", worldMatrix);
        setUniform("MVP", MVPMatrix);
        setUniform("lightMatrix", lightMatrix);

        setUniform("color", material.getVector3("color"));
        setUniformf("specularIntensity", material.getFloat("specularIntensity"));
        setUniformf("specularPower", material.getFloat("specularPower"));
        setUniformf("scale", material.getFloat("scale"));
        setUniformf("bias", material.getFloat("bias"));
        setUniformi("textureRepeat", material.getInteger("textureRepeat"));
        setUniformf("alpha", material.getFloat("alpha"));
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());
        setUniformf("shadowVarianceMin", graphics.getFloat("shadowVarianceMin"));
        setUniformf("shadowLightBleedReduction", graphics.getFloat("shadowLightBleedReduction"));

        setUniformi("diffuse", graphics.getSamplerSlot("diffuse"));
        setUniformi("normalMap", graphics.getSamplerSlot("normalMap"));
        setUniformi("dispMap", graphics.getSamplerSlot("dispMap"));
        setUniformi("shadowMap", graphics.getSamplerSlot("shadowMap"));

        setUniformDirectionalLight("directionalLight", (DirectionalLight) graphics.getActiveLight());
    }
}
