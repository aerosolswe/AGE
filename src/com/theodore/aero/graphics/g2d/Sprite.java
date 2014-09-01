package com.theodore.aero.graphics.g2d;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Mesh;
import com.theodore.aero.graphics.Shader;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.math.Vector3;

public class Sprite {

    private Transform transform;
    private Mesh mesh;
    private Material material;
    private Shader shader;

    private float x;
    private float y;
    private float width;
    private float height;

    public Sprite(Texture texture) {
        this(0, 0, texture.getWidth(), texture.getHeight(), texture);
    }

    public Sprite(float x, float y, Texture texture) {
        this(x, y, texture.getWidth(), texture.getHeight(), texture);
    }

    public Sprite(float x, float y, float width, float height, Texture texture) {
        this.transform = new Transform();
        this.mesh = Mesh.get("rect");
        this.material = new Material();
        this.material.setDiffuseTexture(texture);
        this.shader = BasicShader.getInstance();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        transform.setPosition(new Vector3(x, y, 0));
        transform.setScale(new Vector3(width, height, height));
    }

    public void draw() {
        if (mesh != null) {
            shader.bind();
            shader.updateUniforms(transform.calcModel(), transform.calcMVP(), material);
            mesh.draw();
        }
    }

    public void setPosition(float x, float y) {
        transform.setPosition(new Vector3(x, y, 0));
    }

    public Transform getTransform() {
        return transform;
    }

    public Material getMaterial() {
        return material;
    }

    public Mesh getMesh() {
        return mesh;
    }

}
