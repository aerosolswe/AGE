package com.theodore.aero.core;

import com.theodore.aero.components.GameComponent;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.graphics.g2d.gui.Gui;
import com.theodore.aero.graphics.g2d.gui.Label;
import com.theodore.aero.graphics.shaders.Shader;

import java.text.DecimalFormat;

public class Debug {

    private Gui gui;

    private Label fpsLabel;
    private Label renderTimeLabel;
    private Label syncTimeLabel;
    private Label inputTimeLabel;
    private Label updateTimeLabel;
    private Label totalTimeLabel;

    private DecimalFormat format;

    public Debug() {
        gui = new Gui(Aero.window.getWidth(), Aero.window.getHeight());

        fpsLabel = new Label("Frames per seconds: 120", 17, Aero.window.getHeight() - 20, 10, 20, 10);
        renderTimeLabel = new Label("Render time: 0 ms", 17, fpsLabel.getY() - 28, 10, 20, 10);
        syncTimeLabel = new Label("Sync time: 0 ms", 17, renderTimeLabel.getY() - 18, 10, 20, 10);
        inputTimeLabel = new Label("Input time: 0 ms", 17, syncTimeLabel.getY() - 18, 10, 20, 10);
        updateTimeLabel = new Label("Update time: 0 ms", 17, inputTimeLabel.getY() - 18, 10, 20, 10);
        totalTimeLabel = new Label("Total time: 0 ms", 17, updateTimeLabel.getY() - 18, 10, 20, 10);

        gui.addWidget(fpsLabel);
        gui.addWidget(renderTimeLabel);
        gui.addWidget(syncTimeLabel);
        gui.addWidget(inputTimeLabel);
        gui.addWidget(updateTimeLabel);
//        gui.addWidget(totalTimeLabel);

        format = new DecimalFormat("0.0000");
    }

    public void input(float delta) {
        if (Aero.input.getKey(Input.Keys.KEY_F12) == Input.Actions.PRESS) {
            gui.getParent().setVisible(!gui.getParent().isVisible());
        }

        if (Aero.input.getKey(Input.Keys.KEY_F11) == Input.Actions.PRESS) {
            Aero.graphics.setBoolean("wireframe", !Aero.graphics.getBoolean("wireframe"));
        }
    }

    public void update(float delta) {
        fpsLabel.setText("Frames per seconds: " + Aero.graphics.getInteger("fps"));
        renderTimeLabel.setText("Render time: " + format.format(Aero.renderTime) + " ms");
        syncTimeLabel.setText("Sync time: " + format.format(Aero.syncTime) + " ms");
        inputTimeLabel.setText("Input time: " + format.format(Aero.inputTime) + " ms");
        updateTimeLabel.setText("Update time: " + format.format(Aero.updateTime) + " ms");
        totalTimeLabel.setText("Total time: " + format.format(Aero.totalTime) + " ms");

        gui.update(delta);
    }

    public void draw() {
        gui.draw(Aero.graphics);
    }
}
