package com.theodore.aero.input;

import org.lwjgl.glfw.GLFWKeyCallback;

public abstract class KeyCallback extends GLFWKeyCallback {

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keyCallback(window, key, scancode, action, mods);
    }

    public abstract void keyCallback(long window, int key, int scancode, int action, int mods);

}
