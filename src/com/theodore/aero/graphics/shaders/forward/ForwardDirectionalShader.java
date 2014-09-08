package com.theodore.aero.graphics.shaders.forward;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.DirectionalLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class ForwardDirectionalShader extends Shader {

    private static final ForwardDirectionalShader instance = new ForwardDirectionalShader();

    public static ForwardDirectionalShader getInstance() {
        return instance;
    }

    private ForwardDirectionalShader() {
        super();

        addVertexShaderFromFile("forwardrendering/forward.vs");
        addFragmentShaderFromFile("forwardrendering/forward-directional.fs");

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

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniformf("scale", material.getHeightScale());
        setUniformf("bias", material.getHeightBias());
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

        setUniformDirectionalLight("directionalLight", (DirectionalLight) graphics.getActiveLight());
    }

    public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
        setUniformBaseLight(uniformName + ".base", directionalLight);
        setUniform(uniformName + ".direction", directionalLight.getDirection().getForward());
    }

}