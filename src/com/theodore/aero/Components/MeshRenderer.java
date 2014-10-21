package com.theodore.aero.components;

import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.Shader;
import org.lwjgl.opengl.GL11;

public class MeshRenderer extends GameComponent {

    private Mesh mesh;
    private Material material;

    public MeshRenderer(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void render(Shader shader, Graphics graphics) {
        shader.bind();
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        material.getTexture("normalMap").bind(graphics.getSamplerSlot("normalMap"));
        material.getTexture("dispMap").bind(graphics.getSamplerSlot("dispMap"));
        shader.updateUniforms(getTransform(), material, graphics);
        mesh.draw(GL11.GL_TRIANGLES);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
