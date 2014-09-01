package com.theodore.aero.core;


import java.io.*;

public class Settings {

    private static int width = 1280;
    private static int height = 720;
    private static int samples = 0;
    private static int shadowMapSize = 1024;
    private static int fov = 90;
    private static float masterVolume = 0.5f;
    private static float effectsVolume = 0.5f;
    private static float musicVolume = 0.5f;

    private static boolean fullscreen = false;

    public static void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("settings.cfg"));

            out.write("width " + width);
            out.newLine();
            out.write("height " + height);
            out.newLine();
            out.write("fullscreen " + fullscreen);
            out.newLine();
            out.write("anti-aliasing " + samples);
            out.newLine();
            out.write("shadow-map-size " + shadowMapSize);
            out.newLine();
            out.write("fov " + fov);
            out.newLine();
            out.write("master-volume " + masterVolume);
            out.newLine();
            out.write("effects-volume " + effectsVolume);
            out.newLine();
            out.write("music-volume " + musicVolume);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFile() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("settings.cfg"));

            String line;
            while ((line = in.readLine()) != null) {
                String[] s = line.split(" ");

                if (s[0].equals("width")) {
                    width = Integer.parseInt(s[1]);
                } else if (s[0].equals("height")) {
                    height = Integer.parseInt(s[1]);
                } else if (s[0].equals("fullscreen")) {
                    fullscreen = Boolean.parseBoolean(s[1]);
                } else if (s[0].equals("anti-aliasing")) {
                    samples = Integer.parseInt(s[1]);
                } else if (s[0].equals("shadow-map-size")) {
                    shadowMapSize = Integer.parseInt(s[1]);
                } else if (s[0].equals("fov")) {
                    fov = Integer.parseInt(s[1]);
                } else if (s[0].equals("master-volume")) {
                    masterVolume = Float.parseFloat(s[1]);
                } else if (s[0].equals("effects-volume")) {
                    effectsVolume = Float.parseFloat(s[1]);
                } else if (s[0].equals("music-volume")) {
                    musicVolume = Float.parseFloat(s[1]);
                }
            }

        } catch (IOException e) {
            System.err.println("Couldn't find settings file, creating a new one.");
            saveFile();
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static boolean isFullscreen() {
        return fullscreen;
    }

    public static int getSamples() {
        return samples;
    }

    public static int getShadowMapSize() {
        return shadowMapSize;
    }

    public static int getFov() {
        return fov;
    }

    public static void setWidth(int width) {
        Settings.width = width;
    }

    public static void setHeight(int height) {
        Settings.height = height;
    }

    public static void setFullscreen(boolean fullscreen) {
        Settings.fullscreen = fullscreen;
    }

    public static void setSamples(int samples) {
        Settings.samples = samples;
    }

    public static void setShadowMapSize(int shadowMapSize) {
        Settings.shadowMapSize = shadowMapSize;
    }

    public static void setFov(int fov) {
        Settings.fov = fov;
    }

    public static float getMasterVolume() {
        return masterVolume;
    }

    public static void setMasterVolume(float masterVolume) {
        Settings.masterVolume = masterVolume;
    }

    public static float getEffectsVolume() {
        return effectsVolume;
    }

    public static void setEffectsVolume(float effectsVolume) {
        Settings.effectsVolume = effectsVolume;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        Settings.musicVolume = musicVolume;
    }
}
