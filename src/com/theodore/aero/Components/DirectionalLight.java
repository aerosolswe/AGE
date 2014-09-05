package com.theodore.aero.Components;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.g3d.ShadowMap;
import com.theodore.aero.graphics.shaders.forward.ForwardDirectionalShader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class DirectionalLight extends BaseLight {

    float[] lightViewSize = new float[]{5, 15, 60, 100};

    public DirectionalLight(Vector3 color, float intensity) {
        super(color, intensity);

        setShader(ForwardDirectionalShader.getInstance());

        Matrix4[] projMatrix = new Matrix4[4];

        for (int i = 0; i < 4; i++) {
            projMatrix[i] = new Matrix4().initOrthographic(-lightViewSize[i], lightViewSize[i],
                    -lightViewSize[i], lightViewSize[i],
                    -lightViewSize[i], lightViewSize[i]);
        }

        this.setShadowMap(new ShadowMap(projMatrix, lightViewSize));
    }

    @Override
    public void render(GameObject object) {
        super.render(object);
        getShadowMap().setDirection(getDirection());
    }

    public Quaternion getDirection() {
        return getTransform().getTransformedRot();
    }

}
