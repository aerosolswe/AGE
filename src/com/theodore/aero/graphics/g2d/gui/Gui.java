package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.math.Matrix4;
import org.lwjgl.opengl.GL11;


public class Gui {

    private Node parent;

    private Camera camera;

    private int width;
    private int height;

    public Gui(int width, int height) {
        this.width = width;
        this.height = height;

        camera = new Camera(new Matrix4().initOrthographic(0, width, 0, height, -1, 1));
        new GameObject().addComponent(camera);

        parent = new Node(0, 0, width, height);
    }

    public void update(float delta) {
        parent.update(delta);
    }

    public void draw(Graphics graphics) {
        camera.setProjection(new Matrix4().initOrthographic(0, width, 0, height, -1f, 1));

        Camera mainCamera = Aero.graphics.getMainCamera();
        Aero.graphics.setMainCamera(camera);

        Aero.graphicsUtil.enableBlending(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Aero.graphicsUtil.disableCullFace();
        Aero.graphicsUtil.setDepthTest(false);

        parent.draw(graphics);

        Aero.graphicsUtil.setDepthTest(true);
        Aero.graphicsUtil.enableCullFace(GL11.GL_BACK);

        Aero.graphics.setMainCamera(mainCamera);
    }

    public void addWidget(Widget widget) {
        this.parent.addChild(widget);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        camera.setProjection(new Matrix4().initOrthographic(0, width, 0, height, -1f, 1));
//        parent = new Node(0, 0, width, height);
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
