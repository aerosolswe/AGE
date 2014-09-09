package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.graphics.shaders.ortho.OrthographicShader;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public abstract class Widget {

    private ArrayList<Widget> children;

    protected Transform transform;
    protected Material material;
    protected Shader orthoShader;
    protected Mesh mesh;

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    private boolean mousePressed = false;
    private boolean mouseReleased = false;
    private boolean mouseClicked = false;

    public Widget() {
        transform = new Transform();
        material = new Material();

        children = new ArrayList<Widget>();

        orthoShader = OrthographicShader.getInstance();
        mesh = Mesh.get("plane");
        transform.rotate(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(90)));
//        transform.rotate(new Quaternion(new Vector3(0, 0, 1), (float) Math.toRadians(180.0f)));
        transform.rotate(new Quaternion(new Vector3(0, 1, 0), (float) Math.toRadians(180.0f)));
    }

    public void update(float delta) {
        for (int i = 0; i < children.size(); i++) children.get(i).update(delta);

        transform.setScale(new Vector3(width / 2, height / 2, height / 2));
        transform.setPosition(new Vector3((x + width) - (width / 2), (y + height) - (height / 2), 0));

        mousePressed = Aero.input.getMouseDown(0) && isInside(Aero.input.getMousePosition());
        mouseReleased = Aero.input.getMouseUp(0) && isInside(Aero.input.getMousePosition());
        mouseClicked = Aero.input.getMouse(0) && isInside(Aero.input.getMousePosition());
    }

    public void draw(Graphics graphics) {
        orthoShader.bind();
        orthoShader.updateUniforms(transform, material, graphics);
        mesh.draw();


        for (int i = 0; i < children.size(); i++) children.get(i).draw(graphics);
    }

    public boolean isInside(Vector2 position) {
        return isInside(position.getX(), position.getY());
    }

    public boolean isInside(float x, float y) {
        return x > this.x && x < this.x + this.width &&
                y > this.y && y < this.y + this.height;
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

    public void addChild(Widget widget) {
        children.add(widget);
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
