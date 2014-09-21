package com.theodore.aero.core;

import com.theodore.aero.components.GameComponent;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.shaders.Shader;

import java.util.ArrayList;

public class GameObject {

    private ArrayList<GameObject> children;
    private ArrayList<GameComponent> components;
    private Transform transform;

    public GameObject() {
        children = new ArrayList<GameObject>();
        components = new ArrayList<GameComponent>();
        transform = new Transform();
    }

    public void addChild(GameObject child) {
        children.add(child);
        child.setEngine();
        child.getTransform().setParent(transform);
    }

    public GameObject addComponent(GameComponent component) {
        components.add(component);
        component.setParent(this);

        return this;
    }

    public void inputAll(float delta) {
        input(delta);

        for (GameObject child : children)
            child.inputAll(delta);
    }

    public void updateAll(float delta) {
        update(delta);

        for (GameObject child : children)
            child.updateAll(delta);
    }

    public void renderAll(Shader shader, Graphics graphics) {
        render(shader, graphics);

        for (GameObject child : children)
            child.renderAll(shader, graphics);
    }

    public void input(float delta) {
        transform.update();

        for (GameComponent component : components)
            component.input(delta);
    }

    public void update(float delta) {
        for (GameComponent component : components)
            component.update(delta);
    }

    public void render(Shader shader, Graphics graphics) {
        for (GameComponent component : components)
            component.render(shader, graphics);
    }

    public ArrayList<GameObject> getAllAttached() {
        ArrayList<GameObject> result = new ArrayList<GameObject>();

        for (GameObject child : children)
            result.addAll(child.getAllAttached());

        result.add(this);
        return result;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setEngine() {
        for (GameComponent component : components)
            component.addToEngine();

        for (GameObject child : children)
            child.setEngine();
    }

}
