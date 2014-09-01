package com.theodore.aero.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer {

    private Server server;

    public GameServer() {
        server = new Server();
        server.start();
    }

    public void start() {
        try {
            server.bind(Network.TCP_PORT, Network.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update(int i) {
        try {
            server.update(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToTCP(int connectionID, Object object) {
        server.sendToTCP(connectionID, object);
    }

    public void sendToUDP(int connectionID, Object object) {
        server.sendToUDP(connectionID, object);
    }

    public void sendToAllExceptTCP(int connectionID, Object object) {
        server.sendToAllExceptTCP(connectionID, object);
    }

    public void sendToAllExceptUDP(int connectionID, Object object) {
        server.sendToAllExceptUDP(connectionID, object);
    }

    public void sendToAllTCP(Object object) {
        server.sendToAllTCP(object);
    }

    public void sendToAllUDP(Object object) {
        server.sendToAllUDP(object);
    }

    public void register() {
        Network.register(server);
    }

    public void addClass(Class aClass) {
        Network.addClass(aClass);
    }

    public void close() {
        server.stop();
        server.close();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Connection[] getConnections() {
        return server.getConnections();
    }
}
