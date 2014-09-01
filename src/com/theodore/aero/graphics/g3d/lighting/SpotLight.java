package com.theodore.aero.graphics.g3d.lighting;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Attenuation;
import com.theodore.aero.graphics.g3d.ShadowMap;
import com.theodore.aero.graphics.shaders.forward.ForwardSpotShader;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public class SpotLight extends PointLight {

    float[] lightViewSize = new float[]{5, 15, 60, 100};

    private Quaternion direction;
    private float cutoff;

    public SpotLight(Vector3 position, Vector3 color, float intensity, Attenuation attenuation, Quaternion direction, float cutoff) {
        super(position, color, intensity, attenuation);
        getTransform().setPosition(position);
        this.direction = direction;
        this.cutoff = cutoff;
        this.setShader(ForwardSpotShader.getInstance());
    }

    public SpotLight(Vector3 position, Vector3 color, float intensity, Quaternion direction, float cutoff) {
        super(position, color, intensity);
        getTransform().setPosition(position);
        this.direction = direction;
        this.cutoff = cutoff;
        this.setShader(ForwardSpotShader.getInstance());


        Matrix4[] projMatrix = new Matrix4[4];

        for(int i = 0; i < 4; i++){
            projMatrix[i] = new Matrix4().initPerspective(cutoff * 2, 1, 1, 0.1f, getRange());
        }

        this.setShadowMap(new ShadowMap(projMatrix, lightViewSize));
    }

    public SpotLight(Vector3 position, Vector3 color, float intensity, Quaternion direction) {
        this(position, color, intensity, direction, 0.3f);
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

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }

}
