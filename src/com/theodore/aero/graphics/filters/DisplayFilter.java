package com.theodore.aero.graphics.filters;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.shaders.FxaaFilterShader;
import com.theodore.aero.graphics.shaders.NullFilterShader;
import com.theodore.aero.graphics.shaders.Shader;

public class DisplayFilter extends Filter {

    private Shader nullFilterShader;

    public DisplayFilter() {
        nullFilterShader = new NullFilterShader();
    }

    @Override
    public void renderFilter(Graphics graphics, Texture render, Texture renderTarget, GameObject object, Camera altCamera) {
        applyFilter(nullFilterShader, render, null, object, altCamera, true);
    }
}
