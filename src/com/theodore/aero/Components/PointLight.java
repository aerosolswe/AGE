package com.theodore.aero.components;

import com.theodore.aero.graphics.g3d.Attenuation;
import com.theodore.aero.graphics.shaders.ForwardPointShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Vector3;

public class PointLight extends BaseLight {

    private static final int COLOR_DEPTH = 256;

    private Attenuation attenuation;
    private float range;

    public PointLight(Vector3 color, float intensity, Attenuation attenuation, int shadowSize) {
        super(color, intensity);
        this.attenuation = attenuation;

        float a = attenuation.getExponent();
        float b = attenuation.getLinear();
        float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();

        this.range = (float) ((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));

        setShader(new ForwardPointShader());
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

}
