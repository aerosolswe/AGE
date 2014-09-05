package com.theodore.aero.graphics;

import com.theodore.aero.core.GameObject;

public abstract class Screen {

    private GameObject root;

    public void init() {
    }

    public void input(float delta) {
        getRootGameObject().inputAll(delta);
    }

    public void update(float delta) {
        getRootGameObject().updateAll(delta);
    }

    public void render(Graphics graphics) {
        graphics.fullRender(getRootGameObject());
    }

    public void addObject(GameObject object) {
        getRootGameObject().addChild(object);
    }

    private GameObject getRootGameObject() {
        if (root == null)
            root = new GameObject();

        return root;
    }

    public void setEngine() {
        getRootGameObject().setEngine();
    }

    /*public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
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
    }*/

}
