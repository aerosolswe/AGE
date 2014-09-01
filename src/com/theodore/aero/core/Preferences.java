package com.theodore.aero.core;

import java.io.*;

public class Preferences {

    BufferedWriter out = null;
    BufferedReader in = null;

    private String name;

    private StringBuilder preferences = new StringBuilder();


    public Preferences(String name) {
        this.name = name;
        try {
            in = new BufferedReader(new FileReader(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            out = new BufferedWriter(new FileWriter(name));
            out.write(preferences.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addValue(String name, boolean value) {
        preferences.append(name).append(" ").append(value).append(System.getProperty("line.separator"));
    }

    public void addValue(String name, int value) {
        preferences.append(name).append(" ").append(value).append(System.getProperty("line.separator"));
    }

    public void addValue(String name, float value) {
        preferences.append(name).append(" ").append(value).append(System.getProperty("line.separator"));

    }

    public void addValue(String name, double value) {
        preferences.append(name).append(" ").append(value).append(System.getProperty("line.separator"));
    }

    public void addValue(String name, long value) {
        preferences.append(name).append(" ").append(value).append(System.getProperty("line.separator"));
    }

    public void addValue(String name, byte[] value) {
        preferences.append(name).append(" ").append(value).append(System.getProperty("line.separator"));
    }

    public float getFloat(String name) {
        return Float.parseFloat(returnValue(name));
    }

    public int getInt(String name) {
        return Integer.parseInt(returnValue(name));
    }

    public boolean getBoolean(String name) {
        return Boolean.parseBoolean(returnValue(name));
    }

    public long getLong(String name) {
        return Long.parseLong(returnValue(name));
    }

    public double getDouble(String name) {
        return Double.parseDouble(returnValue(name));
    }

    private String returnValue(String name) {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                String[] s = line.split(" ");

                if (s[0].equals(name))
                    return s[1];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
