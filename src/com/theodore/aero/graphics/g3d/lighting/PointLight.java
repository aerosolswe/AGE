package com.theodore.aero.graphics.g3d.lighting;

import com.theodore.aero.graphics.Attenuation;
import com.theodore.aero.graphics.shaders.forward.ForwardPointShader;
import com.theodore.aero.math.Vector3;

public class PointLight extends BaseLight {

    private static final int COLOR_DEPTH = 256;

    private Attenuation attenuation;
    private float range;

    public PointLight(Vector3 position, Vector3 color, float intensity, Attenuation attenuation) {
        super(color, intensity, ForwardPointShader.getInstance());
        this.getTransform().setPosition(position);
        this.attenuation = attenuation;

        float a = attenuation.getExponent();
        float b = attenuation.getLinear();
        float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();

        this.range = (float) ((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));
    }

    public PointLight(Vector3 position, Vector3 color, float intensity) {
        this(position, color, intensity, new Attenuation(0, 0, 0.1f));
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }
}
