package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Input;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.input.Mapping;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public abstract class Widget {

    protected ArrayList<Widget> children;
    protected Widget parent;

    protected Transform transform;
    protected Material material;
    protected Shader orthoShader;
    protected Mesh mesh;

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected boolean mousePressed = false;
    protected boolean mouseReleased = false;
    protected boolean mouseClicked = false;
    protected boolean mouseHovered = false;

    protected boolean visible = true;

    private Vector3 pos = new Vector3();

    public Widget(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        transform = new Transform();
        material = new Material();

        children = new ArrayList<Widget>();

        orthoShader = new BasicShader();
        mesh = new Mesh("plane");
        transform.rotate(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(90)));

        Aero.inputManager.addMouseMap("widgetLeftButton", Input.Buttons.LEFT);
    }

    public void update(float delta) {
        transform.setScale(new Vector3(width / 2, height / 2, height / 2));

        Mapping leftButton = Aero.inputManager.getMapping("widgetLeftButton");
        if (mouseHovered) {
            mousePressed = leftButton.isPressed();
        }

        mouseReleased = leftButton.isReleased();

        if (parent != null) {
            pos.set(parent.getX() + ((this.x + width) - (width / 2)), parent.getY() + ((this.y + height) - (height / 2)), 0);
            transform.setPosition(pos);

            float mouseX = Aero.input.getCursorPosition().x;
            float mouseY = Aero.input.getCursorPosition().y;
            mouseY = Aero.window.getHeight() - mouseY;

            mouseHovered = isInside(mouseX, mouseY);
        }

        for (Widget child : children) child.update(delta);
    }

    public void draw(Graphics graphics) {
        if (visible) {
            if (material != null) {
                orthoShader.bind();
                orthoShader.updateUniforms(transform, material, graphics);
                mesh.draw(GL11.GL_TRIANGLES);
            }

            for (Widget child : children) child.draw(graphics);
        }
    }

    public Widget center() {
        setX(parent.getCenterX() - getCenterX());
        setY(parent.getCenterY() - getCenterY());

        return this;
    }

    public float getCenterX() {
        return (width / 2);
    }

    public float getCenterY() {
        return (height / 2);
    }

    public boolean isInside(Vector2 position) {
        return isInside(position.getX(), position.getY());
    }

    public boolean isInside(float x, float y) {
        float dx = this.x;
        float dy = this.y;

        if (parent != null) {
            dx = parent.getX() + this.x;
            dy = parent.getY() + this.y;
        }

        return x > dx && x < dx + this.width &&
                y > dy && y < dy + this.height;
    }

    public boolean mousePressed() {
        return mousePressed;
    }

    public boolean mouseReleased() {
        return mouseReleased;
    }

    public boolean mouseClicked() {
        return mouseClicked;
    }

    public boolean mouseHovered() {
        return mouseClicked;
    }

    public void setParent(Widget parent) {
        this.parent = parent;
    }

    public Widget getParent() {
        return parent;
    }

    public ArrayList<Widget> getChildren() {
        return children;
    }

    public void addChild(Widget widget) {
        if (widget.getParent() == null) {
            widget.setParent(this);
        }
        children.add(widget);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
