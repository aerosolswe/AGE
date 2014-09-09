package com.theodore.aero.graphics.shaders.cubemaps;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.CubeMap;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.SkyBox;
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

        String vertexShaderText = loadShader("cubemaps/cubemap-skybox.vs");
        String fragmentShaderText = loadShader("cubemaps/cubemap-skybox.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();

        addUniform("MVP");
        addUniform("cubeMapTexture");
        addUniform("color");
    }

    public void updateUniforms(Transform transform, Graphics graphics, SkyBox skyBox) {
        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getView().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);
        setUniform("color", skyBox.getColor());
        setUniformi("cubeMapTexture", GL13.GL_TEXTURE0);
    }
}
