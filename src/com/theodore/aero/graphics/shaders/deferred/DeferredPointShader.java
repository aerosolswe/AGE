package com.theodore.aero.graphics.shaders.deferred;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.DirectionalLight;
import com.theodore.aero.components.PointLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector2;

public class DeferredPointShader extends Shader {

    private static final DeferredPointShader instance = new DeferredPointShader();

    public static DeferredPointShader getInstance() {
        return instance;
    }

    private DeferredPointShader() {
        super();

        addVertexShaderFromFile("deferredrendering/light-pass.vs");
        addFragmentShaderFromFile("deferredrendering/point-light-pass.fs");

        compileShader();

        addUniform("MVP");

        addUniform("positionMap");
        addUniform("colorMap");
        addUniform("normalMap");

        addUniform("specularIntensity");
        addUniform("specularPower");

        addUniform("eyePos");
        addUniform("screenSize");

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

        setUniform("MVP", MVPMatrix);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniform("eyePos", graphics.getMainCamera().getTransform().getPosition());
        setUniform("screenSize", new Vector2(Window.getWidth(), Window.getHeight()));

        setUniformi("positionMap", Texture.GBUFFER_TEXTURE_TYPE_POSITION);
        setUniformi("colorMap", Texture.GBUFFER_TEXTURE_TYPE_DIFFUSE);
        setUniformi("normalMap", Texture.GBUFFER_TEXTURE_TYPE_NORMAL);

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
