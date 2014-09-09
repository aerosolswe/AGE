package com.theodore.aero.graphics.shaders.ortho;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

public class OrthographicShader extends Shader {

    private static final OrthographicShader instance = new OrthographicShader();

    public static OrthographicShader getInstance() {
        return instance;
    }

    private OrthographicShader() {
        super();

        addVertexShaderFromFile("ortho/orthographic.vs");
        addFragmentShaderFromFile("ortho/orthographic.fs");

        compileShader();

        addUniform("transform");
        addUniform("color");
        addUniform("alpha");
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

        setUniform("transform", MVPMatrix);
        setUniform("color", material.getColor());
        setUniformf("alpha", material.getAlpha());
        setUniformi("textureRepeat", material.getTextureRepeat());
    }

}
