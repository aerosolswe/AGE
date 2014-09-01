package com.theodore.networktest;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.FreeLookCamera;
import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Mesh;
import com.theodore.aero.graphics.Screen;
import com.theodore.aero.graphics.g2d.gui.Label;
import com.theodore.aero.graphics.g3d.lighting.DirectionalLight;
import com.theodore.aero.graphics.g3d.lighting.PointLight;
import com.theodore.aero.graphics.g3d.lighting.SpotLight;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.network.GameClient;

public class GameWorld {

    private DirectionalLight dirLight;

    private FreeLookCamera camera;

    public GameWorld(Screen screen){
        Material fl = Material.getDefaultMaterials();
        fl.setTextureRepeat(50);
        fl.setSpecularIntensity(0.1f);
        fl.setSpecularPower(8f);

        GameObject floor = new GameObject(Mesh.get("cube"), fl);
        floor.castShadows(false);
        floor.getTransform().setScale(100, 1, 100);

        screen.addGameObject(floor);

        Quaternion d1 = new Quaternion(new Vector3(0,1,-1));

        dirLight = new DirectionalLight(new Vector3(1,0.9f,0.8f), 1f, d1);

        screen.addLight(dirLight);

        camera = new FreeLookCamera();
        Transform.setCamera(camera);
    }

    public void input(float delta){
        camera.input(delta);
    }

    public void update(float delta){

    }
}
