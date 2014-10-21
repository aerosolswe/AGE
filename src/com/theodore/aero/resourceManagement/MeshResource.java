package com.theodore.aero.resourceManagement;

import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.Vertex;
import com.theodore.aero.graphics.mesh.meshLoading.IndexedModel;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;

public class MeshResource {

    private int vao;
    private int refCount;
    private int drawCount;

    private int[] buffers = {0, 1, 2, 3, 4};

    private IndexedModel model;

    public MeshResource(IndexedModel model) {
        this.model = model;
        this.refCount = 1;
        this.drawCount = model.getIndices().size();

        if (!model.isValid()) {
            System.err.println("Error: Invalid mesh! Must have same number of positions, texCoords, normals, and tangents! (Maybe you forgot to finish() your IndexedModel?)");
            System.exit(1);
        }

        vao = createVAO();

        Integer[] indexData = new Integer[model.getIndices().size()];
        model.getIndices().toArray(indexData);
        bindIndiciesBuffer(Util.toIntArray(indexData));

        Vector3[] posArray = new Vector3[model.getPositions().size()];
        model.getPositions().toArray(posArray);
        storeDataInAttributeList(0, posArray);

        Vector2[] texArray = new Vector2[model.getTexCoords().size()];
        model.getTexCoords().toArray(texArray);
        storeDataInAttributeList(1, texArray);

        Vector3[] normsArray = new Vector3[model.getNormals().size()];
        model.getNormals().toArray(normsArray);
        storeDataInAttributeList(2, normsArray);

        Vector3[] tangentsArray = new Vector3[model.getTangents().size()];
        model.getTangents().toArray(tangentsArray);
        storeDataInAttributeList(3, tangentsArray);

        unbindVao();

    }

    public void draw(int mode) {
        glBindVertexArray(vao);

        glDrawElements(mode, drawCount, GL_UNSIGNED_INT, 0);

        unbindVao();
    }

    private int createVAO() {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        return vaoId;
    }

    private void storeDataInAttributeList(int attribNumber, Vector3[] data) {
        buffers[attribNumber] = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, buffers[attribNumber]);

        FloatBuffer buffer = Util.createFlippedBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(attribNumber);
        glVertexAttribPointer(attribNumber, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void storeDataInAttributeList(int attribNumber, Vector2[] data) {
        buffers[attribNumber] = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, buffers[attribNumber]);

        FloatBuffer buffer = Util.createFlippedBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(attribNumber);
        glVertexAttribPointer(attribNumber, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void unbindVao() {
        glBindVertexArray(0);
    }

    private void bindIndiciesBuffer(int[] indices) {
        buffers[4] = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[4]);

        IntBuffer buffer = Util.createFlippedBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
    }

    @Override
    protected void finalize() {
        for (int buffer : buffers) glDeleteBuffers(buffer);

        glDeleteVertexArrays(vao);
    }

    public void addReference() {
        refCount++;
    }

    public boolean removeReference() {
        refCount--;
        return refCount == 0;
    }

    public int getRefCount() {
        return refCount;
    }

    public int getVao() {
        return vao;
    }

    public IndexedModel getModel() {
        return model;
    }
}
