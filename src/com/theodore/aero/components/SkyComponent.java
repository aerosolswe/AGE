package com.theodore.aero.components;

import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.graphics.sky.Sky;
import com.theodore.aero.math.Vector3;

public class SkyComponent extends GameComponent {

    private Sky sky;

    public SkyComponent(Sky sky) {
        this.sky = sky;
    }

    @Override
    public void renderBasic(Shader shader, Graphics graphics) {
        super.renderBasic(shader, graphics);

        sky.render(graphics);

    }

}
