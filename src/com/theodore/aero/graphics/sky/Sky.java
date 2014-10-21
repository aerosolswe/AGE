package com.theodore.aero.graphics.sky;

import com.theodore.aero.components.MeshRenderer;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.resourceManagement.MappedValues;

public abstract class Sky extends MappedValues {

    protected Shader shader;
    protected MeshRenderer meshRenderer;

    public abstract void render(Graphics graphics);

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public MeshRenderer getMeshRenderer() {
        return meshRenderer;
    }

    public void setMeshRenderer(MeshRenderer meshRenderer) {
        this.meshRenderer = meshRenderer;
    }
}
