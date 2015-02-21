package com.theodore.aero.input;

import java.util.ArrayList;

public class Mapping {

    private final String name;
    private ArrayList<Integer> keys = new ArrayList<Integer>();
    private ArrayList<Integer> buttons = new ArrayList<Integer>();
    private boolean pressed = false;
    private boolean released = false;
    private boolean repeated = false;
    private boolean mwheeldown = false;
    private boolean mwheelup = false;

    public boolean checkMwheeldown = false;
    public boolean checkMwheelup = false;

    public Mapping(String name, int value, boolean key) {
        this.name = name;
        if (key)
            this.keys.add(value);
        else
            this.buttons.add(value);
    }

    public Mapping(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addKey(int key) {
        keys.add(key);
    }

    public int getKey(int i) {
        return keys.get(i);
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }

    public void addButton(int button) {
        buttons.add(button);
    }

    public int getButton(int i) {
        return buttons.get(i);
    }

    public ArrayList<Integer> getButtons() {
        return buttons;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public boolean isMwheeldown() {
        return mwheeldown;
    }

    public void setMwheeldown(boolean mwheeldown) {
        this.mwheeldown = mwheeldown;
    }

    public boolean isMwheelup() {
        return mwheelup;
    }

    public void setMwheelup(boolean mwheelup) {
        this.mwheelup = mwheelup;
    }
}
