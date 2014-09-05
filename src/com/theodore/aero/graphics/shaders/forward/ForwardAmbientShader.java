package com.theodore.aero.graphics.shaders.forward;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;

public class ForwardAmbientShader extends Shader {

    private static final ForwardAmbientShader instance = new ForwardAmbientShader();

    public static ForwardAmbientShader getInstance() {
        return instance;
    }

    private ForwardAmbientShader() {
        super();

        String vertexShaderText = loadShader("forwardrendering/forward.vs");
        String fragmentShaderText = loadShader("forwardrendering/forward-ambient.fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);

        compileShader();

        addUniform("MVP");
        addUniform("ambientIntensity");
        addUniform("textureRepeat");
    }

    @Override
    public void updateUniforms(Transform transform, Material material, Graphics graphics) {
        super.updateUniforms(transform, material, graphics);

        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        if (material.getDiffuseTexture() != null)
            material.getDiffuseTexture().bind(Texture.DIFFUSE_TEXTURE);
        else
            Texture.unbind();

        setUniform("MVP", MVPMatrix);
        setUniformi("textureRepeat", material.getTextureRepeat());
        setUniform("ambientIntensity", Aero.graphics.getAmbientLight());
    }

    /*@Override
    public void updateUniforms(Matrix4 worldMatrix, Matrix4 projectedMatrix, Material material) {
        if (material.getDiffuseTexture() != null)
            material.getDiffuseTexture().bind(Texture.DIFFUSE_TEXTURE);
        else
            Texture.unbind();

        setUniform("MVP", projectedMatrix);
        setUniformi("textureRepeat", material.getTextureRepeat());
        setUniform("ambientIntensity", Aero.graphics.getAmbientLight());
    }*/
}
