package com.theodore.aero.components;

import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.graphics.sky.Sky;
import com.theodore.aero.math.Vector3;

public class SkyComponent extends GameComponent {

    private Sky sky;

    public SkyComponent(Sky sky){
        this.sky = sky;
    }

    @Override
    public void render(Shader shader, Graphics graphics) {
        super.render(shader, graphics);

        sky.render(graphics);

    }
}
