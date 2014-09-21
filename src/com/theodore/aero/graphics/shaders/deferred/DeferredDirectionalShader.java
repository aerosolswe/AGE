package com.theodore.aero.graphics.shaders.deferred;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.DirectionalLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector2;

public class DeferredDirectionalShader extends Shader {

    private static final DeferredDirectionalShader instance = new DeferredDirectionalShader();

    public static DeferredDirectionalShader getInstance() {
        return instance;
    }

    private DeferredDirectionalShader() {
        super();

        addVertexShaderFromFile("deferredrendering/light-pass.vs");
        addFragmentShaderFromFile("deferredrendering/dir-light-pass.fs");

        compileShader();

        addUniform("MVP");

        addUniform("positionMap");
        addUniform("colorMap");
        addUniform("normalMap");

        addUniform("specularIntensity");
        addUniform("specularPower");

        addUniform("eyePos");
        addUniform("screenSize");

        addUniform("directionalLight.base.color");
        addUniform("directionalLight.base.intensity");
        addUniform("directionalLight.direction");
    }

    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());
        setUniform("screenSize", new Vector2(Window.getWidth(), Window.getHeight()));

        setUniformi("positionMap", Texture.GBUFFER_TEXTURE_TYPE_POSITION);
        setUniformi("colorMap", Texture.GBUFFER_TEXTURE_TYPE_DIFFUSE);
        setUniformi("normalMap", Texture.GBUFFER_TEXTURE_TYPE_NORMAL);

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
