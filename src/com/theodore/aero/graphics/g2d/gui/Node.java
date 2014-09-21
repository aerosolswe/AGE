package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.graphics.Texture;

public class Node extends Widget {

    public Node(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        material = null;
    }
}
