package com.theodore.aero.graphics.filters;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.resourceManagement.MappedValues;

public abstract class Filter extends MappedValues {

    public abstract void renderFilter(Graphics graphics, Texture render, Texture renderTarget, GameObject object, Camera altCamera);

    protected void applyFilter(Shader filter, Texture source, Texture dest, GameObject object, Camera altCamera, boolean unbind) {
        assert (source != dest);
        if (dest == null)
            Window.bindAsRenderTarget();
        else
            dest.bindAsRenderTarget();

        filter.updateTextureUniform("filterTexture", source.getID());

        altCamera.setProjection(new Matrix4().initIdentity());
        altCamera.getTransform().setPosition(new Vector3());
        altCamera.getTransform().setRotation(new Quaternion(new Vector3(0, 1, 0), (float) Math.toRadians(180)));

        Graphics graphics = Aero.graphics;

        Camera tmp = graphics.getMainCamera();
        graphics.setMainCamera(altCamera);

        Aero.graphicsUtil.clearDepth();
        object.renderAll(filter, graphics);

        if(unbind)
            filter.updateTextureUniform("filterTexture", 0);

        graphics.setMainCamera(tmp);

    }
}
