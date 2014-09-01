package com.theodore.aero.graphics;

import com.theodore.aero.graphics.g2d.gui.Gui;
import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.g2d.Sprite;
import com.theodore.aero.graphics.g3d.lighting.BaseLight;
import com.theodore.aero.graphics.g3d.lighting.DirectionalLight;

import java.util.ArrayList;

public abstract class Screen implements ScreenInterface {

    public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    public ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    public ArrayList<DirectionalLight> directionalLights = new ArrayList<DirectionalLight>();
    public Gui gui = new Gui();

    private ArrayList<BaseLight> lights = new ArrayList<BaseLight>();
    private BaseLight activeLight = null;

    @Override
    public abstract void create();

    @Override
    public abstract void input(float delta);

    @Override
    public abstract void update(float delta);

    @Override
    public abstract void render();

    @Override
    public abstract void dispose();

    public ArrayList<BaseLight> getLights() {
        return lights;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public ArrayList<DirectionalLight> getDirectionalShadows() {
        return directionalLights;
    }

    public Screen getActiveScreen() {
        return Aero.getActiveScreen();
    }

    public void setActiveScreen(Screen screen) {
        Aero.setActiveScreen(screen);
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void removeAllGameObjects() {
        gameObjects.clear();
    }

    public void addLight(BaseLight light) {
        getLights().add(light);
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public Gui getGui() {
        return gui;
    }

    public BaseLight getActiveLight() {
        return activeLight;
    }


    public void setActiveLight(BaseLight activeLight) {
        this.activeLight = activeLight;
    }


}
