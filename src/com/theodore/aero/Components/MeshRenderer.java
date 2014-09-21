package com.theodore.aero.components;

import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.Shader;

public class MeshRenderer extends GameComponent {

    private Mesh[] meshes;
    private Material[] materials;

    public MeshRenderer(Mesh mesh, Material material) {
        this.meshes = new Mesh[]{ mesh };
        this.materials = new Material[]{ material };
    }

    public MeshRenderer(Mesh[] meshes, Material[] materials){
        this.meshes = meshes;
        this.materials = materials;

    }

    @Override
    public void render(Shader shader, Graphics graphics) {
        shader.bind();
        for(int i = 0; i < meshes.length; i++){
            shader.updateUniforms(getTransform(), materials[i], graphics);
            meshes[i].draw();
        }
    }

    public Mesh[] getMeshes() {
        return meshes;
    }

    public void setMeshes(Mesh[] meshes) {
        this.meshes = meshes;
    }

    public Material[] getMaterials() {
        return materials;
    }

    public void setMaterials(Material[] materials) {
        this.materials = materials;
    }
}
