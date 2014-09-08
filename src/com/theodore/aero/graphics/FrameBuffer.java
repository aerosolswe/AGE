package com.theodore.aero.graphics;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FrameBuffer {

    private int frameBuffer;
    private int renderBuffer;
    private int width, height;

    private int[] textures = new int[32];
    private int depthTexture;

    public FrameBuffer() {
        frameBuffer = glGenFramebuffers();
        renderBuffer = glGenRenderbuffers();
    }

    public FrameBuffer(int width, int height, Texture[] textures, int[] attachments) {
        this.width = width;
        this.height = height;

        if (attachments.length == 0)
            return;

        IntBuffer ib = BufferUtils.createIntBuffer(32);
        int[] drawBuffers = new int[32];

        boolean hasDepth = false;

        for (int i = 0; i < textures.length; i++) {
            if (attachments[i] == GL_DEPTH_ATTACHMENT) {
                drawBuffers[i] = GL_NONE;
                hasDepth = true;
            } else {
                drawBuffers[i] = attachments[i];
                ib.put(i, drawBuffers[i]);
            }

            if (attachments[i] == GL_NONE)
                continue;

            if (frameBuffer == 0) {
                frameBuffer = glGenFramebuffers();
                glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
            }

            glFramebufferTexture(GL_FRAMEBUFFER, attachments[i], textures[i].getID(), 0);
        }

        if (frameBuffer == 0)
            return;

        if (!hasDepth) {
            renderBuffer = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
        }

        glDrawBuffers(ib);

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

    public static void unbindRenderTarget() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, Window.getWidth(), Window.getHeight());
    }

    public static void debugFrameBuffer(int status) {
        switch (status) {
            case GL_FRAMEBUFFER_UNSUPPORTED:
                System.err.println("OpenGL framebuffer format not supported. ");
            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                System.err.println("OpenGL framebuffer missing attachment.");
            case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
                System.err.println("OpenGL framebuffer missing draw buffer.");
            case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
                System.err.println("OpenGL framebuffer missing read buffer.");
            case GL_FRAMEBUFFER_COMPLETE:
            default:
        }
    }

    public int getFrameBuffer() {
        return frameBuffer;
    }

    public void setFrameBuffer(int frameBuffer) {
        this.frameBuffer = frameBuffer;
    }

    public int getDepthTexture() {
        return depthTexture;
    }

    public void setDepthTexture(int depthTexture) {
        this.depthTexture = depthTexture;
    }

    public int getTexture(int i) {
        return textures[i];
    }

    public void setTextures(int[] textures) {
        this.textures = textures;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
