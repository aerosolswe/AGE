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
    public void renderBasic(Shader shader, Graphics graphics) {
        shader.bind();
        material.getTexture("blendMap").bind(graphics.getSamplerSlot("blendMap"));
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        material.getTexture("rdiffuse").bind(graphics.getSamplerSlot("rdiffuse"));
        material.getTexture("gdiffuse").bind(graphics.getSamplerSlot("gdiffuse"));
        material.getTexture("bdiffuse").bind(graphics.getSamplerSlot("bdiffuse"));
        shader.updateUniforms(getTransform(), material, graphics);
        mesh.draw(GL11.GL_TRIANGLES);
        shader.unbind();
    }

    @Override
    public void renderShadow(Shader shader, Graphics graphics) {
        shader.bind();
        shader.updateUniforms(getTransform(), null, graphics);
        material.getTexture("blendMap").bind(graphics.getSamplerSlot("blendMap"));
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        material.getTexture("rdiffuse").bind(graphics.getSamplerSlot("rdiffuse"));
        material.getTexture("gdiffuse").bind(graphics.getSamplerSlot("gdiffuse"));
        material.getTexture("bdiffuse").bind(graphics.getSamplerSlot("bdiffuse"));
        mesh.draw(GL11.GL_TRIANGLES);
        shader.unbind();
    }

    @Override
    public void renderLight(Shader shader, Graphics graphics) {
        shader.bind();
        shader.updateUniforms(getTransform(), material, graphics);
        material.getTexture("blendMap").bind(graphics.getSamplerSlot("blendMap"));
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        material.getTexture("normalMap").bind(graphics.getSamplerSlot("normalMap"));
        material.getTexture("specularMap").bind(graphics.getSamplerSlot("specularMap"));
        material.getTexture("dispMap").bind(graphics.getSamplerSlot("dispMap"));

        material.getTexture("rdiffuse").bind(graphics.getSamplerSlot("rdiffuse"));
        material.getTexture("rnormalMap").bind(graphics.getSamplerSlot("rnormalMap"));
        material.getTexture("rspecularMap").bind(graphics.getSamplerSlot("rspecularMap"));
        material.getTexture("rdispMap").bind(graphics.getSamplerSlot("rdispMap"));

        material.getTexture("gdiffuse").bind(graphics.getSamplerSlot("gdiffuse"));
        material.getTexture("gnormalMap").bind(graphics.getSamplerSlot("gnormalMap"));
        material.getTexture("gspecularMap").bind(graphics.getSamplerSlot("gspecularMap"));
        material.getTexture("gdispMap").bind(graphics.getSamplerSlot("gdispMap"));

        material.getTexture("bdiffuse").bind(graphics.getSamplerSlot("bdiffuse"));
        material.getTexture("bnormalMap").bind(graphics.getSamplerSlot("bnormalMap"));
        material.getTexture("bspecularMap").bind(graphics.getSamplerSlot("bspecularMap"));
        material.getTexture("bdispMap").bind(graphics.getSamplerSlot("bdispMap"));
        mesh.draw(GL11.GL_TRIANGLES);
        shader.unbind();
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
