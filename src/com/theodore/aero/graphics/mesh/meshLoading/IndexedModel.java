package com.theodore.aero.graphics.mesh.meshLoading;

import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

import java.util.ArrayList;

public class IndexedModel {

    public ArrayList<Vector3> positions;
    public ArrayList<Vector2> texCoords;
    public ArrayList<Vector3> normals;
    public ArrayList<Vector3> tangents;
    public ArrayList<Integer> indices;

    public IndexedModel() {
        positions = new ArrayList<Vector3>();
        texCoords = new ArrayList<Vector2>();
        normals = new ArrayList<Vector3>();
        tangents = new ArrayList<Vector3>();
        indices = new ArrayList<Integer>();
    }

    public IndexedModel(ArrayList<Vector3> positions, ArrayList<Vector2> texCoords, ArrayList<Vector3> normals, ArrayList<Vector3> tangents, ArrayList<Integer> indices) {
        this.positions = positions;
        this.texCoords = texCoords;
        this.normals = normals;
        this.tangents = tangents;
        this.indices = indices;
    }

    public boolean isValid() {
        return positions.size() == texCoords.size() &&
                texCoords.size() == normals.size() &&
                normals.size() == tangents.size();
    }

    public void addVertex(Vector3 vertex) {
        positions.add(vertex);
    }

    public void addTexCoord(Vector2 texCoord) {
        texCoords.add(texCoord);
    }

    public void addNormal(Vector3 normal) {
        normals.add(normal);
    }

    public void addtangent(Vector3 tangent) {
        tangents.add(tangent);
    }

    public IndexedModel finish() {
        if (isValid()) {
            return this;
        }

        if (texCoords.size() == 0) {
            for (int i = texCoords.size(); i < positions.size(); i++) {
                texCoords.add(new Vector2(0, 0));
            }
        }

        if (normals.size() == 0) {
            calcNormals();
        }

        if (tangents.size() == 0) {
            calcTangents();
        }

        return this;
    }

    public void addFace(int vertIndex0, int vertIndex1, int vertIndex2) {
        indices.add(vertIndex0);
        indices.add(vertIndex1);
        indices.add(vertIndex2);
    }

    public void calcNormals() {
        normals.clear();

        for (int i = 0; i < positions.size(); i++) {
            normals.add(new Vector3());
        }

        for (int i = 0; i < indices.size(); i += 3) {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vector3 v1 = positions.get(i1).sub(positions.get(i0));
            Vector3 v2 = positions.get(i2).sub(positions.get(i0));

            Vector3 normal = v1.cross(v2).normalized();

            normals.get(i0).set(normals.get(i0).add(normal));
            normals.get(i1).set(normals.get(i1).add(normal));
            normals.get(i2).set(normals.get(i2).add(normal));
        }

        for (int i = 0; i < normals.size(); i++)
            normals.get(i).set(normals.get(i).normalized());
    }

    public void calcTangents() {
        tangents.clear();

        for (int i = 0; i < positions.size(); i++) {
            tangents.add(new Vector3());
        }

        for (int i = 0; i < indices.size(); i += 3) {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vector3 edge1 = positions.get(i1).sub(positions.get(i0));
            Vector3 edge2 = positions.get(i2).sub(positions.get(i0));

            float deltaU1 = texCoords.get(i1).getX() - texCoords.get(i0).getX();
            float deltaV1 = texCoords.get(i1).getY() - texCoords.get(i0).getY();
            float deltaU2 = texCoords.get(i2).getX() - texCoords.get(i0).getX();
            float deltaV2 = texCoords.get(i2).getY() - texCoords.get(i0).getY();

            float dividend = (deltaU1 * deltaV2 - deltaU2 * deltaV1);
            //TODO: The first 0.0f may need to be changed to 1.0f here.
            float f = dividend == 0 ? 0.0f : 1.0f / dividend;

            Vector3 tangent = new Vector3(0, 0, 0);
            tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
            tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
            tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

            tangents.get(i0).set(tangents.get(i0).add(tangent));
            tangents.get(i1).set(tangents.get(i1).add(tangent));
            tangents.get(i2).set(tangents.get(i2).add(tangent));
        }

        for (int i = 0; i < tangents.size(); i++)
            tangents.get(i).set(tangents.get(i).normalized());
    }

}