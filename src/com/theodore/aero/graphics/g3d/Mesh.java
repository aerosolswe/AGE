package com.theodore.aero.graphics.g3d;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.Vertex;
import com.theodore.aero.graphics.g3d.meshLoading.IndexedModel;
import com.theodore.aero.graphics.g3d.meshLoading.OBJModel;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;

public class Mesh {

    public static final String DIRECTORY = "models/";
    private static final HashMap<String, Mesh> meshes = new HashMap<String, Mesh>();

    private int vbo;
    private int ibo;
    private int size;

    private String name;

    /**
     * @throws java.lang.Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (vbo != 0)
            Aero.graphicsUtil.deleteBuffer(vbo);
        if (ibo != 0)
            Aero.graphicsUtil.deleteBuffer(ibo);

        meshes.remove(name);
    }

    private Mesh(String name, Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
        this.name = name;
        this.vbo = 0;
        this.ibo = 0;
        this.size = 0;

        addVertices(vertices, indices, calcNormals, calcTangents);
    }

    public static Mesh get(String name) {
        if (meshes.containsKey(name))
            return meshes.get(name);
        else {
            meshes.put(name, loadMesh(name));
            return meshes.get(name);
        }
    }

    public static void put(String name, Mesh mesh) {
        meshes.put(name, mesh);
    }

    public static Mesh customMesh(String name, Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
        Mesh mesh = new Mesh(name, vertices, indices, calcNormals, calcTangents);
        meshes.put(name, mesh);
        return meshes.get(name);
    }

    public static void generatePrimitives() {
        generateRect();
        generatePlane();
        generateCube();
    }

    private static void generateRect() {
        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3(-0.5f, -0.5f, 0), new Vector2(0, 1)),
                new Vertex(new Vector3(-0.5f, 0.5f, 0), new Vector2(0, 0)),
                new Vertex(new Vector3(0.5f, 0.5f, 0), new Vector2(1, 0)),
                new Vertex(new Vector3(0.5f, -0.5f, 0), new Vector2(1, 1))};

        int[] indices = new int[]{0, 1, 2,
                0, 2, 3};

        String name = "rect";

        meshes.put(name, new Mesh(name, vertices, indices, true, true));
    }

    private static void generatePlane() {
        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3(-1, 0.0f, -1), new Vector2(0.0f, 0.0f)),
                new Vertex(new Vector3(-1, 0.0f, 1), new Vector2(0.0f, 1.0f)),
                new Vertex(new Vector3(1, 0.0f, -1), new Vector2(1.0f, 0.0f)),
                new Vertex(new Vector3(1, 0.0f, 1), new Vector2(1.0f, 1.0f))};

        int indices[] = new int[]{0, 1, 2,
                2, 1, 3};

        String plane = "plane";

        meshes.put(plane, new Mesh(plane, vertices, indices, true, true));
    }

    private static void generateCube() {
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

        meshes.put(name, new Mesh(name, vertices, indices, true, true));
    }

    private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents) {
        if (calcNormals)
            calcNormals(vertices, indices);
        if (calcTangents)
            calcTangents(vertices, indices);

        size = indices.length;

        vbo = Aero.graphicsUtil.createVertexBuffer(Util.createFlippedBuffer(vertices), true);
        ibo = Aero.graphicsUtil.createIndexBuffer(Util.createFlippedBuffer(indices), true);
    }

    public void draw() {
        Aero.graphicsUtil.drawTriangles(vbo, ibo, size);
    }

    private void calcNormals(Vertex[] vertices, int[] indices) {
        for (int i = 0; i < indices.length; i += 3) {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            Vector3 v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
            Vector3 v2 = vertices[i2].getPos().sub(vertices[i0].getPos());

            Vector3 normal = v1.cross(v2).normalized();

            vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
            vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
            vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
        }

        for (Vertex vertex : vertices)
            vertex.setNormal(vertex.getNormal().normalized());
    }

    private void calcTangents(Vertex[] vertices, int[] indices) {
        for (int i = 0; i < indices.length; i += 3) {
            Vertex v0 = vertices[indices[i]];
            Vertex v1 = vertices[indices[i + 1]];
            Vertex v2 = vertices[indices[i + 2]];

            Vector3 edge1 = v1.getPos().sub(v0.getPos());
            Vector3 edge2 = v2.getPos().sub(v0.getPos());

            float deltaU1 = v1.getTexCoord().getX() - v0.getTexCoord().getX();
            float deltaU2 = v2.getTexCoord().getX() - v0.getTexCoord().getX();
            float deltaV1 = v1.getTexCoord().getY() - v0.getTexCoord().getY();
            float deltaV2 = v2.getTexCoord().getY() - v0.getTexCoord().getY();

            float f = 1.0f / (deltaU1 * deltaV2 - deltaU2 * deltaV1);

            Vector3 tangent = new Vector3(0, 0, 0);

            tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
            tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
            tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

            v0.setTangent(v0.getTangent().add(tangent));
            v1.setTangent(v1.getTangent().add(tangent));
            v2.setTangent(v2.getTangent().add(tangent));
        }

        for (Vertex vertex : vertices)
            vertex.setTangent(vertex.getTangent().normalized());
    }

    public static Mesh loadMesh(String fileName) {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];

        if (!ext.equals("obj")) {
            System.err.println("Error: File format not supported for mesh data: " + ext);
            new Exception().printStackTrace();
            System.exit(1);
        }

        String path = Aero.getResourcePath(DIRECTORY + fileName);

        OBJModel test = new OBJModel(path);
        IndexedModel model = test.toIndexedModel();

        ArrayList<Vertex> vertices = new ArrayList<Vertex>();

        for (int i = 0; i < model.getPositions().size(); i++) {
            vertices.add(new Vertex(model.getPositions().get(i),
                    model.getTexCoords().get(i),
                    model.getNormals().get(i),
                    model.getTangents().get(i)));
        }

        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);

        Integer[] indexData = new Integer[model.getIndices().size()];
        model.getIndices().toArray(indexData);

        Mesh mesh = new Mesh(fileName, vertexData, Util.toIntArray(indexData), false, true);

//        addVertices();

        return mesh;
    }

    /*public static Mesh loadMesh(String fileName) {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];

        if (!ext.equals("obj")) {
            System.err.println("Error: File format not supported for mesh data: " + ext);
            new Exception().printStackTrace();
            System.exit(1);
        }

        String path = Aero.getResourcePath(DIRECTORY + fileName);

        OBJModel test = new OBJModel(path);
        IndexedModel model = test.toIndexedModel();
        model.calcNormals();

        ArrayList<Vertex> vertices = new ArrayList<Vertex>();

        for (int i = 0; i < model.getPositions().size(); i++) {
            vertices.add(new Vertex(model.getPositions().get(i),
                    model.getTexCoords().get(i),
                    model.getNormals().get(i)));
        }

        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);

        Integer[] indexData = new Integer[model.getIndices().size()];
        model.getIndices().toArray(indexData);

        return new Mesh(fileName, vertexData, Util.toIntArray(indexData), false, true);
    }*/

}
