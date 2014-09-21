package com.theodore.aero.graphics.g3d;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.sky.SkyBoxShader;
import com.theodore.aero.math.Vector3;

import static org.lwjgl.opengl.GL11.*;

public class SkyBox {

    private CubeMap skyBoxCubeMap;
    private Mesh mesh;
    private SkyBoxShader shader;
    private Transform transform;
    private Vector3 color;

    public SkyBox() {
        this("front.png", "back.png", "top.png", "bottom.png", "left.png", "right.png");
    }

    public SkyBox(String front, String back, String top, String bottom, String left, String right) {
        this.skyBoxCubeMap = new CubeMap(
                Aero.files.internal("default/cubemaps/" + front),
                Aero.files.internal("default/cubemaps/" + back),
                Aero.files.internal("default/cubemaps/" + top),
                Aero.files.internal("default/cubemaps/" + bottom),
                Aero.files.internal("default/cubemaps/" + left),
                Aero.files.internal("default/cubemaps/" + right));
        this.shader = SkyBoxShader.getInstance();

        this.mesh = Mesh.get("sphere.obj");
        this.transform = new Transform();
        this.color = new Vector3(1, 1, 1);

        transform.setScale(new Vector3(1000, 1000, 1000));
        transform.setPosition(new Vector3(0, -10, 0));
    }

    public void render(Graphics graphics) {
        int oldCullFaceMode = glGetInteger(GL_CULL_FACE_MODE);
        int oldDepthFuncMode = glGetInteger(GL_DEPTH_FUNC);

        Aero.graphicsUtil.enableCullFace(GL_FRONT);
        Aero.graphicsUtil.setDepthFunc(GL_LEQUAL);

        Camera tmp = graphics.getMainCamera();

        this.shader.bind();
        this.shader.updateUniforms(transform, graphics, color);
//        skyBoxCubeMap.bind(GL_TEXTURE0);
        mesh.draw();

        graphics.setMainCamera(tmp);

        Aero.graphicsUtil.enableCullFace(oldCullFaceMode);
//        Aero.graphicsUtil.setDepthFunc(oldDepthFuncMode);
    }

    public CubeMap getSkyBoxCubeMap() {
        return skyBoxCubeMap;
    }

    public void setSkyBoxCubeMap(CubeMap skyBoxCubeMap) {
        this.skyBoxCubeMap = skyBoxCubeMap;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public SkyBoxShader getShader() {
        return shader;
    }

    public void setShader(SkyBoxShader shader) {
        this.shader = shader;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }
}
