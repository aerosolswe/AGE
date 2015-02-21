package com.theodore.aero.graphics.sky;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.SkyAtmosphereShader;
import com.theodore.aero.math.Vector3;

import static org.lwjgl.opengl.GL11.*;

public class SkyAtmosphere extends Sky {

    private GameObject gameObject;
    private Mesh mesh;
    private Material material;

    private SkyAtmosphereShader shader;

    public SkyAtmosphere() {
        shader = new SkyAtmosphereShader();

        mesh = new Mesh("sphere");
        material = new Material(new Texture("starmap", Aero.files.internal("default/textures/starmap.png")));
        setStandardValues();

        gameObject = new GameObject();
        gameObject.getTransform().setScale(new Vector3(getFloat("domeRadius"), getFloat("domeRadius"), getFloat("domeRadius")));
        gameObject.getTransform().setPosition(new Vector3(0, 10, 0));
    }

    public void setStandardValues() {
        setFloat("starBrightness", 0.8f);

        setFloat("kr", 0.0075f); // 0.0025
        setFloat("km", 0.0065f); // 0.0015
        setFloat("eSun", 25.0f); //25

        setFloat("krESun", getFloat("kr") * getFloat("eSun"));
        setFloat("kmESun", getFloat("km") * getFloat("eSun"));
        setFloat("kr4PI", getFloat("kr") * 4.0f * (float) Math.PI);
        setFloat("km4PI", getFloat("km") * 4.0f * (float) Math.PI);

        setFloat("g", -0.95f);

        setFloat("innerRadius", 10);
//        setFloat("outerRadius", getFloat("innerRadius") * 1.025f);
        setFloat("outerRadius", getFloat("innerRadius") * 1.08f);

        setFloat("domeRadius", 10);

        setFloat("rayleighScaleDepth", 0.25f);

        setFloat("scale", 1 / (getFloat("outerRadius") - getFloat("innerRadius")));
        setFloat("scaleDepth", getFloat("rayleighScaleDepth"));
        setFloat("scaleOverScaleDepth", getFloat("scale") / getFloat("scaleDepth"));

        setFloat("exposure", 3);

        setVector3("lightDir", new Vector3());

        setVector3("cameraPos", new Vector3(0, getFloat("innerRadius"), 0));

        setFloat("cameraHeight", getVector3("cameraPos").length());
    }

    public void render(Graphics graphics) {
        int oldCullFaceMode = glGetInteger(GL_CULL_FACE_MODE);

        Aero.graphicsUtil.enableCullFace(GL_FRONT);
        Aero.graphicsUtil.setDepthFunc(GL_LEQUAL);
        Aero.graphicsUtil.disableBlending();

        Camera tmp = graphics.getMainCamera();

        gameObject.getTransform().setPosition(graphics.getMainCamera().getTransform().getTransformedPos().sub(new Vector3(0, 0.2f, 0)));

        shader.bind();
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        shader.updateShader(gameObject.getTransform(), material, graphics, this);
        mesh.draw(GL_TRIANGLES);

        graphics.setMainCamera(tmp);

        Aero.graphicsUtil.enableCullFace(oldCullFaceMode);
    }

}
