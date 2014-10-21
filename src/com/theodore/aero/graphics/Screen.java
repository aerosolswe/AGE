package com.theodore.aero.graphics;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.ProfileTimer;

public abstract class Screen {

    private GameObject root;

    private ProfileTimer inputTimer;
    private ProfileTimer updateTimer;

    public void init() {
        inputTimer = new ProfileTimer();
        updateTimer = new ProfileTimer();
    }

    public double getInputTimeAndReset(double dividend) {
        return inputTimer.getTimeAndReset(dividend);
    }

    public double getUpdateTimeAndReset(double dividend) {
        return updateTimer.getTimeAndReset(dividend);
    }

    public void input(float delta) {
        inputTimer.startInvocation();
        getRootGameObject().inputAll(delta);
        inputTimer.stopInvocation();
    }

    public void update(float delta) {
        updateTimer.startInvocation();
        getRootGameObject().updateAll(delta);
        updateTimer.stopInvocation();
    }

    public void render(Graphics graphics) {
        graphics.fullRender(getRootGameObject());
    }

    public void resized(int width, int height) {
    }

    public void addObject(GameObject object) {
        getRootGameObject().addChild(object);
    }

    public void removeObject(GameObject object) {
        getRootGameObject().removeChild(object);
    }

    private GameObject getRootGameObject() {
        if (root == null)
            root = new GameObject();

        return root;
    }

    public void setEngine() {
        getRootGameObject().setEngine();
    }
}
