package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.graphics.Texture;

public class Frame extends Widget {

    private Texture texture;

    public Frame(Texture texture, float x, float y, float width, float height) {
        super(x, y, width, height);

        material.setTexture("diffuse", texture);
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
