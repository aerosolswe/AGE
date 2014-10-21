package com.theodore.aero.resourceManagement;

import com.theodore.aero.math.MathUtils;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class TextureResource {

    private int refCount;

    private int id;
    private int frameBuffer;
    private int renderBuffer;
    private int width;
    private int height;

    public TextureResource(int id, int width, int height, ByteBuffer data, int sideTarget, int textureTarget, int filters, int internalFormat, int format, boolean clamp, int attachment) {
        this.id = id;
        createTexture(width, height, data, sideTarget, textureTarget, filters, internalFormat, format, clamp);
        this.refCount = 1;
        this.width = width;
        this.height = height;

        if (attachment != 0) {
            initRenderTarget(attachment);
        }
    }

    public void createTexture(int width, int height, ByteBuffer data, int sideTarget, int textureTarget, int filters, int internalFormat, int format, boolean clamp) {
        glBindTexture(textureTarget, id);

        if (filters == GL_NEAREST_MIPMAP_LINEAR ||
                filters == GL_NEAREST_MIPMAP_NEAREST ||
                filters == GL_LINEAR_MIPMAP_LINEAR ||
                filters == GL_LINEAR_MIPMAP_NEAREST) {
            GL30.glGenerateMipmap(id);
            glTexParameteri(textureTarget, GL_GENERATE_MIPMAP, GL_TRUE);

            float maxAnisotropy = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);

            glTexParameterf(textureTarget, GL_TEXTURE_MAX_ANISOTROPY_EXT, MathUtils.clamp(0.0f, 16.0f, maxAnisotropy));
        } else {
            glTexParameteri(textureTarget, GL_TEXTURE_BASE_LEVEL, 0);
            glTexParameteri(textureTarget, GL_TEXTURE_MAX_LEVEL, 0);
        }

        if (clamp) {
            glTexParameteri(textureTarget, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
            glTexParameteri(textureTarget, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(textureTarget, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        glTexParameterf(textureTarget, GL_TEXTURE_MIN_FILTER, filters);
        glTexParameterf(textureTarget, GL_TEXTURE_MAG_FILTER, filters);

        glTexImage2D(sideTarget, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
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

    public void bindAsRenderTarget() {
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }

    @Override
    protected void finalize() {
        glDeleteBuffers(id);
    }

    public void addReference() {
        refCount++;
    }

    public boolean removeReference() {
        refCount--;
        return refCount == 0;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
