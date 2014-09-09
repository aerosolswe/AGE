package com.theodore.aero.graphics.g3d;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class CubeMap {

    public static final String DIRECTORY = "cubemaps/";

    private int texCube;

    public CubeMap(String front, String back, String top, String bottom, String left, String right) {
        glActiveTexture(Texture.CUBE_TEXTURE);
        texCube = glGenTextures();

        loadCubeMapSide(texCube, GL_TEXTURE_CUBE_MAP_POSITIVE_Z, front);
        loadCubeMapSide(texCube, GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, back);
        loadCubeMapSide(texCube, GL_TEXTURE_CUBE_MAP_POSITIVE_Y, top);
        loadCubeMapSide(texCube, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, bottom);
        loadCubeMapSide(texCube, GL_TEXTURE_CUBE_MAP_NEGATIVE_X, left);
        loadCubeMapSide(texCube, GL_TEXTURE_CUBE_MAP_POSITIVE_X, right);
    }

    public void loadCubeMapSide(int texture, int sideTarget, String side) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);

        try {
            BufferedImage image = ImageIO.read(new File(Aero.getResourcePath(DIRECTORY + side)));

            boolean hasAlpha = image.getColorModel().hasAlpha();

            int[] pixels = image.getRGB(0, 0, image.getWidth(),
                    image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = Util.createByteBuffer(image.getWidth() * image.getHeight() * 4);

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) ((pixel >> 0) & 0xFF));
                    if (hasAlpha)
                        buffer.put((byte) ((pixel >> 24) & 0xFF));
                    else
                        buffer.put((byte) (0xFF));
                }
            }

            buffer.flip();

            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

            glTexImage2D(sideTarget, 0, GL_RGB, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void bind(int unit) {
        glActiveTexture(unit);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texCube);
    }

    public int getTexCube() {
        return texCube;
    }

}
