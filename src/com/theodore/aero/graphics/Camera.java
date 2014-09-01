package com.theodore.aero.graphics;

import com.theodore.aero.core.Transform;
import com.theodore.aero.math.MathUtils;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class Camera {

    public static final Vector3 yAxis = new Vector3(0, 1, 0);

    protected Vector3 position;
    protected Quaternion rotation;
    public float upAngle = 0;

    public Camera() {
        this(new Vector3(0, 0, 0));
    }

    public Camera(Vector3 pos) {
        this(pos, new Quaternion(0, 0, 0, 1));
    }

    public Camera(Vector3 position, Quaternion rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    /**
     * WIP
     */
//    public void lookAt(Vector3 pos){
//        rotation = new Quaternion(new Matrix4().initRotation(pos.sub(position), yAxis));
//    }
    public void lookAt(Vector3 pos) {
        Vector3 direction = pos.sub(position).normalized();
        float verticalAngle = MathUtils.toDegrees(MathUtils.asin(direction.y));
        float horizontalAngle = -MathUtils.toDegrees(MathUtils.atan2(direction.x, direction.z));

        verticalAngle = normalizeVAngle(verticalAngle);
        horizontalAngle = normalizeHAngle(horizontalAngle);

        Quaternion q0 = new Quaternion(new Vector3(1, 0, 0), verticalAngle);
        Quaternion q1 = new Quaternion(new Vector3(0, 1, 0), horizontalAngle);

        rotation = q0.mul(q1);
    }

    private float normalizeHAngle(float angle) {
        if (angle < 0.0f)
            angle += 360.0f;

        return angle;
    }

    private float normalizeVAngle(float angle) {
        if (angle > 85)
            angle = 85;
        else if (angle < -85)
            angle = -85;

        return angle;
    }

    public Matrix4 getViewProjection() {
        Matrix4 cameraRotation = rotation.conjugate().getRotationMatrix();
        Vector3 cameraPos = position.mul(-1);

        Matrix4 cameraTranslation = new Matrix4().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());

        return Transform.getProjection().mul(cameraRotation.mul(cameraTranslation));
    }

    public void rotate(Quaternion quaternion) {
        rotation = rotation.mul(quaternion).normalized();
    }

    public Vector3 getForward() {
        return rotation.getForward();
    }

    public Vector3 getRight() {
        return rotation.getRight();
    }

    public Vector3 getUp() {
        return rotation.getUp();
    }

    public void rotateY(float angle) {
        Quaternion newRotation = new Quaternion(yAxis, -angle).normalized();

        rotation = rotation.mul(newRotation).normalized();
    }

    public void rotateX(float angle) {
        Quaternion newRotation = new Quaternion(rotation.getRight(), -angle).normalized();

        rotation = rotation.mul(newRotation).normalized();
    }

    public Vector3 getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public float getUpAngle() {
        return upAngle;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

}
