package com.theodore.aero.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class Window {

    private static PixelFormat pf = new PixelFormat();

    private static boolean sync = false;
    private static int maxFps = 3000;

    public static void createWindow(int width, int height, String title, boolean fullscreen, float fov, double frameRate, int samples) throws LWJGLException {
        setDisplayMode(width, height, fullscreen);
        Display.create(pf.withDepthBits(24).withSamples(samples).withSRGB(true));
        Display.setResizable(true);
        Display.setVSyncEnabled(false);
        Display.setTitle(title);
        Keyboard.create();
        Mouse.create();
        Window.sync((int) frameRate);
    }

    public static void update() {
        if (isResized()) {
            glViewport(0, 0, getWidth(), getHeight());
//            Transform.setSize(getWidth(), getHeight());
        }
    }

    public static void render() {
        Display.update();

        if (sync) {
            Display.sync(maxFps);
        }
    }

    public static void sync() {
        sync = true;
    }

    public static void sync(int maxFps) {
        sync = true;
        Window.maxFps = maxFps;
    }

    public static void desync() {
        sync = false;
    }

    public static void dispose() {
        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }

    public static void makeCurrent() {
        try {
            Display.makeCurrent();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCurrent() {
        try {
            return Display.isCurrent();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void bindAsRenderTarget() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        glViewport(0, 0, getWidth(), getHeight());
    }

    public static boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    public static boolean isCreated() {
        return Display.isCreated();
    }

    public static boolean isResized() {
        return Display.wasResized();
    }

    public static boolean isResizable() {
        return Display.isResizable();
    }

    public static boolean isFullscreen() {
        return Display.isFullscreen();
    }

    public static void setWidth(int width) {
        setDisplayMode(width, getHeight(), false);
        glViewport(0, 0, getWidth(), getHeight());
//        Transform.setSize(getWidth(), getHeight());
    }

    public static void setHeight(int height) {
        setDisplayMode(getWidth(), height, false);
        glViewport(0, 0, getWidth(), getHeight());
//        Transform.setSize(getWidth(), getHeight());
    }

    public static void setResolution(int width, int height, boolean fullScreen) {
        setDisplayMode(width, height, fullScreen);
        glViewport(0, 0, getWidth(), getHeight());
//        Transform.setSize(getWidth(), getHeight());
    }

    public static DisplayMode getDisplayMode() {
        return Display.getDisplayMode();
    }

    public static int getWidth() {
        return Display.getWidth();
    }

    public static int getHeight() {
        return Display.getHeight();
    }

    public static int getFrequency() {
        return Display.getDisplayMode().getFrequency();
    }

    public static int getDesktopFrequency() {
        return Display.getDesktopDisplayMode().getFrequency();
    }

    public static com.theodore.aero.math.Vector2 getCenterPosition() {
        return new com.theodore.aero.math.Vector2(Window.getWidth() / 2, Window.getHeight() / 2);
    }

    public static String getTitle() {
        return Display.getTitle();
    }

    public static void setResizable(boolean value) {
        Display.setResizable(value);
    }

    public static void setVSync(boolean value) {
        Display.setVSyncEnabled(value);
    }

    public static void setParent(Canvas parent) throws LWJGLException {
        Display.setParent(parent);
    }

    public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        return Display.getAvailableDisplayModes();
    }

    public static void setDisplayMode(int width, int height, boolean fullscreen) {
        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i = 0; i < modes.length; i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    public static void setIcon(String icon16, String icon32) {
        try {
            ByteBuffer[] icons = new ByteBuffer[2];
            icons[0] = loadIcon("res/default/" + icon16, 16, 16);
            icons[1] = loadIcon("res/default/" + icon32, 32, 32);
            Display.setIcon(icons);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ByteBuffer loadIcon(String filename, int width, int height) throws IOException {
        BufferedImage image = ImageIO.read(new File(filename)); // load image

        // convert image to byte array
        byte[] imageBytes = new byte[width * height * 4];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image.getRGB(j, i);
                for (int k = 0; k < 3; k++) // red, green, blue
                    imageBytes[(i * 16 + j) * 4 + k] = (byte) (((pixel >> (2 - k) * 8)) & 255);
                imageBytes[(i * 16 + j) * 4 + 3] = (byte) (((pixel >> (3) * 8)) & 255); // alpha
            }
        }
        return ByteBuffer.wrap(imageBytes);
    }
}
