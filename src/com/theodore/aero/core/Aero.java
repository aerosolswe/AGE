package com.theodore.aero.core;

import com.theodore.aero.graphics.*;
import com.theodore.aero.input.InputManager;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Aero {

    public static Window window;
    public static Graphics graphics;
    public static Input input;
    public static InputManager inputManager;
    public static GraphicsUtil graphicsUtil;
    public static Files files;
    public static Screen activeScreen;

    private boolean running = false;
    private double frameTime;
    private int frameRate;

    public static double renderTime = 0;
    public static double syncTime = 0;
    public static double inputTime = 0;
    public static double updateTime = 0;
    public static double totalTime = 0;

    //

    /** Instead of manually changing natives -Djava.library.path=native/os/x64-86 */
    static {
        System.setProperty("org.lwjgl.librarypath", (new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName() + File.separator + "x" + System.getProperty("sun.arch.data.model"))).getAbsolutePath());
    }


    public Aero(String title, int width, int height, int frameRate, boolean fullscreen, boolean vsync, int samples) {
        window = new Window(title, width, height, fullscreen, vsync, samples);
        this.frameRate = frameRate;

        if (vsync) {
            ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            int hz = GLFWvidmode.refreshRate(vidmode);
            frameTime = 1.0f / hz;
            this.frameRate = hz;
        } else {
            frameTime = 1.0f / frameRate;
        }

        init();
    }

    private void init() {

        Aero.graphicsUtil = new GraphicsUtil();
        Aero.graphics = new Graphics();
        Aero.input = new Input();
        Aero.inputManager = new InputManager();
        Aero.files = new Files();

        graphics.initGraphics();
        graphics.init();


    }

    public void start(Screen game) {
        if (running)
            return;

        Aero.activeScreen = game;
        Aero.activeScreen.setEngine();
        Aero.setActiveScreen(activeScreen);

        run();
    }

    public void stop() {
        if (running)
            running = false;
    }

    public void run() {
        running = true;
        window.makeContextCurrent();
        window.createContextFromCurrent();

        double lastTime = glfwGetTime();
        double frameCounter = 0;
        double unprocessedTime = 0;
        int frames = 0;

        while (running) {
            boolean render = false;

            double startTime = glfwGetTime();
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime;
            frameCounter += passedTime;

            if (frameCounter >= 1.0) {
                totalTime = (1000.0 * frameCounter) / (double) frames;

                renderTime = graphics.getRenderTimeAndReset(frames);
                syncTime = graphics.getSyncTimeAndReset(frames);
                inputTime = activeScreen.getInputTimeAndReset(frames);
                updateTime = activeScreen.getUpdateTimeAndReset(frames);

                graphics.setInteger("fps", frames);
                frames = 0;
                frameCounter = 0;
            }

            while (unprocessedTime > frameTime) {
                if (window.isClosed())
                    stop();

                activeScreen.input((float) frameTime);
                activeScreen.update((float) frameTime);
                inputManager.update();
                graphics.setFloat("delta", (float) frameTime);

                render = true;

                unprocessedTime -= frameTime;
            }

            if (render) {
                activeScreen.render(graphics);
                window.render();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        dispose();
    }

    private void dispose() {
        activeScreen.dispose();
        inputManager.dispose();
        window.destroy();
    }

    public static Screen getActiveScreen() {
        return activeScreen;
    }

    public static void setActiveScreen(Screen activeScreen) {
        Aero.activeScreen = activeScreen;
        Aero.activeScreen.init();
    }

}
