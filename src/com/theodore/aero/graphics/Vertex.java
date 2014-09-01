package com.theodore.aero.graphics;

import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

import java.nio.FloatBuffer;

public class Vertex {

    public static final int SIZE = 11;

    private Vector3 pos;
    private Vector2 texCoord;
    private Vector3 normal;
    private Vector3 tangent;

    public Vertex(Vector3 pos) {
        this(pos, new Vector2(0, 0));
    }

    public Vertex(Vector2 texCoord) {
        this(new Vector3(0, 0, 0), texCoord);
    }

    public Vertex(Vector3 pos, Vector2 texCoord) {
        this(pos, texCoord, new Vector3(0, 0, 0));
    }

    public Vertex(Vector3 pos, Vector2 texCoord, Vector3 normal) {
        this(pos, texCoord, normal, new Vector3(0, 0, 0));
    }

    public Vertex(Vector3 pos, Vector2 texCoord, Vector3 normal, Vector3 tangent) {
        this.pos = pos;
        this.texCoord = texCoord;
        this.normal = normal;
        this.tangent = tangent;
    }

    public void addToBuffer(FloatBuffer buffer) {
        buffer.put(getPos().getX());
        buffer.put(getPos().getY());
        buffer.put(getPos().getZ());
        buffer.put(getTexCoord().getX());
        buffer.put(getTexCoord().getY());
        buffer.put(getNormal().getX());
        buffer.put(getNormal().getY());
        buffer.put(getNormal().getZ());
        buffer.put(getTangent().getX());
        buffer.put(getTangent().getY());
        buffer.put(getTangent().getZ());
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public Vector2 getTexCoord() {
        return texCoord;
    }

    public void setTexCoord(Vector2 texCoord) {
        this.texCoord = texCoord;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public void setNormal(Vector3 normal) {
        this.normal = normal;
    }

    public Vector3 getTangent() {
        return tangent;
    }

    public void setTangent(Vector3 tangent) {
        this.tangent = tangent;
    }
}
