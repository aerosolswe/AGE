package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.sky.SkyAtmosphere;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;

public class SkyAtmosphereShader extends Shader {

    public SkyAtmosphereShader() {
        super("skyatmosphere");

        setAttribLocation("position", 0);
        setAttribLocation("texCoord", 1);

        compileShader();

        addUniform("MVP");
        addUniform("lightDir");
        addUniform("cameraPos");

        addUniform("cameraHeight");

        addUniform("sunAngle");
        addUniform("starBrightness");

        addUniform("krESun");
        addUniform("kmESun");
        addUniform("kr4PI");
        addUniform("km4PI");

        addUniform("innerRadius");
        addUniform("domeRadius");

        addUniform("scale");
        addUniform("scaleDepth");
        addUniform("scaleOverScaleDepth");

        addUniform("exposure");

        addUniform("g");
        addUniform("g2");

        addUniform("starmap");
    }


    public void updateShader(Transform transform, Material material, Graphics graphics, SkyAtmosphere sky) {
        Matrix4 worldMatrix = transform.getTransformation();
        Matrix4 MVPMatrix = graphics.getMainCamera().getViewProjection().mul(worldMatrix);

        setUniform("MVP", MVPMatrix);
        setUniform("lightDir", sky.getVector3("lightDir"));
        setUniformf("sunAngle", (float) Math.toRadians(sky.getVector3("lightDir").angleBetween(graphics.getMainCamera().getTransform().getTransformedPos())));

        setUniformf("starBrightness", sky.getFloat("starBrightness"));

        setUniformf("krESun", sky.getFloat("krESun"));
        setUniformf("kmESun", sky.getFloat("kmESun"));
        setUniformf("kr4PI", sky.getFloat("kr4PI"));
        setUniformf("km4PI", sky.getFloat("km4PI"));

        setUniformf("innerRadius", sky.getFloat("innerRadius"));
        setUniformf("domeRadius", sky.getFloat("domeRadius"));

        setUniformf("scale", sky.getFloat("scale"));
        setUniformf("scaleDepth", sky.getFloat("scaleDepth"));
        setUniformf("scaleOverScaleDepth", sky.getFloat("scaleOverScaleDepth"));

        setUniformf("exposure", sky.getFloat("exposure"));
        setUniformf("g", sky.getFloat("g"));
        setUniformf("g2", sky.getFloat("g") * sky.getFloat("g"));

        setUniform("cameraPos", sky.getVector3("cameraPos"));
        setUniformf("cameraHeight", sky.getFloat("cameraHeight"));

        setUniformi("starmap", graphics.getSamplerSlot("diffuse"));
    }

}
