package com.theodore.aero.Components;

import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.g3d.Mesh;
import com.theodore.aero.graphics.shaders.Shader;

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
        shader.updateUniforms(getTransform(), material, graphics);
        mesh.draw();
    }
}
