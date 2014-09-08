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

    /*private ArrayList<GameObject> children;

    private Transform transform;
    private Mesh mesh;
    private Material material;

    private String name;

    private Vector3 velocity = new Vector3(0, 0, 0);

    private boolean isVisible = true;
    private boolean castShadows = true;

    public GameObject(Mesh mesh, Material material) {
        this(mesh, material, new Vector3(0, 0, 0), new Vector3(1, 1, 1));
    }

    public GameObject(Mesh mesh, Material material, Vector3 position, Vector3 scale) {
        this.transform = new Transform();
        this.mesh = mesh;
        this.material = material;
        this.transform.setPosition(position);
        this.transform.setScale(scale);

        this.children = new ArrayList<GameObject>();
    }

    public void render() {
        if (mesh != null && isVisible) {
            mesh.draw();
        }
    }


    public void addChild(GameObject child) {
        children.add(child);
        child.getTransform().setParent(transform);
    }

    public void removeChild(GameObject child) {
        children.remove(child);

    }

    public ArrayList<GameObject> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<GameObject> children) {
        this.children = children;
    }

    public ArrayList getAllAttached() {
        ArrayList<GameObject> result = new ArrayList<GameObject>();

        for (GameObject child : children) {
            result.addAll(child.getAllAttached());
        }

        result.add(this);

        return result;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Material getMaterial() {
        return material;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public boolean canCastShadows() {
        return castShadows;
    }

    public void castShadows(boolean castShadows) {
        this.castShadows = castShadows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }*/
}
