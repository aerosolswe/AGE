package com.theodore.tests;

import com.theodore.aero.core.Aero;

public class Main {
    public static void main(String[] args) {
        new Aero(1280, 720, "Aero game engine v0.1", 2000, false, false).start(new RenderTest());
    }
}
