package com.theodore.aero.graphics.mesh.meshLoading;

import com.theodore.aero.core.Util;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class OBJModel {
    private ArrayList<Vector3> positions;
    private ArrayList<Vector2> texCoords;
    private ArrayList<Vector3> normals;
    private ArrayList<OBJIndex> indices;
    private boolean hasTexCoords;
    private boolean hasNormals;

    public OBJModel(File file) {
        positions = new ArrayList<Vector3>();
        texCoords = new ArrayList<Vector2>();
        normals = new ArrayList<Vector3>();
        indices = new ArrayList<OBJIndex>();
        hasTexCoords = false;
        hasNormals = false;

        BufferedReader meshReader = null;

        try {
            meshReader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = meshReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                tokens = Util.removeEmptyStrings(tokens);

                if (tokens.length == 0 || tokens[0].equals("#"))
                    continue;
                else if (tokens[0].equals("v")) {
                    positions.add(new Vector3(Float.valueOf(tokens[1]),
                            Float.valueOf(tokens[2]),
                            Float.valueOf(tokens[3])));
                } else if (tokens[0].equals("vt")) {
                    texCoords.add(new Vector2(Float.valueOf(tokens[1]),
                            1.0f - Float.valueOf(tokens[2])));
                } else if (tokens[0].equals("vn")) {
                    normals.add(new Vector3(Float.valueOf(tokens[1]),
                            Float.valueOf(tokens[2]),
                            Float.valueOf(tokens[3])));
                } else if (tokens[0].equals("f")) {
                    for (int i = 0; i < tokens.length - 3; i++) {
                        indices.add(ParseOBJIndex(tokens[1]));
                        indices.add(ParseOBJIndex(tokens[2 + i]));
                        indices.add(ParseOBJIndex(tokens[3 + i]));
                    }
                }
            }

            meshReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public IndexedModel toIndexedModel() {
        IndexedModel result = new IndexedModel();
        IndexedModel normalModel = new IndexedModel();
        HashMap<OBJIndex, Integer> resultIndexMap = new HashMap<OBJIndex, Integer>();
        HashMap<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

        for (int i = 0; i < indices.size(); i++) {
            OBJIndex currentIndex = indices.get(i);

            Vector3 currentPosition = positions.get(currentIndex.getVertexIndex());
            Vector2 currentTexCoord;
            Vector3 currentNormal;

            if (hasTexCoords)
                currentTexCoord = texCoords.get(currentIndex.getTexCoordIndex());
            else
                currentTexCoord = new Vector2(0, 0);

            if (hasNormals)
                currentNormal = normals.get(currentIndex.getNormalIndex());
            else
                currentNormal = new Vector3(0, 0, 0);

            Integer modelVertexIndex = resultIndexMap.get(currentIndex);

            if (modelVertexIndex == null) {
                modelVertexIndex = result.positions.size();
                resultIndexMap.put(currentIndex, modelVertexIndex);

                result.positions.add(currentPosition);
                result.texCoords.add(currentTexCoord);
                if (hasNormals)
                    result.normals.add(currentNormal);
            }

            Integer normalModelIndex = normalIndexMap.get(currentIndex.getVertexIndex());

            if (normalModelIndex == null) {
                normalModelIndex = normalModel.positions.size();
                normalIndexMap.put(currentIndex.getVertexIndex(), normalModelIndex);

                normalModel.positions.add(currentPosition);
                normalModel.texCoords.add(currentTexCoord);
                normalModel.normals.add(currentNormal);
                normalModel.tangents.add(new Vector3(0, 0, 0));
            }

            result.indices.add(modelVertexIndex);
            normalModel.indices.add(normalModelIndex);
            indexMap.put(modelVertexIndex, normalModelIndex);
        }

        if (!hasNormals) {
            normalModel.calcNormals();

            for (int i = 0; i < result.positions.size(); i++)
                result.normals.add(normalModel.normals.get(indexMap.get(i)));
        }

        normalModel.calcTangents();

        for (int i = 0; i < result.positions.size(); i++)
            result.tangents.add(normalModel.tangents.get(indexMap.get(i)));

        return result;
    }

    private OBJIndex ParseOBJIndex(String token) {
        String[] values = token.split("/");

        OBJIndex result = new OBJIndex();
        result.setVertexIndex(Integer.parseInt(values[0]) - 1);

        if (values.length > 1) {
            if (!values[1].isEmpty()) {
                hasTexCoords = true;
                result.setTexCoordIndex(Integer.parseInt(values[1]) - 1);
            }

            if (values.length > 2) {
                hasNormals = true;
                result.setNormalIndex(Integer.parseInt(values[2]) - 1);
            }
        }

        return result;
    }
}