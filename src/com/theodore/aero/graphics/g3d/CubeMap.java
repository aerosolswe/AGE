package com.theodore.aero.graphics.g3d;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Mesh;
import com.theodore.aero.graphics.Shader;
import com.theodore.aero.graphics.Vertex;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

public class CubeMap {

    private Shader shader;
    private Mesh mesh;
    private Transform transform;

    public CubeMap() {

        shader = BasicShader.getInstance();
        transform = new Transform();

        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3(-0.5f, -0.5f, -0.5f), new Vector2(0, 1)),
                new Vertex(new Vector3(-0.5f, 0.5f, -0.5f), new Vector2(0, 0)),
                new Vertex(new Vector3(0.5f, 0.5f, -0.5f), new Vector2(1, 0)),
                new Vertex(new Vector3(0.5f, -0.5f, -0.5f), new Vector2(1, 1)),

                new Vertex(new Vector3(-0.5f, -0.5f, -0.5f), new Vector2(1, 0)),
                new Vertex(new Vector3(0.5f, -0.5f, -0.5f), new Vector2(0, 0)),
                new Vertex(new Vector3(0.5f, -0.5f, 0.5f), new Vector2(0, 1)),
                new Vertex(new Vector3(-0.5f, -0.5f, 0.5f), new Vector2(1, 1)),

                new Vertex(new Vector3(-0.5f, -0.5f, -0.5f), new Vector2(1, 1)),
                new Vertex(new Vector3(-0.5f, -0.5f, 0.5f), new Vector2(0, 1)),
                new Vertex(new Vector3(-0.5f, 0.5f, 0.5f), new Vector2(0, 0)),
                new Vertex(new Vector3(-0.5f, 0.5f, -0.5f), new Vector2(1, 0)),

                new Vertex(new Vector3(-0.5f, -0.5f, 0.5f), new Vector2(1, 1)),
                new Vertex(new Vector3(0.5f, -0.5f, 0.5f), new Vector2(0, 1)),
                new Vertex(new Vector3(0.5f, 0.5f, 0.5f), new Vector2(0, 0)),
                new Vertex(new Vector3(-0.5f, 0.5f, 0.5f), new Vector2(1, 0)),

                new Vertex(new Vector3(-0.5f, 0.5f, -0.5f), new Vector2(0, 1)),
                new Vertex(new Vector3(-0.5f, 0.5f, 0.5f), new Vector2(0, 0)),
                new Vertex(new Vector3(0.5f, 0.5f, 0.5f), new Vector2(1, 0)),
                new Vertex(new Vector3(0.5f, 0.5f, -0.5f), new Vector2(1, 1)),

                new Vertex(new Vector3(0.5f, -0.5f, -0.5f), new Vector2(0, 1)),
                new Vertex(new Vector3(0.5f, 0.5f, -0.5f), new Vector2(0, 0)),
                new Vertex(new Vector3(0.5f, 0.5f, 0.5f), new Vector2(1, 0)),
                new Vertex(new Vector3(0.5f, -0.5f, 0.5f), new Vector2(1, 1))};

        int[] indices = new int[]{
                0, 1, 2,
                0, 2, 3,

                4, 5, 6,
                4, 6, 7,

                8, 9, 10,
                8, 10, 11,

                12, 13, 14,
                12, 14, 15,

                16, 17, 18,
                16, 18, 19,

                20, 21, 22,
                20, 22, 23
        };

        mesh = Mesh.customMesh("cubeMapMesh", vertices, indices, false, false);
    }

    public void render() {
        Aero.graphicsUtil.disableCullFace();
        shader.bind();
        shader.updateUniforms(Material.getDefaultMaterials());
        shader.updateUniformWorldMatrix(transform.getMVP());
        mesh.draw();
        Aero.graphicsUtil.init();
    }

}
