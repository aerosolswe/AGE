package com.theodore.aero.core;

import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.GraphicsUtil;
import com.theodore.aero.graphics.Screen;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.input.Input;
import com.theodore.aero.math.Time;
import org.lwjgl.LWJGLException;

import java.io.File;
import java.util.ArrayList;

public class Aero {

    private static final String RESOURCE_DIRECTORY = "res/";
    private static final String DEFAULT_RESOURCES = "default/";

    private static ArrayList<String> directories = new ArrayList<String>();
    private boolean isRunning = false;

    public static Graphics graphics;
    public static Input input;
    public static GraphicsUtil graphicsUtil;
    public static Screen activeScreen;

    private double frameTime;

    public Aero(int width, int height, String title, double frameRate, boolean fullscreen, int samples) {
        try {
            Window.createWindow(width, height, title, fullscreen, 90, frameRate, samples);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        init(false, frameRate);
    }

    public Aero(String title, double frameRate) {
        Settings.readFile();

        int width = Settings.getWidth();
        int height = Settings.getHeight();

        boolean fullscreen = Settings.isFullscreen();

        int samples = Settings.getSamples();

        float fov = Settings.getFov();

        try {
            Window.createWindow(width, height, title, fullscreen, fov, frameRate, samples);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        init(true, frameRate);
    }

    private void init(boolean settings, double frameRate) {
        if (frameRate > 2000) {
            frameRate = 2000;
        }
        this.frameTime = 1.0 / frameRate;
        addResourceDirectory(DEFAULT_RESOURCES);

        Aero.graphicsUtil = new GraphicsUtil();
        Aero.graphics = new Graphics();
        Aero.input = new Input();

        graphics.initGraphics();
        graphics.init();
    }

    public void start(Screen game) {
        if (isRunning)
            return;

        Aero.activeScreen = game;
        Aero.activeScreen.setEngine();
        Aero.setActiveScreen(activeScreen);

        run();
    }

    public void stop() {
        if (!isRunning)
            return;

        isRunning = false;
    }

    private void run() {
        isRunning = true;

        activeScreen.init();

        int frames = 0;
        double frameCounter = 0;

        double lastTime = Time.getTime();
        double unprocessedTime = 0;

        while (isRunning) {
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
                input.update();

                if (frameCounter >= 1.0) {
                    graphics.setCurrentFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }

            }

            if (render) {
                frames++;
                graphicsUtil.clearColorAndDepth();
//                graphics.fullRender();
                activeScreen.render(graphics);
                Window.render();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
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
    }

    public static void addResourceDirectory(String directoryName) {
        directories.add(directoryName);
    }

    public static String getResourcePath(String pathFromDirectory) {
        for (String path : directories) {
            String result = "./" + Aero.RESOURCE_DIRECTORY + path + pathFromDirectory;
            if (new File(result).exists())
                return result;
        }

        return null;
    }

}
