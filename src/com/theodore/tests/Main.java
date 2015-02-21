package com.theodore.tests;

import com.theodore.aero.core.Aero;

public class Main {
    public static void main(String[] args) {
        new Aero("Aero game engine v0.1", 1280, 720, 0, false, true, 8).start(new RenderTest());
    }

}
