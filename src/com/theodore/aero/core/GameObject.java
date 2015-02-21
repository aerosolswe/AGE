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

    public void removeChild(GameObject child) {
        children.remove(child);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public GameObject addComponent(GameComponent component) {
        components.add(component);
        component.setParent(this);

        return this;
    }

    public GameObject removeComponent(GameComponent component) {
        components.remove(component);

        return this;
    }

    public GameObject removeComponent(int index) {
        components.remove(index);

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

    public void renderAllBasic(Shader shader, Graphics graphics) {
        renderBasic(shader, graphics);

        for (GameObject child : children)
            child.renderAllBasic(shader, graphics);
    }

    public void renderAllShadow(Shader shader, Graphics graphics) {
        renderShadow(shader, graphics);

        for (GameObject child : children)
            child.renderAllShadow(shader, graphics);
    }

    public void renderAllLight(Shader shader, Graphics graphics) {
        renderLight(shader, graphics);

        for (GameObject child : children)
            child.renderAllLight(shader, graphics);
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

    public void renderBasic(Shader shader, Graphics graphics) {
        for (GameComponent component : components)
            component.renderBasic(shader, graphics);
    }

    public void renderShadow(Shader shader, Graphics graphics) {
        for (GameComponent component : components)
            component.renderShadow(shader, graphics);
    }

    public void renderLight(Shader shader, Graphics graphics) {
        for (GameComponent component : components)
            component.renderLight(shader, graphics);
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
