package com.theodore.aero.graphics.g2d.gui;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.shaders.BasicShader;
import com.theodore.aero.input.Input;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

public class TextField extends Widget {

    private Shader shader;
    private Mesh[] meshes;
    private Transform[] transform;
    private char[] characters = new char[99999];

    private Font font;

    private StringBuilder text;

    private float xOffset;

    private boolean isFocused;

    public TextField(String text) {
        this(text, new Font(Texture.get("fontsheet.png")), 0, 0, 16, 16);
    }

    public TextField(String text, float x, float y) {
        this(text, new Font(Texture.get("fontsheet.png")), x, y, 16, 16);
    }

    public TextField(String text, float x, float y, float width, float height) {
        this(text, new Font(Texture.get("fontsheet.png")), x, y, width, height);
    }

    public TextField(String text, Font font) {
        this(text, font, 0, 0, 16, 16);
    }

    public TextField(String text, Font font, float x, float y) {
        this(text, font, x, y, 16, 16);
    }

    public TextField(String text, Font font, float x, float y, float width, float height) {
        this.shader = BasicShader.getInstance();
        this.font = font;

        this.text = new StringBuilder(text);
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.xOffset = 10;

        this.isFocused = false;

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
            transform[i].setPosition(new Vector3(x, y + height / 2, 0));
            transform[i].setScale(new Vector3(width, height, height));
        }
    }

    @Override
    public void update(float delta) {
        isFocused();

        if (isFocused) {
            if (Aero.input.getAnyKeyDown()) {
                if (Aero.input.getKeyDown(Input.KEY_ESCAPE)) {
                    isFocused = false;
                } else if (Aero.input.getKeyDown(Input.KEY_RETURN)) {
                    isFocused = false;
                } else if (Aero.input.getKeyDown(Input.KEY_SPACE)) {
                    text.append(' ');
                } else if (Aero.input.getKeyDown(Input.KEY_BACK)) {
                    if (text.toString().toCharArray().length > 0) {
                        text.deleteCharAt(text.toString().toCharArray().length - 1);
                    }
                } else {
                    text.append(Aero.input.getKeyCharacter());
                }
            }


        }

    }

    @Override
    public void draw() {
        shader.bind();
        characters = text.toString().toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int ascii = text.charAt(i);
            shader.updateUniforms(transform[ascii].calcModel(), transform[ascii].calcOrthographicMVP(), font.getMaterial());
            transform[ascii].setPosition(new Vector3((x + (i * xOffset)), y + height / 2, 0));
            transform[ascii].setScale(new Vector3(width, height, width));
            meshes[ascii].draw();
        }

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

    private void isFocused() {
        if (Aero.input.getMouseDown(0)) {
            if (checkIntersection(Aero.input.getMousePosition())) {
                isFocused = true;
            } else if (!checkIntersection(Aero.input.getMousePosition())) {
                isFocused = false;
            }
        }

        if (isFocused) {
            if (Aero.input.getKeyDown(Input.KEY_ESCAPE) || Aero.input.getKeyDown(Input.KEY_RETURN))
                isFocused = false;
        }
    }

    public boolean isClicked() {
        return isClicked(new Vector3(0.8f, 0.1f, 0.3f), new Vector3(0.1f, 0.2f, 0.3f), new Vector3(1, 1, 1));
    }

    public boolean isClicked(Vector3 pressedColor, Vector3 clickColor, Vector3 originalColor) {
        if (checkIntersection(Aero.input.getMousePosition())) {

            if (Aero.input.getMouse(0)) {
                setColor(pressedColor);
            } else if (Aero.input.getMouseUp(0))
                return true;
            else
                setColor(clickColor);

        } else if (!checkIntersection(Aero.input.getMousePosition())) {
            setColor(originalColor);
        }

        return false;
    }

    @Override
    public boolean checkIntersection(Vector2 position) {
        return position.getX() > x &&
                position.getX() < (x + width + characters.length * xOffset) - width &&
                position.getY() > y &&
                position.getY() < y + height;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(width, height);
    }

    public Vector2 getTotalSize() {
        return new Vector2(x + width + characters.length * xOffset, height);
    }

    public float getxOffset() {
        return xOffset;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public String getText() {
        return text.toString();
    }

    public void setColor(Vector3 color) {
        font.getMaterial().setColor(color);
    }

    public Vector3 getColor() {
        return font.getMaterial().getColor();
    }

    public void setAlpha(float alpha) {
        font.getMaterial().setAlpha(alpha);
    }

    public float getAlpha() {
        return font.getMaterial().getAlpha();
    }

    public char[] getCharacters() {
        return characters;
    }
}
