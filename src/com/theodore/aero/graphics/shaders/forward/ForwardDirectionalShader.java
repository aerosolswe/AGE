package com.theodore.aero.graphics.shaders.forward;

import com.theodore.aero.Components.BaseLight;
import com.theodore.aero.Components.DirectionalLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.ShadowMap;
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

        addUniform("lightTransform0");
        addUniform("lightTransform1");
        addUniform("lightTransform2");
        addUniform("lightTransform3");

        addUniform("diffuse");
        addUniform("normalMap");
        addUniform("bumpMap");

        addUniform("shadowMap0");
        addUniform("shadowMap1");
        addUniform("shadowMap2");
        addUniform("shadowMap3");

        addUniform("shadowMapSize");
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
        ShadowMap shadowMap = graphics.getActiveLight().getShadowMap();

        shadowMap.shadowDepthTexture[0].bind(Texture.SHADOW_TEXTURE_0);
        shadowMap.shadowDepthTexture[1].bind(Texture.SHADOW_TEXTURE_1);
        shadowMap.shadowDepthTexture[2].bind(Texture.SHADOW_TEXTURE_2);
        shadowMap.shadowDepthTexture[3].bind(Texture.SHADOW_TEXTURE_3);

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

        setUniform("lightTransform0", shadowMap.lightMatrix[0]);
        setUniform("lightTransform1", shadowMap.lightMatrix[1]);
        setUniform("lightTransform2", shadowMap.lightMatrix[2]);
        setUniform("lightTransform3", shadowMap.lightMatrix[3]);
        setUniform("model", worldMatrix);
        setUniform("MVP", MVPMatrix);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniformf("scale", material.getHeightScale());
        setUniformf("bias", material.getHeightBias());
        setUniformi("textureRepeat", material.getTextureRepeat());
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());
        setUniformf("shadowMapSize", graphics.getShadowSize());

        setUniformi("diffuse", Texture.DIFFUSE_TEXTURE);
        setUniformi("normalMap", Texture.NORMAL_TEXTURE);
        setUniformi("bumpMap", Texture.HEIGHT_TEXTURE);
        setUniformi("shadowMap0", Texture.SHADOW_TEXTURE_0);
        setUniformi("shadowMap1", Texture.SHADOW_TEXTURE_1);
        setUniformi("shadowMap2", Texture.SHADOW_TEXTURE_2);
        setUniformi("shadowMap3", Texture.SHADOW_TEXTURE_3);

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