package com.theodore.aero.components;

import com.theodore.aero.graphics.g3d.ShadowCameraTransform;
import com.theodore.aero.graphics.g3d.ShadowInfo;
import com.theodore.aero.graphics.shaders.ForwardDirectionalShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class DirectionalLight extends BaseLight {

    private float halfShadowArea;

    public DirectionalLight(Vector3 color, float intensity, int shadowMapPowerOf2, float shadowArea, float shadowSoftness, float lightBleedReductionAmount) {
        this(color, intensity, shadowMapPowerOf2, shadowArea, shadowSoftness, lightBleedReductionAmount, 0.0000002f);
    }

    public DirectionalLight(Vector3 color, float intensity, int shadowMapPowerOf2, float shadowArea, float shadowSoftness) {
        this(color, intensity, shadowMapPowerOf2, shadowArea, shadowSoftness, 0.1f, 0.0000002f);
    }

    public DirectionalLight(Vector3 color, float intensity, int shadowMapPowerOf2, float shadowArea) {
        this(color, intensity, shadowMapPowerOf2, shadowArea, 1, 0.1f, 0.0000002f);
    }

    public DirectionalLight(Vector3 color, float intensity) {
        this(color, intensity, 0, 1f, 0.1f, 0.0000002f);
    }

    public DirectionalLight(Vector3 color,
                            float intensity,
                            int shadowMapPowerOf2,
                            float shadowArea,
                            float shadowSoftness,
                            float lightBleedReductionAmount,
                            float minVariance
    ) {
        super(color, intensity);

        this.halfShadowArea = shadowArea / 2.0f;

        setShader(new ForwardDirectionalShader());
        if (shadowMapPowerOf2 != 0) {
            setShadowInfo(
                    new ShadowInfo(
                            new Matrix4().initOrthographic(-halfShadowArea, halfShadowArea, -halfShadowArea, halfShadowArea, -halfShadowArea, halfShadowArea),
                            true,
                            shadowMapPowerOf2,
                            shadowSoftness,
                            lightBleedReductionAmount,
                            minVariance
                    )
            );
        }
    }

    @Override
    public ShadowCameraTransform calcShadowCameraTransform(Vector3 mainCameraPos, Quaternion mainCameraRot) {
        ShadowCameraTransform result = new ShadowCameraTransform();
        result.position.x = mainCameraPos.x + mainCameraRot.getForward().x * halfShadowArea;
        result.position.y = mainCameraPos.y + mainCameraRot.getForward().y * halfShadowArea;
        result.position.z = mainCameraPos.z + mainCameraRot.getForward().z * halfShadowArea;

        result.rotation = getTransform().getTransformedRot();

        float worldTexelSize = (halfShadowArea * 2) / ((float) (getShadowInfo().getShadowMapSize()));

        Vector3 lightSpaceCameraPos = result.position.rotate(result.rotation.conjugate());

        lightSpaceCameraPos.setX(worldTexelSize * (float) Math.floor(lightSpaceCameraPos.getX() / worldTexelSize));
        lightSpaceCameraPos.setY(worldTexelSize * (float) Math.floor(lightSpaceCameraPos.getY() / worldTexelSize));

        result.position = lightSpaceCameraPos.rotate(result.rotation);

        return result;
    }

    public Quaternion getDirection() {
        return getTransform().getTransformedRot();
    }

}
