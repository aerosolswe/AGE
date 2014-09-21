package com.theodore.aero.graphics.shaders.sky;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL13;

public class SkyBoxShader extends Shader {

    private static final SkyBoxShader instance = new SkyBoxShader();

    public static SkyBoxShader getInstance() {
        return instance;
    }

    private SkyBoxShader() {
        super();

        String vertexShaderText = loadShader("sky/cubemap-skybox.vs");
        String fragmentShaderText = loadShader("sky/cubemap-skybox.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();

        addUniform("MVP");
        addUniform("cubeMapTexture");
        addUniform("color");
    }

    public void updateUniforms(Transform transform, Graphics graphics, Vector3 color) {
        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getView().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);
        setUniform("color", color);
        setUniformi("cubeMapTexture", GL13.GL_TEXTURE0);
    }
}
