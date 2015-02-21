package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;

public class Camera extends GameComponent {

    private Matrix4 projection;

    public Camera(Matrix4 projection) {
        this.projection = projection;
    }

    public Matrix4 getView() {
        Matrix4 cameraRotation = getTransform().getTransformedRot().conjugate().toRotationMatrix();
        Vector3 cameraPos = getTransform().getTransformedPos().mul(-1);

        return projection.mul(cameraRotation);
    }

    public Matrix4 getViewProjection() {
        Matrix4 cameraRotation = getTransform().getTransformedRot().conjugate().toRotationMatrix();
        Vector3 cameraPos = getTransform().getTransformedPos().mul(-1);

        Matrix4 cameraTranslation = new Matrix4().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());

        return projection.mul(cameraRotation.mul(cameraTranslation));
    }

    public void setProjection(Matrix4 projection) {
        this.projection = projection;
    }

    public Matrix4 getProjection() {
        return projection;
    }

    @Override
    public void addToEngine() {
        Aero.graphics.setMainCamera(this);
    }

    public Vector3 unproject(Vector3 screenCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        float x = screenCoords.x, y = screenCoords.y;
        x = x - viewportX;
        y = Aero.window.getHeight() - y - 1;
        y = y - viewportY;
        screenCoords.x = (2 * x) / viewportWidth - 1;
        screenCoords.y = (2 * y) / viewportHeight - 1;
        screenCoords.z = 2 * screenCoords.z - 1;
        Matrix4 inv = getViewProjection();
        inv = inv.inv();
        screenCoords.prj(inv);
        return screenCoords;
    }

    public Vector3 unproject(Vector3 screenCoords) {
        unproject(screenCoords, 0, 0, Aero.window.getWidth(), Aero.window.getHeight());
        return screenCoords;
    }

    public Vector3 project(Vector3 worldCoords) {
        project(worldCoords, 0, 0, Aero.window.getWidth(), Aero.window.getHeight());
        return worldCoords;
    }

    public Vector3 project(Vector3 worldCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        worldCoords.prj(getViewProjection());
        worldCoords.x = viewportWidth * (worldCoords.x + 1) / 2 + viewportX;
        worldCoords.y = viewportHeight * (worldCoords.y + 1) / 2 + viewportY;
        worldCoords.z = (worldCoords.z + 1) / 2;
        return worldCoords;
    }

}
