package com.theodore.aero.graphics;

import com.theodore.aero.math.Vector3;

public class Font {

    private Material material;
    private Texture texture;
    private Vector3 color;

    public Font() {
        this(Texture.get("font.png"));
    }

    public Font(Texture texture) {
        this(texture, new Vector3(1, 1, 1));
    }

    public Font(Texture texture, Vector3 color) {
        material = new Material();
        material.setDiffuseTexture(texture);
        material.setColor(color);
        this.texture = texture;
        this.color = color;
    }

    public Material getMaterial() {
        return material;
    }
}
