package com.theodore.aero.graphics.sky;

import com.theodore.aero.components.Camera;
import com.theodore.aero.components.MeshRenderer;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.SkyBoxShader;
import com.theodore.aero.graphics.sky.Sky;
import com.theodore.aero.math.Vector3;

import java.io.File;

import static org.lwjgl.opengl.GL11.*;

public class SkyBox extends Sky {

    public SkyBox() {
        this(Aero.files.internal("default/cubemaps/front.png"),
                Aero.files.internal("default/cubemaps/back.png"),
                Aero.files.internal("default/cubemaps/top.png"),
                Aero.files.internal("default/cubemaps/bottom.png"),
                Aero.files.internal("default/cubemaps/left.png"),
                Aero.files.internal("default/cubemaps/right.png")
        );
    }

    public SkyBox(File front, File back, File top, File bottom, File left, File right) {
        this.shader = new SkyBoxShader();

        meshRenderer = new MeshRenderer(new Mesh("sphere"), new Material(new Texture(new File[]{front, back, top, bottom, left, right})));
        GameObject gameObject = new GameObject().addComponent(meshRenderer);

        gameObject.getTransform().setScale(new Vector3(1000, 1000, 1000));
        gameObject.getTransform().setPosition(new Vector3(0, -10, 0));
    }

    @Override
    public void render(Graphics graphics) {
        int oldCullFaceMode = glGetInteger(GL_CULL_FACE_MODE);

        Aero.graphicsUtil.enableCullFace(GL_FRONT);
        Aero.graphicsUtil.setDepthFunc(GL_LEQUAL);
//        Aero.graphicsUtil.disableBlending();

        Camera tmp = graphics.getMainCamera();

        meshRenderer.renderBasic(shader, graphics);

        graphics.setMainCamera(tmp);

        Aero.graphicsUtil.enableCullFace(oldCullFaceMode);
    }

}
