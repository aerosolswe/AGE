package com.theodore.aero.graphics.g3d;

import com.theodore.aero.math.Vector3;

public class Attenuation extends Vector3 {

    public Attenuation() {
        this(0, 0, 2);
    }

    public Attenuation(float constant, float linear, float exponent) {
        super(constant, linear, exponent);
    }

    public void setConstant(float constant) {
        setX(constant);
    }

    public void setLinear(float linear) {
        setY(linear);
    }

    public void setExponent(float exponent) {
        setZ(exponent);
    }

    public float getConstant() {
        return getX();
    }

    public float getLinear() {
        return getY();
    }

    public float getExponent() {
        return getZ();
    }
}
