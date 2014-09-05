package com.theodore.aero.graphics.shaders.forward;

import com.theodore.aero.Components.BaseLight;
import com.theodore.aero.Components.PointLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class ForwardPointShader extends Shader {

    private static final ForwardPointShader instance = new ForwardPointShader();

    public static ForwardPointShader getInstance() {
        return instance;
    }

    private ForwardPointShader() {
        super();

        addVertexShaderFromFile("forwardrendering/forward.vs");
        addFragmentShaderFromFile("forwardrendering/forward-point.fs");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);
        setAttribLocation("normal", 2);
        setAttribLocation("tangent", 3);

        compileShader();

        addUniform("model");
        addUniform("MVP");

        addUniform("diffuse");
        addUniform("normalMap");
        addUniform("bumpMap");

        addUniform("textureRepeat");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("scale");
        addUniform("bias");
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

        if (material.getNormalTexture() != null)
            material.getNormalTexture().bind(Texture.NORMAL_TEXTURE);
        else
            Texture.unbind();

        if (material.getBumpTexture() != null)
            material.getBumpTexture().bind(Texture.HEIGHT_TEXTURE);
        else
            Texture.unbind();

        if (material.getDiffuseTexture() != null)
            material.getDiffuseTexture().bind(Texture.DIFFUSE_TEXTURE);
        else
            Texture.unbind();

        setUniform("model", worldMatrix);
        setUniform("MVP", MVPMatrix);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniformf("scale", material.getHeightScale());
        setUniformf("bias", material.getHeightBias());
        setUniformi("textureRepeat", material.getTextureRepeat());
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());

        setUniformi("diffuse", Texture.DIFFUSE_TEXTURE);
        setUniformi("normalMap", Texture.NORMAL_TEXTURE);
        setUniformi("bumpMap", Texture.HEIGHT_TEXTURE);

        setUniformPointLight("pointLight", (PointLight) graphics.getActiveLight());
    }

    public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformPointLight(String uniformName, PointLight pointLight) {
        setUniformBaseLight(uniformName + ".base", pointLight);
        setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
        setUniform(uniformName + ".position", pointLight.getTransform().getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }
}