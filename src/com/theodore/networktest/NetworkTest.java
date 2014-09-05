package com.theodore.networktest;

import com.theodore.aero.graphics.Screen;

public class NetworkTest extends Screen {
/*

    private Label connect;
    private Label host;

    private TextField ip;

    public NetworkTest() {
        Aero.graphicsUtil.setClearColor(0.2f, 0.4f, 0.6f, 1);

        connect = new Label("connect", 50, Window.getHeight() / 2 - 100, 42, 42);
        connect.setxOffset(21);
        host = new Label("host", 50, Window.getHeight() / 2 - 50, 42, 42);
        host.setxOffset(21);

        ip = new TextField("127.0.0.1", 250, Window.getHeight() / 2 - 100, 42, 42);
        ip.setxOffset(21);

        gui.add(connect);
        gui.add(host);
        gui.add(ip);
        setGui(gui);
    }

    @Override
    public void create() {

    }

    @Override
    public void input(float delta) {
        if (connect.isClicked()) {
            setActiveScreen(new ClientScreen(ip.getText()));
        }

        if (host.isClicked()) {
            setActiveScreen(new ServerScreen());
        }

    }

    @Override
    public void update(float delta) {
        gui.update(delta);
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {

    }

    public static void main(String[] args) {
        new Aero(1280, 720, "Network test", 60, false, 16).start(new NetworkTest());
    }
*/

}
