package com.theodore.aero.graphics.g3d.meshLoading;

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
                modelVertexIndex = result.getPositions().size();
                resultIndexMap.put(currentIndex, modelVertexIndex);

                result.getPositions().add(currentPosition);
                result.getTexCoords().add(currentTexCoord);
                if (hasNormals)
                    result.getNormals().add(currentNormal);
            }

            Integer normalModelIndex = normalIndexMap.get(currentIndex.getVertexIndex());

            if (normalModelIndex == null) {
                normalModelIndex = normalModel.getPositions().size();
                normalIndexMap.put(currentIndex.getVertexIndex(), normalModelIndex);

                normalModel.getPositions().add(currentPosition);
                normalModel.getTexCoords().add(currentTexCoord);
                normalModel.getNormals().add(currentNormal);
                normalModel.getTangents().add(new Vector3(0, 0, 0));
            }

            result.getIndices().add(modelVertexIndex);
            normalModel.getIndices().add(normalModelIndex);
            indexMap.put(modelVertexIndex, normalModelIndex);
        }

        if (!hasNormals) {
            normalModel.calcNormals();

            for (int i = 0; i < result.getPositions().size(); i++)
                result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
        }

        normalModel.calcTangents();

        for (int i = 0; i < result.getPositions().size(); i++)
            result.getTangents().add(normalModel.getTangents().get(indexMap.get(i)));

//		for(int i = 0; i < result.GetTexCoords().size(); i++)
//			result.GetTexCoords().Get(i).SetY(1.0f - result.GetTexCoords().Get(i).GetY());

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