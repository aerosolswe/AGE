package com.theodore.aero.graphics;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.ProfileTimer;
import com.theodore.aero.graphics.g3d.SkyBox;
import com.theodore.aero.graphics.g3d.SkyGradient;

public abstract class Screen {

    private GameObject root;
    private SkyBox skyBox;
    private SkyGradient skyGradient;

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
        graphics.fullRender(getRootGameObject(), skyGradient);
    }

    public void resized(int width, int height) {
    }

    public void addObject(GameObject object) {
        getRootGameObject().addChild(object);
    }

    private GameObject getRootGameObject() {
        if (root == null)
            root = new GameObject();

        return root;
    }

    public void setEngine() {
        getRootGameObject().setEngine();
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SkyGradient getSkyGradient() {
        return skyGradient;
    }

    public void setSkyGradient(SkyGradient skyGradient) {
        this.skyGradient = skyGradient;
    }
}
