package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.math.Frustum;
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

    public Frustum getFrustum() {
        return new Frustum(getView().mul(getProjection()));
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

}
