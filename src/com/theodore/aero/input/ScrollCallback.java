package com.theodore.aero.input;

import org.lwjgl.glfw.GLFWScrollCallback;

public abstract class ScrollCallback extends GLFWScrollCallback {

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        scrollCallback(window, xoffset, yoffset);
    }

    public abstract void scrollCallback(long window, double xoffset, double yoffset);
}
