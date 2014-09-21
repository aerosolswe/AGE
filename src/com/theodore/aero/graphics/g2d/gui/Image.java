package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.graphics.Texture;

public class Image extends Widget {

    private Texture texture;

    public Image(Texture texture, float x, float y, float width, float height) {
        super(x, y, width, height);

        material.setDiffuseTexture(texture);
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        material.setDiffuseTexture(texture);
    }
}
