package com.theodore.tools.modelviewer;

import com.theodore.aero.core.Aero;
import com.theodore.aero.graphics.Window;

public class Main {

    public static void main(String[] args) {
        new Aero(1280, 720, "Model viewer 0.1", 2000, false, true).start(new ModelViewer());
    }

}
