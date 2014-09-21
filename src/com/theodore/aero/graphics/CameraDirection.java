package com.theodore.aero.graphics;

import com.theodore.aero.math.Vector3;

public class CameraDirection {

    public int cubeMapFace = 0;
    public Vector3 target;
    public Vector3 up;

    public CameraDirection(int cubeMapFace, Vector3 target, Vector3 up) {
        this.cubeMapFace = cubeMapFace;
        this.target = target;
        this.up = up;
    }

    public int getCubeMapFace() {
        return cubeMapFace;
    }

    public void setCubeMapFace(int cubeMapFace) {
        this.cubeMapFace = cubeMapFace;
    }

    public Vector3 getTarget() {
        return target;
    }

    public void setTarget(Vector3 target) {
        this.target = target;
    }

    public Vector3 getUp() {
        return up;
    }

    public void setUp(Vector3 up) {
        this.up = up;
    }
}
