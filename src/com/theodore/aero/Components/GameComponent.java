package com.theodore.aero.components;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.shaders.Shader;

public class GameComponent {

    protected GameObject parent;

    public void input(float delta) {
    }

    public void update(float delta) {
    }

    public void render(Shader shader, Graphics graphics) {
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public Transform getTransform() {
        return parent.getTransform();
    }

    public void addToEngine() {
    }

}
