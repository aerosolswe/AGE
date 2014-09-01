package com.theodore.aero.graphics.shaders.forward;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Screen;
import com.theodore.aero.graphics.Shader;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.lighting.BaseLight;
import com.theodore.aero.graphics.g3d.lighting.PointLight;
import com.theodore.aero.graphics.g3d.lighting.SpotLight;
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

        addUniform("diffuse");
        addUniform("normalMap");
        addUniform("bumpMap");

        addUniform("textureRepeat");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("scale");
        addUniform("bias");
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
    public void updateUniforms(Matrix4 worldMatrix, Matrix4 projectedMatrix, Material material) {
        Screen screen = Aero.getActiveScreen();

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
        setUniform("MVP", projectedMatrix);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniformf("scale", material.getHeightScale());
        setUniformf("bias", material.getHeightBias());
        setUniformi("textureRepeat", material.getTextureRepeat());
        setUniform("eyePos", Transform.getCamera().getPosition());

        setUniformi("diffuse", Texture.DIFFUSE_TEXTURE);
        setUniformi("normalMap", Texture.NORMAL_TEXTURE);
        setUniformi("bumpMap", Texture.HEIGHT_TEXTURE);

        setUniformSpotLight("spotLight", (SpotLight) screen.getActiveLight());
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
        setUniform(uniformName + ".direction", spotLight.getDirection().getForward());
        setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
    }
}