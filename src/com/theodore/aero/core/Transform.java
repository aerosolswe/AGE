package com.theodore.aero.core;

import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class Transform {

    private Transform parent;
    private Matrix4 parentMatrix;

    private Vector3 position;
    private Quaternion rotation;
    private Vector3 scale;

    private Vector3 oldPosition;
    private Quaternion oldRotation;
    private Vector3 oldScale;

    public Transform() {
        position = new Vector3(0, 0, 0);
        rotation = new Quaternion(0, 0, 0, 1);
        scale = new Vector3(1, 1, 1);

        parentMatrix = new Matrix4().initIdentity();
    }

    public Transform(Vector3 position, Quaternion rotation, Vector3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void update() {
        if (oldPosition != null) {
            oldPosition.set(position);
            oldRotation.set(rotation);
            oldScale.set(scale);
        } else {
            oldPosition = new Vector3(0, 0, 0).set(position).add(1.0f);
            oldRotation = new Quaternion(0, 0, 0, 0).set(rotation).mul(0.5f);
            oldScale = new Vector3(0, 0, 0).set(scale).add(1.0f);
        }
    }

    public void rotate(Vector3 axis, float angle) {
        rotation = new Quaternion(axis, angle).mul(rotation);
    }

    public void rotate(Quaternion rotationAmount) {
        rotation = rotationAmount.mul(rotation);
    }

    public void lookAt(Vector3 point, Vector3 up) {
        rotation = getLookRotation(point, up);
    }

    public Quaternion getLookRotation(Vector3 point, Vector3 up) {
        return new Quaternion(new Matrix4().initRotation(point.sub(position).normalized(), up));
    }

    public boolean hasChanged() {
        if (parent != null && parent.hasChanged())
            return true;

        if (!position.equals(oldPosition))
            return true;

        if (!rotation.equals(oldRotation))
            return true;

        if (!scale.equals(oldScale))
            return true;

        return false;
    }

    public Matrix4 getTransformation() {
        Matrix4 translationMatrix = new Matrix4().initTranslation(position.getX(), position.getY(), position.getZ());
        Matrix4 rotationMatrix = rotation.toRotationMatrix();
        Matrix4 scaleMatrix = new Matrix4().initScale(scale.getX(), scale.getY(), scale.getZ());

        return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
    }

    private Matrix4 getParentMatrix() {
        if (parent != null && parent.hasChanged())
            parentMatrix = parent.getTransformation();

        return parentMatrix;
    }

    public void setParent(Transform parent) {
        this.parent = parent;
    }

    public Vector3 getTransformedPos() {
        return getParentMatrix().transform(position);
    }

    public Quaternion getTransformedRot() {
        Quaternion parentRotation = new Quaternion(0, 0, 0, 1);

        if (parent != null)
            parentRotation = parent.getTransformedRot();

        return parentRotation.mul(rotation);
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 pos) {
        this.position = pos;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }
}
