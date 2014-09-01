package com.theodore.aero.core;

import com.theodore.aero.graphics.Camera;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

import java.util.Stack;

public class Transform {

    private static Stack<Matrix4> projectionStack = new Stack<Matrix4>();
    private static Stack<Camera> cameraStack = new Stack<Camera>();
    private static Matrix4 view = new Matrix4().initIdentity();
    private static Matrix4 projection = new Matrix4().initIdentity();
    private static Camera camera;

    private static float zNear;
    private static float zFar;
    private static float width;
    private static float height;
    private static float fov;
    private static boolean hasProjectionData = false;

    private Transform parent;

    private Matrix4 model;
    private Matrix4 parentMatrix;

    private Vector3 position;
    private Quaternion rotation;
    private Vector3 scale;

    private Vector3 oldPosition;
    private Quaternion oldRotation;
    private Vector3 oldScale;

    public Transform() {
        this(new Vector3(0, 0, 0));
    }

    public Transform(Vector3 position) {
        this(position, new Quaternion(0, 0, 0, 1), new Vector3(1, 1, 1));
    }

    public Transform(Vector3 position, Quaternion rotation) {
        this(position, rotation, new Vector3(1, 1, 1));
    }

    public Transform(Vector3 position, Vector3 scale) {
        this(position, new Quaternion(0, 0, 0, 1), scale);
    }

    public Transform(Vector3 position, Quaternion rotation, Vector3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;

        this.model = new Matrix4().initIdentity();
        this.parentMatrix = new Matrix4().initIdentity();
    }

    public void lookAt(Vector3 point, Vector3 up) {
        rotation = getLookAtRotation(point, up);
    }

    public void lookAt(float x, float y, float z, Vector3 up) {
        rotation = getLookAtRotation(new Vector3(x, y, z), up);
    }

    public Quaternion getLookAtRotation(Vector3 point, Vector3 up) {
        return new Quaternion(new Matrix4().initRotation(point.sub(position).normalized(), up));
    }

    public void lookAt(float x, float y, float z) {
        rotation = new Quaternion(position.sub(new Vector3(x, y, z)).normalized());
    }

    public void rotate(Quaternion rotationAmount) {
        rotation = rotationAmount.mul(rotation);
    }

    public void rotate(Vector3 eulerAngles) {
        rotate(new Quaternion(Vector3.UP, eulerAngles.getY()));
        rotate(new Quaternion(Vector3.FORWARD, eulerAngles.getZ()));
        rotate(new Quaternion(Vector3.RIGHT, eulerAngles.getX()));
    }

    public void scale(float amt) {
        scale = scale.mul(amt);
    }

    public void scale(Vector3 amt) {
        scale = scale.mul(amt);
    }

