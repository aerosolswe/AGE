package com.theodore.aero.core;

import com.theodore.aero.graphics.*;
import com.theodore.aero.math.Time;
import org.lwjgl.LWJGLException;

import java.io.File;
import java.util.ArrayList;

public class Aero {

    private static ArrayList<String> directories = new ArrayList<String>();

    public static Graphics graphics;
    public static Input input;
    public static GraphicsUtil graphicsUtil;
    public static Files files;
    public static Screen activeScreen;

    private boolean running = false;
    private boolean paused = false;
    private double frameTime;

    public static double renderTime = 0;
    public static double syncTime = 0;
    public static double inputTime = 0;
    public static double updateTime = 0;
    public static double totalTime = 0;


    public Aero(int width, int height, String title, float frameRate, boolean fullscreen, boolean vSync) {
        Window.setIcon("32.png", "32.png");
        try {
            Window.createWindow(width, height, title, fullscreen, vSync);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        init(frameRate);
    }

    private void init(float frameRate) {
        if (frameRate > 2000 || frameRate <= 25) {
            frameRate = 2000;
        }

        this.frameTime = 1.0 / frameRate;

        Aero.graphicsUtil = new GraphicsUtil();
        Aero.graphics = new Graphics();
        Aero.input = new Input();
        files = new Files();

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
        if (!running)
            return;

        running = false;
    }


    public void run() {
        running = true;

        int frames = 0;
        double frameCounter = 0;

        double lastTime = Time.getTime();
        double unprocessedTime = 0;

        while (running) {
            boolean render = false;

            double startTime = Time.getTime();
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            frameCounter += passedTime;
            unprocessedTime += passedTime;

            while (unprocessedTime > frameTime) {
                render = true;

                unprocessedTime -= frameTime;

                if (Window.isCloseRequested())
                    stop();

                Window.update();
                activeScreen.input((float) frameTime);
                activeScreen.update((float) frameTime);

                if(Aero.input.getKey(Input.KEY_LALT) && Aero.input.getKeyDown(Input.KEY_RETURN))
                    Window.setDisplayMode(Window.getWidth(), Window.getHeight(), !Window.isFullscreen());

                input.update();

                if (frameCounter >= 1.0) {
                    totalTime = (1000.0 * frameCounter) / (double) frames;

                    renderTime = graphics.getRenderTimeAndReset(frames);
                    syncTime = graphics.getSyncTimeAndReset(frames);
                    inputTime = activeScreen.getInputTimeAndReset(frames);
                    updateTime = activeScreen.getUpdateTimeAndReset(frames);

                    graphics.setCurrentFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                activeScreen.render(graphics);
                Window.render();
                frames++;
            } else {
                int sleepTime = (int)(1000.0 * (unprocessedTime - frameTime));
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        cleanUp();
    }

    private void cleanUp() {
        Window.dispose();
    }

    public static void setIcon(String icon16, String icon32) {
        Window.setIcon(icon16, icon32);
    }

    public static Screen getActiveScreen() {
        return activeScreen;
    }

    public static void setActiveScreen(Screen activeScreen) {
        Aero.activeScreen = activeScreen;
        Aero.activeScreen.init();
    }

}
