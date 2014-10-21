package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Util;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class Window {

    private static PixelFormat pf = new PixelFormat();

    private static boolean sync = false;
    private static int maxFps = 3000;

    private static boolean once = false;

    public static void createWindow(int width, int height, String title, boolean fullscreen, boolean vSync) throws LWJGLException {
        setDisplayMode(width, height, fullscreen);
        Display.create();
        Display.setResizable(!fullscreen);
        Display.setVSyncEnabled(vSync);
        Display.setTitle(title);
        Keyboard.create();
        Mouse.create();

        System.out.println("OpenGL version: " + GraphicsUtil.getOpenglVersion());
    }

    public static void update() {
        if (isResized()) {
            if (!once) {
                Aero.activeScreen.resized(getWidth(), getHeight());
                glViewport(0, 0, getWidth(), getHeight());
                Aero.graphics.initDisplay(getWidth(), getHeight());
                once = true;
            }
        } else {
            once = false;
        }
    }

    public static void render() {
        Display.update();
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

    public static void setFullscreen(boolean value) {
        try {
            Display.setFullscreen(value);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
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

    public static void setIcon(File file) {
        try {
            ByteBuffer[] icons = new ByteBuffer[2];
            icons[0] = loadIcon(file);
            icons[1] = loadIcon(file);
            Display.setIcon(icons);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ByteBuffer loadIcon(File file) throws IOException {
        try {
            BufferedImage image = ImageIO.read(file);

            boolean hasAlpha = image.getColorModel().hasAlpha();

            int[] pixels = image.getRGB(0, 0, image.getWidth(),
                    image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = Util.createByteBuffer(image.getWidth() * image.getHeight() * 4);

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) ((pixel >> 0) & 0xFF));
                    if (hasAlpha)
                        buffer.put((byte) ((pixel >> 24) & 0xFF));
                    else
                        buffer.put((byte) (0xFF));
                }
            }

            buffer.flip();

            return buffer;

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
