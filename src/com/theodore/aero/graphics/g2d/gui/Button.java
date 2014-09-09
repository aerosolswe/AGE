package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.graphics.Texture;

public class Button extends Widget {

    private Texture upTexture;
    private Texture downTexture;

    public Button(Texture upTexture, Texture downTexture, float x, float y, float width, float height) {
        super();

        this.upTexture = upTexture;
        this.downTexture = downTexture;

        material.setDiffuseTexture(upTexture);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (mousePressed()) {
            setTexture(downTexture);
        }
        if (mouseReleased()) {
            setTexture(upTexture);
        }
    }

    public void setTexture(Texture texture) {
        material.setDiffuseTexture(texture);
    }

}
