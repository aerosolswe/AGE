package com.theodore.tests;

import com.theodore.aero.core.Aero;

public class Main {
    public static void main(String[] args) {
        new Aero(1366, 768, "Aero game engine v0.1", 120, false, false).start(new RenderTest());
    }

}
