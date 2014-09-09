package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.components.Camera;
import com.theodore.aero.components.GameComponent;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Gui {

    private ArrayList<Widget> widgets;

    private Camera camera;

    private int width;
    private int height;

    public Gui(int width, int height) {
        this.width = width;
        this.height = height;

        camera = new Camera(new Matrix4().initOrthographic(0, width, 0, height, -1, 1));
        GameObject cgo = new GameObject().addComponent(camera);

        widgets = new ArrayList<Widget>();
    }

    public void update(float delta) {
        for (Widget widget : widgets) widget.update(delta);
    }

    public void draw(Graphics graphics) {
        camera.setProjection(new Matrix4().initOrthographic(0, width, 0, height, -1f, 1));

        Camera mainCamera = Aero.graphics.getMainCamera();
        Aero.graphics.setMainCamera(camera);

        Aero.graphicsUtil.enableBlending(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Aero.graphicsUtil.disableCullFace();
        Aero.graphicsUtil.setDepthTest(false);

        for (Widget widget : widgets) widget.draw(graphics);

        Aero.graphicsUtil.setDepthTest(true);
        Aero.graphicsUtil.enableCullFace(GL11.GL_BACK);

        Aero.graphics.setMainCamera(mainCamera);
    }

    public void addWidget(Widget widget) {
        this.widgets.add(widget);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        camera.setProjection(new Matrix4().initOrthographic(0, width, 0, height, 0.1f, 2));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
