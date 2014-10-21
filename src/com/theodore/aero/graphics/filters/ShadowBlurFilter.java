package com.theodore.aero.graphics.filters;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.shaders.ShadowBlurFilterShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Vector3;

public class ShadowBlurFilter extends Filter {

    private Shader gausBlurFilterShader;

    public ShadowBlurFilter(float blurAmount) {
        setFloat("blurAmount", blurAmount);
        gausBlurFilterShader = new ShadowBlurFilterShader();
    }

    @Override
    public void renderFilter(Graphics graphics, Texture render, Texture renderTarget, GameObject object, Camera altCamera) {
        graphics.setVector3("blurScale", new Vector3(getFloat("blurAmount") / render.getWidth(), 0.0f, 0.0f));
        applyFilter(gausBlurFilterShader, renderTarget, render, object, altCamera, true);

        graphics.setVector3("blurScale", new Vector3(0.0f, getFloat("blurAmount") / (render.getHeight()), 0.0f));
        applyFilter(gausBlurFilterShader, render, renderTarget, object, altCamera, true);
    }
}
