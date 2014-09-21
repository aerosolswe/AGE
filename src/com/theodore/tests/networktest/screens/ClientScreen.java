package com.theodore.tests.networktest.screens;

import com.theodore.aero.graphics.Screen;

public class ClientScreen extends Screen {
/*
    private GameClient gameClient;

    private GameWorld gameWorld;

    private Label fpsLabel;

    public ClientScreen(String ipToConnect) {
        gameClient = new GameClient();
        gameClient.register();
        gameClient.start(ipToConnect);

        gameClient.getClient().addListener(new Listener() {

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);
            }

            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }
        });

        if (gameClient.isConnected()) {
            gameWorld = new GameWorld(this);

            fpsLabel = new Label("fps " + 60, 50, 0);

//            gui.add(fpsLabel);
        }
    }

    @Override
    public void create() {

    }

    @Override
    public void input(float delta) {
        if (gameWorld != null) {
            gameWorld.input(delta);
        }

    }

    @Override
    public void update(float delta) {
        if (gameWorld != null) {
            gameWorld.update(delta);
            fpsLabel.setText("fps " + Aero.graphics.getFramesPerSeconds() + " ping: " + gameClient.getReturnTripTime());
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {
        gameClient.close();
    }*/
}
