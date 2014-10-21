package com.theodore.aero.resourceManagement;

import com.theodore.aero.graphics.Texture;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;

import java.util.HashMap;

public abstract class MappedValues {

    private HashMap<String, Texture> textureHashMap;

    private HashMap<String, Matrix4> matrix4HashMap;
    private HashMap<String, Vector3> vector3HashMap;

    private HashMap<String, Float> floatHashMap;
    private HashMap<String, Integer> intHashMap;

    private HashMap<String, Boolean> booleanHashMap;

    public MappedValues() {
        matrix4HashMap = new HashMap<String, Matrix4>();
        vector3HashMap = new HashMap<String, Vector3>();
        floatHashMap = new HashMap<String, Float>();
        intHashMap = new HashMap<String, Integer>();
        booleanHashMap = new HashMap<String, Boolean>();
        textureHashMap = new HashMap<String, Texture>();
    }

    public void setMatrix4(String name, Matrix4 matrix) {
        matrix4HashMap.put(name, matrix);
    }

    public void setVector3(String name, Vector3 vector3) {
        vector3HashMap.put(name, vector3);
    }

    public void setFloat(String name, float floatValue) {
        floatHashMap.put(name, floatValue);
    }

    public void setInteger(String name, int intValue) {
        intHashMap.put(name, intValue);
    }

    public void setBoolean(String name, boolean boolValue) {
        booleanHashMap.put(name, boolValue);
    }

    public void setTexture(String name, Texture texture) {
        textureHashMap.put(name, texture);
    }

    public Matrix4 getMatrix4(String name) {
        Matrix4 result = matrix4HashMap.get(name);
        if (result != null)
            return result;

        return new Matrix4();
    }

    public Vector3 getVector3(String name) {
        Vector3 result = vector3HashMap.get(name);
        if (result != null)
            return result;

        return new Vector3(0, 0, 0);
    }

    public float getFloat(String name) {
        Float result = floatHashMap.get(name);
        if (result != null)
            return result;

        return 0;
    }

    public int getInteger(String name) {
        Integer result = intHashMap.get(name);
        if (result != null)
            return result;

        return 0;
    }

    public boolean getBoolean(String name) {
        return booleanHashMap.get(name);
    }

    public Texture getTexture(String name) {
        Texture result = textureHashMap.get(name);
        if (result != null)
            return result;

        return new Texture("default");
    }

}
