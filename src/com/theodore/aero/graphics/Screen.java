package com.theodore.aero.graphics;

import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.g3d.SkyBox;

public abstract class Screen {

    private GameObject root;
    private SkyBox skyBox;

    public void init() {
    }

    public void input(float delta) {
        getRootGameObject().inputAll(delta);
    }

    public void update(float delta) {
        getRootGameObject().updateAll(delta);
    }

    public void render(Graphics graphics) {
        graphics.fullRender(getRootGameObject(), skyBox);
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
}
