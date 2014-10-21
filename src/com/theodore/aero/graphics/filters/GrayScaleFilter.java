package com.theodore.aero.graphics.filters;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.shaders.GrayScaleFilterShader;
import com.theodore.aero.graphics.shaders.NullFilterShader;
import com.theodore.aero.graphics.shaders.Shader;

public class GrayScaleFilter extends Filter {

    private Shader grayScaleShader;

    public GrayScaleFilter() {
        grayScaleShader = new GrayScaleFilterShader();
    }

    @Override
    public void renderFilter(Graphics graphics, Texture render, Texture renderTarget, GameObject object, Camera altCamera) {
        applyFilter(grayScaleShader, renderTarget, renderTarget, object, altCamera, false);
    }
}
