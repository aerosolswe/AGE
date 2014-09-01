package com.theodore.aero.graphics;

import com.theodore.aero.core.Util;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
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

    public void createFrameBuffer(Texture texture, int attachment) {
        width = texture.getWidth();
        height = texture.getHeight();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glFramebufferTexture(GL_FRAMEBUFFER, attachment, texture.getID(), 0);

        if (attachment == GL_DEPTH_ATTACHMENT)
            glDrawBuffer(GL_NONE);
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Framebuffer creation has failed");
            new Exception().printStackTrace();
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void createFrameBuffer(Texture texture, Texture depthTexture) {
        width = texture.getWidth();
        height = texture.getHeight();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.getID(), 0);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTexture.getID(), 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            int status = glCheckFramebufferStatus(frameBuffer);
            System.err.println("Framebuffer creation has failed " + status);
            new Exception().printStackTrace();
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void createFrameBuffer(Texture texture, Texture depthTexture, int attachment) {
        width = texture.getWidth();
        height = texture.getHeight();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glFramebufferTexture(GL_FRAMEBUFFER, attachment, texture.getID(), 0);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTexture.getID(), 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            int status = glCheckFramebufferStatus(frameBuffer);
            System.err.println("Framebuffer creation has failed " + status);
            new Exception().printStackTrace();
            System.exit(1);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void createFrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;

        ByteBuffer buffer = Util.createByteBuffer(Window.getWidth() * Window.getHeight() * 4);

        textures[0] = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textures[0]);
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB16F,
                width,
                height,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                buffer
        );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);


        ByteBuffer b = Util.createByteBuffer(Window.getWidth() * Window.getHeight() * 4);

        textures[1] = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textures[1]);
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB16F,
                width,
                height,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                b
        );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textures[0], 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, textures[1], 0);

        int rb = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rb);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rb);

        IntBuffer ib = BufferUtils.createIntBuffer(2);
        ib.put(0, GL_COLOR_ATTACHMENT0);
        ib.put(1, GL_COLOR_ATTACHMENT1);

        GL20.glDrawBuffers(ib);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            int status = glCheckFramebufferStatus(frameBuffer);
            System.err.println("Framebuffer creation has failed " + status);
            new Exception().printStackTrace();
            System.exit(1);
        }

        FrameBuffer.unbindRenderTarget();
    }

    public void createGBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);

        IntBuffer intBuffer = BufferUtils.createIntBuffer(textures.length);
        intBuffer.put(textures);

        glGenTextures(intBuffer);

        depthTexture = glGenTextures();

        ByteBuffer d = Util.createByteBuffer(width * height * 4);

        for (int i = 0; i < textures.length; i++) {
            glBindTexture(GL_TEXTURE_2D, textures[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB8, GL_FLOAT, d);
            glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, textures[i], 0);
        }

        FloatBuffer data = Util.createFloatBuffer(Window.getWidth() * Window.getHeight());

        for (int j = 0; j < Window.getWidth() * Window.getHeight(); j++)
            data.put(1);
        data.flip();

        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, data);
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);

        IntBuffer ib = BufferUtils.createIntBuffer(4);
        ib.put(GL_COLOR_ATTACHMENT0);
        ib.put(GL_COLOR_ATTACHMENT1);
        ib.put(GL_COLOR_ATTACHMENT2);
        ib.put(GL_COLOR_ATTACHMENT3);

        GL20.glDrawBuffers(ib);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            int status = glCheckFramebufferStatus(frameBuffer);
            System.err.println("Framebuffer creation has failed " + status);
            new Exception().printStackTrace();
            System.exit(1);
        }


        int status = glCheckFramebufferStatus(frameBuffer);
        debugFrameBuffer(status);

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
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
