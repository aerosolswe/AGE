package com.theodore.aero.graphics.g2d;

import com.theodore.aero.components.Camera;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.Font;
import com.theodore.aero.graphics.Texture;
import com.theodore.aero.graphics.Vertex;
import com.theodore.aero.graphics.mesh.Mesh;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.graphics.shaders.Shader;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;
import org.lwjgl.opengl.GL11;

public class BitmapFont {

    private Shader shader;
    private Transform[] transform;
    private Mesh[] meshes;
    private char[] characters = new char[99999];

    private float x;
    private float y;
    private float z;
    private float width;
    private float height;

    private Font font;

    private String text;

    private float xOffset;

    public BitmapFont(String text) {
        this(text, new Font(Texture.get("fontsheet.png")), 0, 0, 0, 16, 16);
    }

    public BitmapFont(String text, Font font, float x, float y, float z, float width, float height) {
        this.shader = BasicShader.getInstance();
        this.font = font;

        this.text = text;
        this.x = x;
        this.y = y;
        this.z = z;

        this.width = width;
        this.height = height;

        this.xOffset = 10;

        meshes = new Mesh[256];
        transform = new Transform[256];
        for (int i = 0; i < meshes.length; i++) {
            Vector2[] uv = calculateUVMapping(i, 16, 16);

            Vertex[] vertices = new Vertex[]{
                    new Vertex(new Vector3(-0.5f, -0.5f, 0), uv[1]), //01
                    new Vertex(new Vector3(-0.5f, 0.5f, 0), uv[0]),  //00
                    new Vertex(new Vector3(0.5f, 0.5f, 0), uv[2]),   //10
                    new Vertex(new Vector3(0.5f, -0.5f, 0), uv[3])}; //11

            int[] indices = new int[]{0, 1, 2,
                    0, 2, 3};

            meshes[i] = Mesh.customMesh("text", vertices, indices, false, false);

            transform[i] = new Transform();
            transform[i].setPosition(new Vector3(x, y + height / 2, z));
            transform[i].setScale(new Vector3(width, height, height));
        }
    }

    public void draw(Camera camera) {
        Camera mainCamera = Aero.graphics.getMainCamera();
        Camera cam = camera;

        Aero.graphics.setMainCamera(cam);

        Aero.graphicsUtil.enableBlending(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Aero.graphicsUtil.disableCullFace();
        Aero.graphicsUtil.setDepthTest(false);

        shader.bind();

        characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int ascii = text.charAt(i);


            shader.updateUniforms(transform[ascii], font.getMaterial(), Aero.graphics);
            transform[ascii].setPosition(new Vector3((x + (i * xOffset)), y + height / 2, z));
            transform[ascii].setScale(new Vector3(width, height, width));
            meshes[ascii].draw();
        }


        Aero.graphicsUtil.setDepthTest(true);
        Aero.graphicsUtil.enableCullFace(GL11.GL_BACK);

        Aero.graphics.setMainCamera(mainCamera);
    }

    public Vector2[] calculateUVMapping(int index, int atlasWidth, int atlasHeight) {
        int u = index % atlasWidth;
        int v = index / atlasHeight;

        float xOffset = 1f / atlasWidth;
        float yOffset = 1f / atlasHeight;

        float uOffset = (u * xOffset);
        float vOffset = (v * yOffset);

        Vector2[] UVList = new Vector2[4];

        UVList[0] = new Vector2(uOffset, vOffset); // 0,0
        UVList[1] = new Vector2(uOffset, vOffset + yOffset); // 0,1
        UVList[2] = new Vector2(uOffset + xOffset, vOffset); // 1,0
        UVList[3] = new Vector2(uOffset + xOffset, vOffset + yOffset); // 1,1

        return UVList;
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
