package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Mesh;
import com.theodore.aero.graphics.Shader;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public abstract class Widget {

    protected Shader shader;
    protected Transform transform;
    protected Mesh mesh;

    protected Material material;

    protected float x;
    protected float y;

    protected float width;
    protected float height;

    protected ArrayList<Widget> children = new ArrayList<Widget>();

    protected boolean visible = true;

    public abstract void update(float delta);

    public void draw() {
        shader.bind();
        shader.updateUniforms(transform.calcOrthographicModel(), transform.calcOrthographicMVP(), material);
        if (visible)
            mesh.draw();
    }

    public boolean checkIntersection(Vector2 position) {
        if (position.getX() > x &&
                position.getX() < x + width &&
                position.getY() > y &&
                position.getY() < y + height) {
            return true;
        }
        return false;
    }

    public void setPosition(Vector2 position) {
        transform.setPosition(new Vector3(position.getX(), position.getY() + height / 2, 0));
    }

    public Vector2 getPosition() {
        return transform.getPosition().getXY();
    }

    public void setSize(Vector2 size) {
        transform.setScale(size.getX(), size.getY(), 0);
    }

    public Vector2 getSize() {
        return transform.getScale().getXY();
    }

    public void setTexture(Texture texture) {
        material.setDiffuseTexture(texture);
    }

    public Transform getTransform() {
        return transform;
    }

    public ArrayList<Widget> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Widget> children) {
        this.children = children;
    }

    public void addChild(Widget widget) {
        children.add(widget);
    }

    public void removeChild(Widget widget) {
        children.remove(widget);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
