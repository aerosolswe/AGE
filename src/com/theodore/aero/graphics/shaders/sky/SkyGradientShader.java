package com.theodore.aero.graphics.shaders.sky;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL13;

public class SkyGradientShader extends Shader {

    private static final SkyGradientShader instance = new SkyGradientShader();

    public static SkyGradientShader getInstance() {
        return instance;
    }

    private SkyGradientShader() {
        super();

        String vertexShaderText = loadShader("sky/skygradient.vs");
        String fragmentShaderText = loadShader("sky/skygradient.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();

        addUniform("MVP");
        addUniform("model");
        addUniform("glow");
        addUniform("color");
        addUniform("lightPos");
    }

    public void updateUniforms(Transform transform, Graphics graphics, Vector3 lightPosition) {
        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getView().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);
        setUniform("model", worldMatrix);
        setUniformi("glow", GL13.GL_TEXTURE1);
        setUniformi("color", GL13.GL_TEXTURE0);
        setUniform("lightPos", lightPosition);
    }
}