    public void move(Vector3 movement) {
        position = position.add(movement);
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

    public Vector3 getEulerAngles() {
        return rotation.getEulerAngles();
    }

    public void setParent(Transform transform) {
        parent = transform;
    }

    public boolean hasChanged() {
        if (oldPosition == null) {
            this.oldPosition = Vector3.ZERO.set(position);
            this.oldRotation = new Quaternion().set(rotation);
            this.oldScale = Vector3.ZERO.set(scale);

            return true;
        }

        if (parent != null && parent.hasChanged())
            return true;

        if (!position.equals(oldPosition)) {
            return true;
        }

        if (!rotation.equals(oldRotation)) {
            return true;
        }
        if (!scale.equals(oldScale)) {
            return true;
        }

        return false;
    }

    public Matrix4 calcModel() {
        Matrix4 translationMatrix = new Matrix4().initTranslation(position.getX(), position.getY(), position.getZ());
        Matrix4 rotationMatrix = rotation.getRotationMatrix();
        Matrix4 scaleMatrix = new Matrix4().initScale(scale.getX(), scale.getY(), scale.getZ());

        if (oldPosition != null) {
            oldPosition.set(position);
            oldRotation.set(rotation);
            oldScale.set(scale);
        }

        model = getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
        return model;
    }

    private Matrix4 getParentMatrix() {
        if (parent != null && parent.hasChanged())
            parentMatrix = parent.calcModel();

        return parentMatrix;
    }

    public Matrix4 calcMVP() {
        calcModel();
        calcView();

        return getMVP();
    }

    public Matrix4 getMVP() {
        return projection.mul(view.mul(model));
    }

    public Matrix4 calcOrthographicModel() {
        Matrix4 translationMatrix = new Matrix4().initOrthoTranslation(position.getX(), position.getY());
        Matrix4 rotationMatrix = new Matrix4().initOrthoRotation(rotation.getX());
        Matrix4 scaleMatrix = new Matrix4().initOrthoScale(scale.getX(), scale.getY());

        model = translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
        return model;
    }

    public Matrix4 calcOrthographicMVP() {
        Matrix4 transformation = calcModel();

        Matrix4 projection = new Matrix4().initOrthographic(0, Window.getWidth(), 0, Window.getHeight(), -1, 1);

        return projection.mul(transformation);
    }

    public Matrix4 getModel() {
        return model;
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

    public static void pushProjection(Matrix4 newProjection) {
        projectionStack.push(projection);
        projection = newProjection;
    }

    public static void popProjection() {
        projection = projectionStack.pop();
    }

    public static void pushCamera(Camera newCamera) {
        cameraStack.push(camera);
        setCamera(newCamera);
    }

    public static void popCamera() {
        setCamera(cameraStack.pop());
    }

    public static Matrix4 getView() {
        return view;
    }

    public static Matrix4 getProjection() {
        return projection;
    }

    public void setModel(Matrix4 model) {
        this.model = model;
    }

    public static void setView(Matrix4 view) {
        Transform.view = view;
    }

    public static void setView(Camera camera) {
        Camera temp = Transform.camera;
        Transform.camera = camera;
        calcView();
        Transform.camera = temp;
    }

    public static void setProjection(Matrix4 projection) {
        Transform.projection = projection;
    }

    public static Matrix4 calcView() {
        Matrix4 cameraRotation = camera.getRotation().getRotationMatrix();
        Matrix4 cameraTranslation = new Matrix4().initTranslation(-camera.getPosition().getX(), -camera.getPosition().getY(), -camera.getPosition().getZ());

        view = cameraRotation.mul(cameraTranslation);
        return view;
    }

    public static Matrix4 getOrthographicMatrix() {
        //return new Matrix4().initOrthographicProjection(-width/2, width/2, height/2, -height/2, zNear, zFar);
        return new Matrix4().initOrthographic(-width / 2, width / 2, height / 2, -height / 2, -zFar, zFar);
    }

    public static Matrix4 getOrthographicMatrix(float left, float right, float top, float bottom, float zNear, float zFar) {
        //return new Matrix4().initOrthographicProjection(-width/2, width/2, height/2, -height/2, zNear, zFar);
        return new Matrix4().initOrthographic(left, right, top, bottom, zNear, zFar);
    }

    public static Matrix4 getPerspectiveMatrix() {
        return new Matrix4().initPerspective(fov, width, height, zNear, zFar);
    }

    public Vector3 getPosition() {
        return position;
    }

    public static void setProjection(float fov, float width, float height, float zNear, float zFar) {
        Transform.fov = fov;
        Transform.width = width;
        Transform.height = height;
        Transform.zNear = zNear;
        Transform.zFar = zFar;
        Transform.hasProjectionData = true;
    }

    public static void setFOV(float fov) {
        Transform.fov = fov;
    }

    public static void setSize(float width, float height) {
        Transform.width = width;
        Transform.height = height;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position = new Vector3(x, y, z);
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

    public void setScale(float x, float y, float z) {
        this.scale = new Vector3(x, y, z);
    }

    public static boolean isInitialized() {
        return hasProjectionData && camera != null;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static void setCamera(Camera camera) {
        Transform.camera = camera;
        calcView();
    }

    public static float getZNear() {
        return zNear;
    }

    public static float getZFar() {
        return zFar;
    }

}
