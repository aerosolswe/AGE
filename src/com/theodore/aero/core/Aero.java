package com.theodore.aero.core;

import com.theodore.aero.graphics.*;
import com.theodore.aero.math.Time;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;

import java.io.File;
import java.util.ArrayList;

public class Aero {

    public static Graphics graphics;
    public static Input input;
    public static GraphicsUtil graphicsUtil;
    public static Files files;
    public static Screen activeScreen;

    private boolean running = false;
    private double frameTime;

    long lastFrame;

    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS;
    int targetFPS;

    public static double renderTime = 0;
    public static double syncTime = 0;
    public static double inputTime = 0;
    public static double updateTime = 0;
    public static double totalTime = 0;

    public Aero(int width, int height, String title, int frameRate, boolean fullscreen, boolean vSync) {
        Window.setIcon(new File("res/default/textures/logo.png"));
        try {
            Window.createWindow(width, height, title, fullscreen, vSync);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        init(frameRate);
    }

    private void init(int frameRate) {
        int fr = frameRate;
        if (fr > 2000 || fr <= 25) {
            fr = 2000;
        }

        this.frameTime = 1.0 / fr;

        targetFPS = fr;

        Aero.graphicsUtil = new GraphicsUtil();
        Aero.graphics = new Graphics();
        Aero.input = new Input();
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

        double lastTime = Time.getDoubleTime();
        double frameCounter = 0;
        double unprocessedTime = 0;
        int frames = 0;

        while (running) {
            boolean render = false;

            double startTime = Time.getDoubleTime();
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
                Window.update();

                if (Window.isCloseRequested())
                    stop();


                activeScreen.input((float) frameTime);
                activeScreen.update((float) frameTime);

                if (Aero.input.getKey(Input.KEY_LALT) && Aero.input.getKeyDown(Input.KEY_RETURN))
                    Window.setDisplayMode(Window.getWidth(), Window.getHeight(), !Window.isFullscreen());

                input.update();

                render = true;

                unprocessedTime -= frameTime;
            }

            if (render) {
                activeScreen.render(graphics);
                Window.render();
                frames++;
            }
        }

        dispose();
    }

    private void dispose() {
        Window.dispose();
    }

    public static void setIcon(File file) {
        Window.setIcon(file);
    }

    public static Screen getActiveScreen() {
        return activeScreen;
    }

    public static void setActiveScreen(Screen activeScreen) {
        Aero.activeScreen = activeScreen;
        Aero.activeScreen.init();
    }

}
