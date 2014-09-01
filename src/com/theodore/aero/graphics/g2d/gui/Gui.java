package com.theodore.aero.graphics.g2d.gui;

import java.util.ArrayList;

public class Gui {

    private ArrayList<Widget> widgets;

    public Gui() {
        widgets = new ArrayList<Widget>();
    }

    public void update(float delta) {
        for (Widget widget : widgets) {
            widget.update(delta);
        }
    }

    public void render() {
        for (Widget widget : widgets) {
            widget.draw();
        }
    }

    public void add(Widget widget) {
        widgets.add(widget);
    }

    public void remove(Widget widget) {
        widgets.remove(widget);
    }

    public ArrayList<Widget> getWidgets() {
        return widgets;
    }

}
