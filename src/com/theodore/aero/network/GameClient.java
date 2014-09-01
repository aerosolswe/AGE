package com.theodore.aero.network;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class GameClient {

    private Client client;

    public GameClient() {
        client = new Client();
        client.start();
    }

    public void start(String ipToConnect) {
        try {
            client.connect(5000, ipToConnect, Network.TCP_PORT, Network.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(int i) {
        try {
            client.update(i);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendTCP(Object object) {
        client.sendTCP(object);
    }

    public void sendUDP(Object object) {
        client.sendUDP(object);
    }

    public void register() {
        Network.register(client);
    }

    public void addClass(Class aClass) {
        Network.addClass(aClass);
    }

    public void close() {
        client.stop();
        client.close();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setKeepAliveTCP(int keepAliveMillis) {
        client.setKeepAliveTCP(keepAliveMillis);
    }

    public void setKeepAliveUDP(int keepAliveMillis) {
        client.setKeepAliveUDP(keepAliveMillis);
    }

    public void setName(String name) {
        client.setName(name);
    }

    public int getID() {
        return client.getID();
    }

    public int getReturnTripTime() {
        return client.getReturnTripTime();
    }

    public boolean isConnected() {
        return client.isConnected();
    }
}
