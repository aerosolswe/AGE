package com.theodore.aero.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

public class Network {

    private static ArrayList<Class> classes = new ArrayList<Class>();

    public static final int TCP_PORT = 44731;
    public static final int UDP_PORT = 44732;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        for (Class c : classes) {
            kryo.register(c);
        }

    }

    public static void addClass(Class aClass) {
        classes.add(aClass);
    }
}
