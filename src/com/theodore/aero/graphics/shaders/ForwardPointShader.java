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

        addUniform("viewProjectionMatrix");
        addUniform("model");
        addUniform("MVP");
//        addUniform("lightMatrix");

        addUniform("blendMap");

        addUniform("diffuse");
        addUniform("normalMap");
        addUniform("specularMap");
        addUniform("dispMap");

        addUniform("rdiffuse");
        addUniform("rnormalMap");
        addUniform("rspecularMap");
        addUniform("rdispMap");

        addUniform("gdiffuse");
        addUniform("gnormalMap");
        addUniform("gspecularMap");
        addUniform("gdispMap");

        addUniform("bdiffuse");
        addUniform("bspecularMap");
        addUniform("bnormalMap");
        addUniform("bdispMap");

        addUniform("textureRepeat");

        addUniform("shininess");
        addUniform("scale");
        addUniform("bias");
        addUniform("color");
        addUniform("alpha");
        addUniform("eyePos");

        addUniform("density");
        addUniform("gradient");
        addUniform("skyColor");

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
        setUniform("viewProjectionMatrix", graphics.getMainCamera().getViewProjection());
//        setUniform("lightMatrix", lightMatrix);

        setUniform("color", material.getVector3("color"));
        setUniformf("shininess", material.getFloat("shininess"));
        setUniformf("scale", material.getFloat("scale"));
        setUniformf("bias", material.getFloat("bias"));
        setUniformi("textureRepeat", material.getInteger("textureRepeat"));
        setUniformf("alpha", material.getFloat("alpha"));
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());

        setUniformf("density", graphics.getFloat("fogDensity"));
        setUniformf("gradient", graphics.getFloat("fogGradient"));
        setUniform("skyColor", graphics.getVector3("skyColor"));

        setUniformi("blendMap", graphics.getSamplerSlot("blendMap"));

        setUniformi("diffuse", graphics.getSamplerSlot("diffuse"));
        setUniformi("normalMap", graphics.getSamplerSlot("normalMap"));
        setUniformi("dispMap", graphics.getSamplerSlot("dispMap"));
        setUniformi("specularMap", graphics.getSamplerSlot("specularMap"));

        setUniformi("rdiffuse", graphics.getSamplerSlot("rdiffuse"));
        setUniformi("rnormalMap", graphics.getSamplerSlot("rnormalMap"));
        setUniformi("rdispMap", graphics.getSamplerSlot("rdispMap"));
        setUniformi("rspecularMap", graphics.getSamplerSlot("rspecularMap"));

        setUniformi("gdiffuse", graphics.getSamplerSlot("gdiffuse"));
        setUniformi("gnormalMap", graphics.getSamplerSlot("gnormalMap"));
        setUniformi("gdispMap", graphics.getSamplerSlot("gdispMap"));
        setUniformi("gspecularMap", graphics.getSamplerSlot("gspecularMap"));

        setUniformi("bdiffuse", graphics.getSamplerSlot("bdiffuse"));
        setUniformi("bnormalMap", graphics.getSamplerSlot("bnormalMap"));
        setUniformi("bdispMap", graphics.getSamplerSlot("bdispMap"));
        setUniformi("bspecularMap", graphics.getSamplerSlot("bspecularMap"));

        setUniformPointLight("pointLight", (PointLight) graphics.getActiveLight());
    }

}
