package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Bitmap {

    public static final String DIRECTORY = "bitmaps/";

    private int width;
    private int height;
    private int[] pixels;

    private static final HashMap<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();

    public static Bitmap get(String name) {
        if (bitmaps.containsKey(name))
            return bitmaps.get(name);
        else {
            bitmaps.put(name, new Bitmap(name));
            return bitmaps.get(name);
        }
    }

    private Bitmap(String fileName) {
        try {
            BufferedImage image = ImageIO.read(new File(Aero.getResourcePath(DIRECTORY + fileName)));

            width = image.getWidth();
            height = image.getHeight();

            pixels = new int[width * height];

            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public Bitmap flipX() {
        int[] tmp = new int[pixels.length];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tmp[i + j * width] = pixels[(width - i - 1) + j * width];

        pixels = tmp;

        return this;
    }

    public Bitmap flipY() {
        int[] tmp = new int[pixels.length];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tmp[i + j * width] = pixels[i + (height - j - 1) * width];

        pixels = tmp;

        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getPixel(int x, int y) {
        return pixels[x + y * width];
    }

    public void setPixel(int x, int y, int value) {
        pixels[x + y * width] = value;
    }

}
