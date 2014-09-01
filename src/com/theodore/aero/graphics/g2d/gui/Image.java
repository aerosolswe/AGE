package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Mesh;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.math.Vector3;

public class Image extends Widget {

    public Image(Texture texture) {
        this(texture, 0, 0, 64, 64);
    }

    public Image(Texture texture, float x, float y) {
        this(texture, x, y, 64, 64);
    }

    public Image(Texture texture, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        shader = BasicShader.getInstance();
        transform = new Transform();
        mesh = Mesh.get("rect");

        material = new Material();
        material.setDiffuseTexture(texture);

        transform.setPosition(new Vector3(x, y + height / 2, 0));
        transform.setScale(new Vector3(width, height, height));
    }

    @Override
    public void update(float delta) {

    }
}
