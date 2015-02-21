package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Vector2;

public class Button extends Widget {

    private Texture upTexture;
    private Texture downTexture;

    public Button(Texture upTexture, Texture downTexture, float x, float y, float width, float height) {
        super(x, y, width, height);

        this.upTexture = upTexture;
        this.downTexture = downTexture;

        material.setTexture("diffuse", upTexture);
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
        material.setTexture("diffuse", texture);
    }

}
