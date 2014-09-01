package com.theodore.aero.graphics;

public interface ScreenInterface {

    public void create();

    public void input(float delta);

    public void update(float delta);

    public void render();

    public void dispose();
}
