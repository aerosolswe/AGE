package com.theodore.aero.Components;

import com.theodore.aero.graphics.g3d.Attenuation;
import com.theodore.aero.graphics.shaders.forward.ForwardSpotShader;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class SpotLight extends PointLight {

    private float cutoff;

    public SpotLight(Vector3 color, float intensity, Attenuation attenuation, float cutoff) {
        super(color, intensity, attenuation);
        this.cutoff = cutoff;

        setShader(ForwardSpotShader.getInstance());
    }

    public Quaternion GetDirection() {
        return getTransform().getTransformedRot();
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }

}
