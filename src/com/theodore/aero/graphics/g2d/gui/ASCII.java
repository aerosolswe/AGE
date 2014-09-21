package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.Vertex;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL11;

import java.io.*;

public class ASCII {

    private Mesh mesh;
    private int asciiNumber;

    private float x = 50;
    private float y = 50;
    private float width = 50;
    private float height = 50;

    public ASCII(int id, Texture texture, String fontfile) {
        this.asciiNumber = id;

        processFontFile(fontfile);

        /** Oklart */

        float f = 1.0f / texture.getWidth();

        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3(x * f, y * f, 0), new Vector2(x, y)), //01
                new Vertex(new Vector3((x + width) * f, y * f, 0), new Vector2(x + width, y)),  //00
                new Vertex(new Vector3((x + width) * f, (y + height) * f, 0), new Vector2(x + width, y + height)),   //10
                new Vertex(new Vector3(x * f, (y + height) * f, 0), new Vector2(x, y + height))}; //11

        int[] indices = new int[]{0, 1, 2,
                0, 2, 3};

        mesh = Mesh.customMesh("text", vertices, indices, false, false);
    }

    public void draw() {
        mesh.draw();
    }

    private void processFontFile(String fontFile) {
        BufferedReader reader = null;

        try {
            File file = new File(fontFile);
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("char")) {
                    String delims = "[ ]+";
                    String[] lineSplit = line.split(delims);

                    String[] idSplit = lineSplit[1].split("=");

                    if (Integer.parseInt(idSplit[1]) == asciiNumber) {
                        /** Calculate x y width height etc */

                        x = Float.parseFloat(lineSplit[2].split("=")[1]);
                        y = Float.parseFloat(lineSplit[3].split("=")[1]);
                        width = Float.parseFloat(lineSplit[4].split("=")[1]);
                        height = Float.parseFloat(lineSplit[5].split("=")[1]);

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getAsciiNumber() {
        return asciiNumber;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return Character.toString((char) asciiNumber);
    }
}
