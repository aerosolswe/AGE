package com.theodore.aero.graphics.g3d;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class CubeMap {

    private int width;
    private int height;

    private int id;
    private int framebuffer;
    private int depth;

    public CubeMap(int width, int height) {
        this.width = width;
        this.height = height;

        id = 0;
        framebuffer = 0;
        depth = 0;

        init();
    }

    private void init() {
        framebuffer = glGenFramebuffers();

        ByteBuffer data = Util.createByteBuffer(width * height * 4);

        depth = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depth);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, data);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D, 0);

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        for (int i = 0; i < 6; i++) {
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_R32F, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, data);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depth, 0);

        glDrawBuffer(GL_NONE);

        glReadBuffer(GL_NONE);

        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FB error, status: 0x%x\n" + status);
            System.exit(1);
        }

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    public CubeMap(File front, File back, File top, File bottom, File left, File right) {
//        glActiveTexture(Texture.CUBE_TEXTURE);
        id = glGenTextures();

        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_POSITIVE_Z, front);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, back);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_POSITIVE_Y, top);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, bottom);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_NEGATIVE_X, left);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_POSITIVE_X, right);
    }

    public void loadCubeMapSide(int texture, int sideTarget, File side) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);

        try {
            BufferedImage image = ImageIO.read(side);

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

    public void bindAsRenderTarget() {
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        glViewport(0, 0, width, height);
    }

    public void bindForWriting(int face) {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, framebuffer);
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, face, id, 0);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
    }


    public void bindForReading(int textureUnit) {
        glActiveTexture(textureUnit);
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glViewport(0, 0, width, height);
    }

    public void bind(int unit) {
        glActiveTexture(unit);
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
    }

    public int getTexCube() {
        return id;
    }

}
