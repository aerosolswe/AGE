package com.theodore.aero.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public abstract class MouseButtonCallback extends GLFWMouseButtonCallback {

    @Override
    public void invoke(long window, int button, int action, int mods) {
        mouseCallback(window, button, action, mods);
    }

    public abstract void mouseCallback(long window, int button, int action, int mods);
}
