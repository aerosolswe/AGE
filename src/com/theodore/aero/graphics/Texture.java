package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Util;
import com.theodore.aero.resources.TextureResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.*;

public class Texture {

    public static final Texture WHITE_PIXEL = new Texture(1, 1, (ByteBuffer) Util.createByteBuffer(4).put(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}).flip(), GL_TEXTURE_2D, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA8, GL_RGBA, false, 0);
    public static final Texture BLACK_PIXEL = new Texture(1, 1, (ByteBuffer) Util.createByteBuffer(4).put(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF}).flip(), GL_TEXTURE_2D, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA8, GL_RGBA, false, 0);
    public static final Texture NORMAL_UP = new Texture(1, 1, (ByteBuffer) Util.createByteBuffer(4).put(new byte[]{(byte) 0x80, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF}).flip(), GL_TEXTURE_2D, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA8, GL_RGBA, false, 0);

    private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
    private TextureResource resource;
    private String name;

    private int id;

    public Texture(int width, int height, ByteBuffer data, int sideTarget, int textureTarget, int filters, int internalFormat, int format, boolean clamp, int attachment) {
        this.name = "";

        id = generateId();

        resource = new TextureResource(id, width, height, data, sideTarget, textureTarget, filters, internalFormat, format, clamp, attachment);
    }

    public Texture(File[] files) {
        this.name = "";

        id = generateId();

        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_POSITIVE_Z, files[0]);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, files[1]);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_POSITIVE_Y, files[2]);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, files[3]);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_NEGATIVE_X, files[4]);
        loadCubeMapSide(id, GL_TEXTURE_CUBE_MAP_POSITIVE_X, files[5]);
    }

    public Texture(String name) {
        this.name = name;

        id = generateId();

        TextureResource oldResource = loadedTextures.get(name);

        if (oldResource != null) {
            resource = oldResource;
            resource.addReference();
        } else {
            System.err.println("Couldnt find the texture with specified name '" + name + "', using default texture!");
            resource = loadedTextures.get("default");
        }
    }

    public Texture(String name, File file, int linearFiltering, boolean clamp) {
        this.name = name;

        id = generateId();

        TextureResource oldResource = loadedTextures.get(name);

        if (oldResource != null) {
            resource = oldResource;
            resource.addReference();
        } else {
            resource = loadTexture(file, id, GL_TEXTURE_2D, GL_TEXTURE_2D, linearFiltering, clamp);
            loadedTextures.put(name, resource);
        }
    }

    public Texture(String name, File file) {
        this(name, file, GL_LINEAR_MIPMAP_LINEAR, false);
    }

    private static TextureResource loadTexture(File file, int id, int sideTarget, int renderTarget, int linearFiltering, boolean clamp) {
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

            int width = image.getWidth();
            int height = image.getHeight();

            return new TextureResource(id, width, height, buffer, sideTarget, renderTarget, linearFiltering, GL_RGBA, GL_RGBA, clamp, 0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    private static int generateId() {
        return glGenTextures();
    }

    public static void generateBaseTextures() {
        loadedTextures.put("default", loadTexture(Aero.files.internal("default/textures/default.png"), generateId(), GL_TEXTURE_2D, GL_TEXTURE_2D, GL_LINEAR_MIPMAP_LINEAR, false));
        loadedTextures.put("fontsheet", loadTexture(Aero.files.internal("default/textures/fontsheet.png"), generateId(), GL_TEXTURE_2D, GL_TEXTURE_2D, GL_LINEAR_MIPMAP_LINEAR, false));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (resource.removeReference() && !name.isEmpty()) {
            loadedTextures.remove(name);
        }
    }

    public void bind() {
        bind(0);
    }

    public void bind(int samplerSlot) {
        assert (samplerSlot >= 0 && samplerSlot <= 31);
        glActiveTexture(GL_TEXTURE0 + samplerSlot);
        if (resource != null)
            glBindTexture(GL_TEXTURE_2D, resource.getId());
        else
            glBindTexture(GL_TEXTURE_2D, id);

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
        resource.bindAsRenderTarget();
    }

    public int getID() {
        return resource.getId();
    }

    public int getWidth() {
        return resource.getWidth();
    }

    public int getHeight() {
        return resource.getHeight();
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
