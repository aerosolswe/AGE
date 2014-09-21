package com.theodore.aero.graphics.g3d;

import com.theodore.aero.graphics.CameraDirection;
import com.theodore.aero.math.Vector3;

import static org.lwjgl.opengl.GL13.*;

public class PointShadowInfo {

    private CameraDirection[] cameraDirections;

    private int shadowSize;
    private CubeMap shadowMap;

    public PointShadowInfo(int shadowSize){
        this.shadowSize = shadowSize;
        this.shadowMap = new CubeMap(shadowSize, shadowSize);

        cameraDirections = new CameraDirection[]{
                new CameraDirection(GL_TEXTURE_CUBE_MAP_POSITIVE_X, new Vector3(1.0f, 0.0f, 0.0f), new Vector3(0.0f, -1.0f, 0.0f)),
                new CameraDirection(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, new Vector3(-1.0f, 0.0f, 0.0f), new Vector3(0.0f, -1.0f, 0.0f)),
                new CameraDirection(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, new Vector3(0.0f, 1.0f, 0.0f), new Vector3(0.0f, 0.0f, -1.0f)),
                new CameraDirection(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, new Vector3(0.0f, -1.0f, 0.0f), new Vector3(0.0f, 0.0f, 1.0f)),
                new CameraDirection(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, -1.0f, 0.0f)),
                new CameraDirection(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, new Vector3(0.0f, 0.0f, -1.0f), new Vector3(0.0f, -1.0f, 0.0f))
        };
    }

    public CameraDirection getCameraDirection(int i){
        return cameraDirections[i];
    }

    public int getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    public CubeMap getShadowMap() {
        return shadowMap;
    }

    public void setShadowMap(CubeMap shadowMap) {
        this.shadowMap = shadowMap;
    }
}
