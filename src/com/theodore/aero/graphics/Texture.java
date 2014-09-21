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
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class Texture {

    //TODO: Make texture constants dynamic (aka hashmap territory)
    public static final Texture WHITE_PIXEL = new Texture(1, 1, (ByteBuffer) Util.createByteBuffer(4).put(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}).flip());
    public static final Texture NORMAL_UP = new Texture(1, 1, (ByteBuffer) Util.createByteBuffer(4).put(new byte[]{(byte) 0x80, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF}).flip());
    public static final int DIFFUSE_TEXTURE = 0;
    public static final int NORMAL_TEXTURE = 1;
    public static final int HEIGHT_TEXTURE = 2;
    public static final int SHADOW_MAP_TEXTURE = 4;
    public static final int CUBE_TEXTURE = 5;

    private static final HashMap<String, Texture> textures = new HashMap<String, Texture>();

    private static int lastWriteBind = 0;
    private int id;
    private int frameBuffer;
    private int renderBuffer;
    private int width;
    private int height;


    public static final int GBUFFER_TEXTURE_TYPE_POSITION = 0;
    public static final int GBUFFER_TEXTURE_TYPE_DIFFUSE = 1;
    public static final int GBUFFER_TEXTURE_TYPE_NORMAL = 2;

    private int depthTexture;
    private int finalTexture;
    private int[] textureIds;

    public static void generateBaseTextures() {
        textures.put("default", new Texture(Aero.files.internal("default/textures/default.png"), GL_LINEAR_MIPMAP_LINEAR, false));
        textures.put("fontsheet", new Texture(Aero.files.internal("default/textures/fontsheet.png"), GL_LINEAR_MIPMAP_LINEAR, false));
    }

    public static Texture get(String name) {
        if (textures.containsKey(name)){
            return textures.get(name);
        }else {
            return textures.get("default");
        }
    }

    public static void load(String name, File file) {
        textures.put(name, new Texture(file, Aero.graphicsUtil.GL_LINEAR_MIPMAP_LINEAR(), false));
    }

    public static void load(String name, File file, int linearFiltering, boolean clamp) {
        textures.put(name, new Texture(file, linearFiltering, clamp));
    }

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;

        frameBuffer = 0;
        depthTexture = 0;
        textureIds = new int[3];

        initGBuffer();
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

    private void initGBuffer() {
        // Create the FBO
        frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);

        // Create the gbuffer textures
        for (int i = 0; i < textureIds.length; i++) {
            textureIds[i] = glGenTextures();
        }
        depthTexture = glGenTextures();
        finalTexture = glGenTextures();

        ByteBuffer data = Util.createByteBuffer(width * height * 4);


        for (int i = 0; i < textureIds.length; i++) {
            glBindTexture(GL_TEXTURE_2D, textureIds[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, textureIds[i], 0);
        }

        // depth
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH32F_STENCIL8, width, height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, data);
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);

        // final
        glBindTexture(GL_TEXTURE_2D, finalTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT4, GL_TEXTURE_2D, finalTexture, 0);

        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FB error, status: 0x%x\n" + status);
            System.exit(1);
        }

        // restore default FBO
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
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

    private Texture(File file, int linearFiltering, boolean clamp) {
        try {
            BufferedImage image = ImageIO.read(file);

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
