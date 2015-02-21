package com.theodore.aero.graphics;

import com.theodore.aero.core.Util;
import com.theodore.aero.math.MathUtils;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;

public class GraphicsUtil {

    public void init() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        setFrontFace(GL_CW);
        enableCullFace(GL_BACK);
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);

        setDepthTest(true);
        setDepthClamp(true);
        setTexture2D(true);
        glEnable(GL_MULTISAMPLE);

        enableBlending(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public void clearColorAndDeptAndStencil() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void clearColorAndDepth() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void clearColor() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void clearDepth() {
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public void setTexture2D(boolean value) {
        if (value)
            glEnable(GL_TEXTURE_2D);
        else
            glDisable(GL_TEXTURE_2D);
    }

    public void setDepthClamp(boolean value) {
        if (value)
            glEnable(GL_DEPTH_CLAMP);
        else
            glDisable(GL_DEPTH_CLAMP);
    }

    public void setFrontFace(int value) {
        glFrontFace(value);
    }

    public void enableCullFace(int value) {
        if (value == GL_NONE) {
            disableCullFace();
        } else {
            glEnable(GL_CULL_FACE);
            glCullFace(value);
        }
    }

    public void disableCullFace() {
        glDisable(GL_CULL_FACE);
    }

    public void setDepthTest(boolean value) {
        if (value)
            glEnable(GL_DEPTH_TEST);
        else
            glDisable(GL_DEPTH_TEST);
    }

    public void setDepthFunc(int value) {
        glDepthFunc(value);
    }

    public void setDepthMask(boolean value) {
        glDepthMask(value);
    }

    public void enableBlending(int value, int value0) {
        glEnable(GL_BLEND);
        glBlendFunc(value, value0);
    }

    public void disableBlending() {
        glDisable(GL_BLEND);
    }

}
