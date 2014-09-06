package com.theodore.aero.graphics.mesh;

import com.theodore.aero.graphics.Vertex;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

public class Primitives {

    public static Mesh rectangle() {
        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3(-0.5f, -0.5f, 0), new Vector2(0, 1)),
                new Vertex(new Vector3(-0.5f, 0.5f, 0), new Vector2(0, 0)),
                new Vertex(new Vector3(0.5f, 0.5f, 0), new Vector2(1, 0)),
                new Vertex(new Vector3(0.5f, -0.5f, 0), new Vector2(1, 1))};

        int[] indices = new int[]{0, 1, 2,
                0, 2, 3};

        String name = "rectangle";

        return Mesh.customMesh(name, vertices, indices, true, true);
    }

    public static Mesh plane() {
        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3(-1, 0.0f, -1), new Vector2(0.0f, 0.0f)),
                new Vertex(new Vector3(-1, 0.0f, 1), new Vector2(0.0f, 1.0f)),
                new Vertex(new Vector3(1, 0.0f, -1), new Vector2(1.0f, 0.0f)),
                new Vertex(new Vector3(1, 0.0f, 1), new Vector2(1.0f, 1.0f))};

        int indices[] = new int[]{0, 1, 2,
                2, 1, 3};

        String name = "plane";

        return Mesh.customMesh(name, vertices, indices, true, true);
    }

    public static Mesh cube() {
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

        int[] indices = new int[]{0, 1, 2,
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
                20, 22, 23};

        String name = "cube";

        return Mesh.customMesh(name, vertices, indices, true, true);
    }
}
