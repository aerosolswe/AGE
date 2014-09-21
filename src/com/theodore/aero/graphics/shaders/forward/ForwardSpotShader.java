package com.theodore.aero.graphics.shaders.forward;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.PointLight;
import com.theodore.aero.components.SpotLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class ForwardSpotShader extends Shader {

    private static final ForwardSpotShader instance = new ForwardSpotShader();

    public static ForwardSpotShader getInstance() {
        return instance;
    }

    private ForwardSpotShader() {
        super();

        addVertexShaderFromFile("forwardrendering/forward.vs");
        addFragmentShaderFromFile("forwardrendering/forward-spot.fs");

        compileShader();

        addUniform("model");
        addUniform("MVP");
        addUniform("lightMatrix");

        addUniform("diffuse");
        addUniform("normalMap");
        addUniform("bumpMap");

        addUniform("shadowMap");
        addUniform("shadowVarianceMin");
        addUniform("shadowLightBleedReduction");

        addUniform("textureRepeat");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("scale");
        addUniform("bias");
        addUniform("alpha");
        addUniform("color");

        addUniform("eyePos");

        addUniform("spotLight.pointLight.base.color");
        addUniform("spotLight.pointLight.base.intensity");
        addUniform("spotLight.pointLight.atten.constant");
        addUniform("spotLight.pointLight.atten.linear");
        addUniform("spotLight.pointLight.atten.exponent");
        addUniform("spotLight.pointLight.position");
        addUniform("spotLight.pointLight.range");
        addUniform("spotLight.direction");
        addUniform("spotLight.cutoff");
    }

    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);
        Matrix4 lightMatrix = graphics.getLightMatrix().mul(worldMatrix);
        ShadowInfo shadowInfo = graphics.getActiveLight().getShadowInfo();

        if (material.getNormalTexture() != null)
            material.getNormalTexture().bind(Texture.NORMAL_TEXTURE);
        else
            com.theodore.aero.graphics.Texture.unbind();

        if (material.getBumpTexture() != null)
            material.getBumpTexture().bind(Texture.HEIGHT_TEXTURE);
        else
            com.theodore.aero.graphics.Texture.unbind();

        if (material.getDiffuseTexture() != null)
            material.getDiffuseTexture().bind(Texture.DIFFUSE_TEXTURE);
        else
            Texture.unbind();

        setUniform("model", worldMatrix);
        setUniform("MVP", MVPMatrix);
        setUniform("lightMatrix", lightMatrix);

        setUniform("color", material.getColor());
        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniformf("scale", material.getHeightScale());
        setUniformf("bias", material.getHeightBias());
        setUniformf("alpha", material.getAlpha());
        setUniformi("textureRepeat", material.getTextureRepeat());
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());

        if (shadowInfo != null) {
            setUniformf("shadowVarianceMin", shadowInfo.getMinVariance());
            setUniformf("shadowLightBleedReduction", shadowInfo.getLightBleedReductionAmount());
        }

        setUniformi("diffuse", Texture.DIFFUSE_TEXTURE);
        setUniformi("normalMap", Texture.NORMAL_TEXTURE);
        setUniformi("bumpMap", Texture.HEIGHT_TEXTURE);
        setUniformi("shadowMap", Texture.SHADOW_MAP_TEXTURE);

        setUniformSpotLight("spotLight", (SpotLight) graphics.getActiveLight());
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

    public void setUniformSpotLight(String uniformName, SpotLight spotLight) {
        setUniformPointLight(uniformName + ".pointLight", spotLight);
        setUniform(uniformName + ".direction", spotLight.getTransform().getRotation().getForward());
        setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
    }
}