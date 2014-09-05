package com.theodore.aero.graphics.shaders;

import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;

public class ShadowMapShader extends Shader {

    private static final ShadowMapShader instance = new ShadowMapShader();

    public static ShadowMapShader getInstance() {
        return instance;
    }

    private ShadowMapShader() {
        super();
        addVertexShaderFromFile("shadow/shadowMap.vs");
        addFragmentShaderFromFile("shadow/shadowMap.fs");
        compileShader();

        addUniform("transformProjected");
    }

    public void updateUniforms(Matrix4 worldMatrix, Matrix4 projectedMatrix, Material material) {
        setUniform("transformProjected", projectedMatrix);
    }
}
