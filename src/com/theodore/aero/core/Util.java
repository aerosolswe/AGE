package com.theodore.aero.core;

import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

public class Util {

    public static FloatBuffer createFloatBuffer(int size) {
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer createIntBuffer(int size) {
        return BufferUtils.createIntBuffer(size);
    }

    public static ByteBuffer createByteBuffer(int size) {
        return BufferUtils.createByteBuffer(size);
    }

    public static IntBuffer createFlippedBuffer(int... values) {
        IntBuffer buffer = createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static IntBuffer createFlippedBuffer(Integer[] values) {
        IntBuffer buffer = createIntBuffer(values.length);

        for (int i = 0; i < values.length; i++) {
            buffer.put(values[i]);
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vector3[] values) {
        FloatBuffer buffer = createFloatBuffer(values.length * 3);

        for (int i = 0; i < values.length; i++) {
            buffer.put(values[i].x);
            buffer.put(values[i].y);
            buffer.put(values[i].z);
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vector2[] values) {
        FloatBuffer buffer = createFloatBuffer(values.length * 2);

        for (int i = 0; i < values.length; i++) {
            buffer.put(values[i].x);
            buffer.put(values[i].y);
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(float... values) {
        FloatBuffer buffer = createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Matrix4 value) {
        FloatBuffer buffer = createFloatBuffer(4 * 4);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                buffer.put(value.get(i, j));

        buffer.flip();

        return buffer;
    }

    public static ByteBuffer createFlippedBuffer(byte... values) {
        ByteBuffer buffer = createByteBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static ByteBuffer createFlippedByteBuffer(int... values) {
        ByteBuffer buffer = createByteBuffer(values.length * 4);
        for (int i = 0; i < values.length; i++) {
            buffer.putInt(values[i]);
        }
        buffer.flip();

        return buffer;
    }

    public static String[] removeEmptyStrings(String[] data) {
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < data.length; i++)
            if (!data[i].equals(""))
                result.add(data[i]);

        String[] res = new String[result.size()];
        result.toArray(res);

        return res;
    }

    public static int[] toIntArray(Integer[] data) {
        int[] result = new int[data.length];

        for (int i = 0; i < data.length; i++)
            result[i] = data[i].intValue();

        return result;
    }

    public static String getOpenGLVersion() {
        return glGetString(GL_VERSION);
    }

}
