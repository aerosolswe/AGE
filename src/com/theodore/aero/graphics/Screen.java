package com.theodore.aero.graphics;

import com.theodore.aero.core.GameObject;

public abstract class Screen {

    private GameObject root;

    public void init() {
    }

    public void input(float delta) {
        getRootGameObject().inputAll(delta);
    }

    public void update(float delta) {
        getRootGameObject().updateAll(delta);
    }

    public void render(Graphics graphics) {
        graphics.fullRender(getRootGameObject());
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

}
