package com.theodore.aero.graphics.filters;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.shaders.FxaaFilterShader;
import com.theodore.aero.graphics.shaders.Shader;

public class FxaaFilter extends Filter {

    private Shader fxaaFilterShader;

    public FxaaFilter(float fxaa) {
        fxaaFilterShader = new FxaaFilterShader();

        Aero.graphics.setFloat("fxaaSpanMax", fxaa);
        Aero.graphics.setFloat("fxaaReduceMin", 1.0f / 128.0f);
        Aero.graphics.setFloat("fxaaReduceMul", 1.0f / fxaa);
        Aero.graphics.setFloat("fxaaAspectDistortion", 150.0f);
    }

    @Override
    public void renderFilter(Graphics graphics, Texture render, Texture renderTarget, GameObject object, Camera altCamera) {
        applyFilter(fxaaFilterShader, render, null, object, altCamera, true);
    }
}
