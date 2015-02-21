package com.theodore.aero.resources;

import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.mesh.meshLoading.IndexedModel;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

public class MeshResource {

    private int vao;
    private int refCount;
    private int drawCount;

    private int[] buffers = {0, 1, 2, 3, 4};

    private IndexedModel model;
    private ByteBuffer indicesBuffer;

    public MeshResource(IndexedModel model) {
        this.model = model;
        this.refCount = 1;
        this.drawCount = model.indices.size();

        if (!model.isValid()) {
            System.err.println("Error: Invalid mesh! Must have same number of positions, texCoords, normals, and tangents! (Maybe you forgot to finish() your IndexedModel?)");
            System.exit(1);
        }

        vao = createVAO();

        Integer[] indexData = new Integer[model.indices.size()];
        model.indices.toArray(indexData);
        bindIndiciesBuffer(Util.toIntArray(indexData));

        Vector3[] posArray = new Vector3[model.positions.size()];
        model.positions.toArray(posArray);
        storeDataInAttributeList(0, posArray);

        Vector2[] texArray = new Vector2[model.texCoords.size()];
        model.texCoords.toArray(texArray);
        storeDataInAttributeList(1, texArray);

        Vector3[] normsArray = new Vector3[model.normals.size()];
        model.normals.toArray(normsArray);
        storeDataInAttributeList(2, normsArray);

        Vector3[] tangentsArray = new Vector3[model.tangents.size()];
        model.tangents.toArray(tangentsArray);
        storeDataInAttributeList(3, tangentsArray);

        unbindVao();

        for (int i = 0; i < buffers.length; i++) {
            glDisableVertexAttribArray(i);
        }
    }

    public void bindVertexArray(int vao) {
        glBindVertexArray(vao);
    }

    public void drawElements() {
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
    }

    public void draw(int mode) {
        bindVertexArray(vao);
        glDrawElements(mode, drawCount, GL_UNSIGNED_INT, 0);
        unbindVao();
    }


    public void unbindVao() {
        glBindVertexArray(0);
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
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(attribNumber);
        glVertexAttribPointer(attribNumber, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void storeDataInAttributeList(int attribNumber, Vector2[] data) {
        buffers[attribNumber] = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, buffers[attribNumber]);

        FloatBuffer buffer = Util.createFlippedBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(attribNumber);
        glVertexAttribPointer(attribNumber, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void bindIndiciesBuffer(int[] indices) {
        buffers[4] = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[4]);

        indicesBuffer = Util.createFlippedByteBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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
