package com.theodore.aero.graphics.g3d.lighting;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.g3d.ShadowMap;
import com.theodore.aero.graphics.shaders.forward.ForwardDirectionalShader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public class DirectionalLight extends BaseLight {

    float[] lightViewSize = new float[]{5, 15, 60, 100};

    private Quaternion direction;

    public DirectionalLight(Vector3 color, float intensity, Quaternion direction) {
        super(color, intensity, ForwardDirectionalShader.getInstance());
        this.direction = direction;


        Matrix4[] projMatrix = new Matrix4[4];

        for(int i = 0; i < 4; i++){
            projMatrix[i] = new Matrix4().initOrthographic(-lightViewSize[i], lightViewSize[i],
                    -lightViewSize[i], lightViewSize[i],
                    -lightViewSize[i], lightViewSize[i]);
        }

        this.setShadowMap(new ShadowMap(projMatrix, lightViewSize));
    }

    @Override
    public void render(ArrayList<GameObject> objects) {
        super.render(objects);
        getShadowMap().setDirection(direction);
    }

    public Quaternion getDirection() {
        return direction;
    }

    public void setDirection(Quaternion direction) {
        this.direction = direction;
    }

}
