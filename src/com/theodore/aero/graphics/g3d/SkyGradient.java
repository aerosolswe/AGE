package com.theodore.aero.graphics.g3d;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.sky.SkyGradientShader;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;

public class SkyGradient {

    private Mesh mesh;
    private SkyGradientShader shader;
    private Transform transform;

    private Texture color;
    private Texture glow;

    private Vector3 lightPosition;

    public SkyGradient(Vector3 lightPosition){
        this.shader = SkyGradientShader.getInstance();

        this.mesh = Mesh.load("dome", Aero.files.internal("default/models/dome.obj"));
        this.transform = new Transform();
        this.lightPosition = lightPosition;
        Texture.load("sky2", Aero.files.internal("default/textures/sky2.png"));
        Texture.load("glow2", Aero.files.internal("default/textures/glow2.png"));
        color = Texture.get("sky2");
        glow = Texture.get("glow2");

        transform.setScale(new Vector3(100, 100, 100));
        transform.setPosition(new Vector3(0, -50, 0));
    }

    public void update(Vector3 lightPosition){
        this.lightPosition = lightPosition;
    }

    public void render(Graphics graphics){
        int oldCullFaceMode = glGetInteger(GL_CULL_FACE_MODE);
        int oldDepthFuncMode = glGetInteger(GL_DEPTH_FUNC);

        Aero.graphicsUtil.disableCullFace();
        Aero.graphicsUtil.setDepthFunc(GL_LEQUAL);

        Camera tmp = graphics.getMainCamera();

        this.shader.bind();
        this.shader.updateUniforms(transform, graphics, lightPosition);
        glow.bind(GL13.GL_TEXTURE1);
        color.bind(GL13.GL_TEXTURE0);
        mesh.draw();

        graphics.setMainCamera(tmp);

        Aero.graphicsUtil.enableCullFace(oldCullFaceMode);
//        Aero.graphicsUtil.setDepthFunc(oldDepthFuncMode);
    }
}
