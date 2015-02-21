package com.theodore.aero.input;

import org.lwjgl.glfw.GLFWCharCallback;

public abstract class CharCallback extends GLFWCharCallback {

    @Override
    public void invoke(long window, int codepoint) {
        charCallback(window, codepoint);
    }

    public abstract void charCallback(long window, int codepoint);

}
