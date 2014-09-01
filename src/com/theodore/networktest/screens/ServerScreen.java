package com.theodore.networktest.screens;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.theodore.aero.graphics.Screen;
import com.theodore.aero.network.GameServer;

public class ServerScreen extends Screen {

    private GameServer gameServer;

    public ServerScreen(){
        gameServer = new GameServer();
        gameServer.register();
        gameServer.start();

        gameServer.getServer().addListener(new Listener() {

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);
            }

            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                System.out.println("Player with id: " + connection.getID() + " has connected");
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                System.out.println("Player with id: " + connection.getID() + " has disconnected");
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }

        });
    }

    @Override
    public void create() {

    }

    @Override
    public void input(float delta) {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {
        gameServer.close();
    }
}
