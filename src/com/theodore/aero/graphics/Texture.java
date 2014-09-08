package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Util;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class Texture {

    public static final String DIRECTORY = "textures/";

    //TODO: Make texture constants dynamic (aka hashmap territory)
    public static final Texture WHITE_PIXEL = new Texture(1, 1, (ByteBuffer) Util.createByteBuffer(4).put(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}).flip());
    public static final Texture NORMAL_UP = new Texture(1, 1, (ByteBuffer) Util.createByteBuffer(4).put(new byte[]{(byte) 0x80, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF}).flip());
    public static final int DIFFUSE_TEXTURE = 0;
    public static final int NORMAL_TEXTURE = 1;
    public static final int HEIGHT_TEXTURE = 2;
    public static final int SHADOW_MAP_TEXTURE = 4;
    public static final int FILTER_TEXTURE = 5;

    private static final HashMap<String, Texture> textures = new HashMap<String, Texture>();

    private static int lastWriteBind = 0;
    private int id;
    private int frameBuffer;
    private int renderBuffer;
    private int width;
    private int height;

    public static Texture get(String name, int linearFiltering, boolean clamp) {
        if (textures.containsKey(name))
            return textures.get(name);
        else {
            textures.put(name, new Texture(name, linearFiltering, clamp));
            return textures.get(name);
        }
    }

    public static Texture get(String name) {
        if (textures.containsKey(name))
            return textures.get(name);
        else {
            textures.put(name, new Texture(name, Aero.graphicsUtil.GL_LINEAR_MIPMAP_LINEAR(), false));
            return textures.get(name);
        }
    }

    public Texture(int width, int height, ByteBuffer data, int textureTarget, int filters, int internalFormat, int format, boolean clamp, int attachment) {
        this.width = width;
        this.height = height;

        this.id = Aero.graphicsUtil.createTexture(width, height, data, textureTarget, filters, internalFormat, format, clamp);

        if (attachment != 0) {
            initRenderTarget(attachment);
        }
    }

    public Texture(int width, int height, ByteBuffer data) {
        this(width, height, data, GL_LINEAR, false);
    }

    public Texture(int width, int height, ByteBuffer data, int filter, boolean clamp) {
        this.width = width;
        this.height = height;

        this.id = Aero.graphicsUtil.createTexture(width, height, data, GL_TEXTURE_2D, filter, GL_RGBA8, GL_RGBA, clamp);
    }

    private void initRenderTarget(int attachment) {
        int drawBuffers;

        boolean hasDepth = false;

        if (attachment == GL_DEPTH_ATTACHMENT) {
            drawBuffers = GL_NONE;
            hasDepth = true;
        } else {
            drawBuffers = attachment;
        }

        if (frameBuffer == 0) {
            frameBuffer = glGenFramebuffers();
            glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        }

        glFramebufferTexture(GL_FRAMEBUFFER, attachment, id, 0);

        if (frameBuffer == 0)
            return;

        if (!hasDepth) {
            renderBuffer = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
        }

        glDrawBuffers(drawBuffers);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            int status = glCheckFramebufferStatus(frameBuffer);
            System.err.println("Framebuffer creation has failed " + status);
            new Exception().printStackTrace();
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    private Texture(String fileName, int linearFiltering, boolean clamp) {
        try {
            BufferedImage image = ImageIO.read(new File(Aero.getResourcePath(DIRECTORY + fileName)));

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

            this.width = image.getWidth();
            this.height = image.getHeight();
            this.id = Aero.graphicsUtil.createTexture(width, height, buffer, GL_TEXTURE_2D, linearFiltering, GL_RGBA, GL_RGBA, clamp);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void bind(int unit) {
        Aero.graphicsUtil.bindTexture(id, unit);
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bindAsRenderTarget() {
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }

    public void bindForWriting() {
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }

    public void bindForReading() {
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }


    public int getID() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
