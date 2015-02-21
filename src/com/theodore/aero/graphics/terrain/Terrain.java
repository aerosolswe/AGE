package com.theodore.aero.graphics.terrain;

import com.theodore.aero.components.GameComponent;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.mesh.meshLoading.IndexedModel;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.MathUtils;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain extends GameComponent {

    public static final float SIZE = 800;

    public static final float MAX_HEIGHT = 40;
    public static final float MAX_PIXEL_COLOR = 255 * 255 * 255;

    private Mesh mesh;
    private Material material;

    private float[][] heights;

    public Terrain(Material material) {
        this.material = material;
    }

    public Terrain flat() {
        mesh = new Mesh("terrain", generateTerrain());

        return this;
    }

    public Terrain heightMap(File heightMap) {
        mesh = new Mesh("terrain", generateTerrain(heightMap));

        return this;
    }

    public Terrain random() {
        mesh = new Mesh("terrain", generateRandomTerrain());

        return this;
    }

    @Override
    public void renderBasic(Shader shader, Graphics graphics) {
        super.renderBasic(shader, graphics);

        shader.bind();
        material.getTexture("blendMap").bind(graphics.getSamplerSlot("blendMap"));
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        material.getTexture("rdiffuse").bind(graphics.getSamplerSlot("rdiffuse"));
        material.getTexture("gdiffuse").bind(graphics.getSamplerSlot("gdiffuse"));
        material.getTexture("bdiffuse").bind(graphics.getSamplerSlot("bdiffuse"));
        shader.updateUniforms(getTransform(), material, graphics);
        mesh.draw(GL11.GL_TRIANGLES);
        shader.unbind();
    }

    @Override
    public void renderShadow(Shader shader, Graphics graphics) {
        super.renderBasic(shader, graphics);

        shader.bind();
        shader.updateUniforms(getTransform(), null, graphics);
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        material.getTexture("rdiffuse").bind(graphics.getSamplerSlot("rdiffuse"));
        material.getTexture("gdiffuse").bind(graphics.getSamplerSlot("gdiffuse"));
        material.getTexture("bdiffuse").bind(graphics.getSamplerSlot("bdiffuse"));
        mesh.draw(GL11.GL_TRIANGLES);
        shader.unbind();
    }

    @Override
    public void renderLight(Shader shader, Graphics graphics) {
        super.renderBasic(shader, graphics);

        shader.bind();
        shader.updateUniforms(getTransform(), material, graphics);
        material.getTexture("blendMap").bind(graphics.getSamplerSlot("blendMap"));
        material.getTexture("diffuse").bind(graphics.getSamplerSlot("diffuse"));
        material.getTexture("normalMap").bind(graphics.getSamplerSlot("normalMap"));
        material.getTexture("specularMap").bind(graphics.getSamplerSlot("specularMap"));
        material.getTexture("dispMap").bind(graphics.getSamplerSlot("dispMap"));

        material.getTexture("rdiffuse").bind(graphics.getSamplerSlot("rdiffuse"));
        material.getTexture("rnormalMap").bind(graphics.getSamplerSlot("rnormalMap"));
        material.getTexture("rspecularMap").bind(graphics.getSamplerSlot("rspecularMap"));
        material.getTexture("rdispMap").bind(graphics.getSamplerSlot("rdispMap"));

        material.getTexture("gdiffuse").bind(graphics.getSamplerSlot("gdiffuse"));
        material.getTexture("gnormalMap").bind(graphics.getSamplerSlot("gnormalMap"));
        material.getTexture("gspecularMap").bind(graphics.getSamplerSlot("gspecularMap"));
        material.getTexture("gdispMap").bind(graphics.getSamplerSlot("gdispMap"));

        material.getTexture("bdiffuse").bind(graphics.getSamplerSlot("bdiffuse"));
        material.getTexture("bnormalMap").bind(graphics.getSamplerSlot("bnormalMap"));
        material.getTexture("bspecularMap").bind(graphics.getSamplerSlot("bspecularMap"));
        material.getTexture("bdispMap").bind(graphics.getSamplerSlot("bdispMap"));
        mesh.draw(GL11.GL_TRIANGLES);
        shader.unbind();
    }

    public float getHeightOfPosition(float x, float z) {
        float terrainX = x - getTransform().getPosition().x;
        float terrainZ = z - getTransform().getPosition().z;

        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        float result = 0;

        if (xCoord <= (1 - zCoord)) {
            result = MathUtils.barryCentric(new Vector3(0, heights[gridX][gridZ], 0), new Vector3(1,
                    heights[gridX + 1][gridZ], 0), new Vector3(0,
                    heights[gridX][gridZ + 1], 1), new Vector2(xCoord, zCoord));
        } else {
            result = MathUtils.barryCentric(new Vector3(1, heights[gridX + 1][gridZ], 0), new Vector3(1,
                    heights[gridX + 1][gridZ + 1], 1), new Vector3(0,
                    heights[gridX][gridZ + 1], 1), new Vector2(xCoord, zCoord));
        }

        return result + getTransform().getPosition().y;
    }

    private IndexedModel generateTerrain(File heightMap) {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(heightMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT = bufferedImage.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        IndexedModel indexedModel = new IndexedModel();

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                float height = getHeight(j, i, bufferedImage);
                heights[j][i] = height;
                indexedModel.positions.add(new Vector3((float) j / ((float) VERTEX_COUNT - 1) * SIZE, height, (float) i / ((float) VERTEX_COUNT - 1) * SIZE));
                indexedModel.normals.add(calcNormal(j, i, bufferedImage));
                indexedModel.texCoords.add(new Vector2((float) j / ((float) VERTEX_COUNT - 1), (float) i / ((float) VERTEX_COUNT - 1)));
            }
        }

        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;

                indexedModel.indices.add(topLeft);
                indexedModel.indices.add(bottomLeft);
                indexedModel.indices.add(topRight);
                indexedModel.indices.add(topRight);
                indexedModel.indices.add(bottomLeft);
                indexedModel.indices.add(bottomRight);
            }
        }

        return indexedModel.finish();
    }

    private IndexedModel generateTerrain() {
        IndexedModel indexedModel = new IndexedModel();

        int VERTEX_COUNT = 128;
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                indexedModel.positions.add(new Vector3((float) j / ((float) VERTEX_COUNT - 1) * SIZE, 0, (float) i / ((float) VERTEX_COUNT - 1) * SIZE));
                indexedModel.normals.add(new Vector3(0, 1, 0));
                indexedModel.texCoords.add(new Vector2((float) j / ((float) VERTEX_COUNT - 1), (float) i / ((float) VERTEX_COUNT - 1)));
            }
        }

        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;

                indexedModel.indices.add(topLeft);
                indexedModel.indices.add(bottomLeft);
                indexedModel.indices.add(topRight);
                indexedModel.indices.add(topRight);
                indexedModel.indices.add(bottomLeft);
                indexedModel.indices.add(bottomRight);
            }
        }

        return indexedModel.finish();
    }

    private IndexedModel generateRandomTerrain() {
        IndexedModel indexedModel = new IndexedModel();

        int VERTEX_COUNT = 128;
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                float height = MathUtils.random(0, 5);
                heights[j][i] = height;
                indexedModel.positions.add(new Vector3((float) j / ((float) VERTEX_COUNT - 1) * SIZE, height, (float) i / ((float) VERTEX_COUNT - 1) * SIZE));
                indexedModel.normals.add(new Vector3(0, 1, 0));
                indexedModel.texCoords.add(new Vector2((float) j / ((float) VERTEX_COUNT - 1), (float) i / ((float) VERTEX_COUNT - 1)));
            }
        }

        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;

                indexedModel.indices.add(topLeft);
                indexedModel.indices.add(bottomLeft);
                indexedModel.indices.add(topRight);
                indexedModel.indices.add(topRight);
                indexedModel.indices.add(bottomLeft);
                indexedModel.indices.add(bottomRight);
            }
        }

        return indexedModel.finish();
    }

    private float getHeight(int x, int y, BufferedImage bufferedImage) {
        if (x < 0 || x >= bufferedImage.getWidth() || y < 0 || y >= bufferedImage.getHeight()) {
            return 0;
        }

        float height = bufferedImage.getRGB(x, y);
        height += MAX_PIXEL_COLOR / 2;
        height /= MAX_PIXEL_COLOR / 2;

        height *= MAX_HEIGHT;

        return height;
    }

    private Vector3 calcNormal(int x, int y, BufferedImage bufferedImage) {
        float heightL = getHeight(x - 1, y, bufferedImage);
        float heightR = getHeight(x + 1, y, bufferedImage);
        float heightD = getHeight(x, y - 1, bufferedImage);
        float heightU = getHeight(x, y + 1, bufferedImage);

        return new Vector3(heightL - heightR, 2, heightD - heightU).normalized();
    }

}
