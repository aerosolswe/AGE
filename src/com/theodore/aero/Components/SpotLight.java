package com.theodore.aero.components;

import com.theodore.aero.graphics.g3d.Attenuation;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.graphics.shaders.forward.ForwardSpotShader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class SpotLight extends PointLight {

    private float cutoff;

    public SpotLight(Vector3 color, float intensity, Attenuation attenuation, int shadowMapPowerOf2, float viewAngle, float shadowSoftness, float lightBleedReductionAmount) {
        this(color, intensity, attenuation, shadowMapPowerOf2, viewAngle, shadowSoftness, lightBleedReductionAmount, 0.0000002f);
    }

    public SpotLight(Vector3 color, float intensity, Attenuation attenuation, int shadowMapPowerOf2, float viewAngle, float shadowSoftness) {
        this(color, intensity, attenuation, shadowMapPowerOf2, viewAngle, shadowSoftness, 0.3f, 0.0000002f);
    }

    public SpotLight(Vector3 color, float intensity, Attenuation attenuation, int shadowMapPowerOf2, float viewAngle) {
        this(color, intensity, attenuation, shadowMapPowerOf2, viewAngle, 0.1f, 0.3f, 0.0000002f);
    }

    public SpotLight(Vector3 color, float intensity, Attenuation attenuation) {
        this(color, intensity, attenuation, 0, (float) Math.toRadians(100), 1f, 0.3f, 0.0000002f);
    }

    public SpotLight(Vector3 color,
                     float intensity,
                     Attenuation attenuation,
                     int shadowMapPowerOf2,
                     float viewAngle,
                     float shadowSoftness,
                     float lightBleedReductionAmount,
                     float minVariance
    ) {
        super(color, intensity, attenuation);
        this.cutoff = (float) Math.cos(viewAngle / 2);

        setShader(ForwardSpotShader.getInstance());
        if (shadowMapPowerOf2 != 0) {
            setShadowInfo(
                    new ShadowInfo(
                            new Matrix4().initPerspective(viewAngle, 1, 0.1f, getRange()),
                            false,
                            shadowMapPowerOf2,
                            shadowSoftness,
                            lightBleedReductionAmount,
                            minVariance
                    )
            );
        }
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
