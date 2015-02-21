package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;
import com.theodore.aero.math.Vector2;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.glfw.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    public long window;
    public String title = "";
    private GLFWErrorCallback errorCallback;
    private GLFWWindowSizeCallback windowSizeCallback;

    private boolean init = false;

    public Window(String title, int width, int height, boolean fullscreen, boolean vsync, int samples) {
        this.title = title;

        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_SAMPLES, samples);

        if (!fullscreen) {
            window = glfwCreateWindow(width, height, title, NULL, NULL);

            ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    window,
                    (GLFWvidmode.width(vidmode) - width) / 2,
                    (GLFWvidmode.height(vidmode) - height) / 2
            );
        } else {
            window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
        }

        if (window == GL_FALSE) {
            System.err.println("[Window] Unable to create window");
            System.exit(1);
        }

        makeContextCurrent();
        if (vsync)
            glfwSwapInterval(1);
        else
            glfwSwapInterval(0);
        glfwShowWindow(window);
        createContextFromCurrent();

        init = true;

        glfwSetCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                resize(width, height);
            }
        });

    }

    public void makeContextCurrent() {
        glfwMakeContextCurrent(window);
    }

    public void createContextFromCurrent() {
        GLContext.createFromCurrent();
    }

    public void setVSync(boolean vsync) {
        if (vsync)
            glfwSwapInterval(1);
        else
            glfwSwapInterval(0);
    }

    public void bindAsRenderTarget() {
        glViewport(0, 0, getWidth(), getHeight());
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void close() {
        glfwSetWindowShouldClose(window, 1);
    }

    public void clear(float r, float g, float b, float a) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(r, g, b, a);
    }

    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        Aero.activeScreen.resized(width, height);
        Aero.graphics.initDisplay(width, height);
    }

    public void render() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void destroy() {
        errorCallback.release();
        windowSizeCallback.release();
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void minimize() {
        glfwIconifyWindow(window);
    }

    public void restore() {
        glfwRestoreWindow(window);
    }

    public void hide() {
        glfwHideWindow(window);
    }

    public void show() {
        glfwShowWindow(window);
    }


    public boolean isClosed() {
        return glfwWindowShouldClose(window) != 0;
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(window, width, height);
    }

    public Vector2 getSize() {
        IntBuffer w = BufferUtils.createIntBuffer(4);
        IntBuffer h = BufferUtils.createIntBuffer(4);
        glfwGetWindowSize(window, w, h);

        return new Vector2(w.get(0), h.get(0));
    }

    public int getWidth() {
        return (int) getSize().x;
    }

    public int getHeight() {
        return (int) getSize().y;
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(window, x, y);
    }

    /*private static PixelFormat pf = new PixelFormat();

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

    public static void sync(int maxFps) {
        Display.sync(maxFps);
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
    }*/
}
