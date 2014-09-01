package com.theodore.networktest;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.graphics.Material;
import com.theodore.aero.graphics.Mesh;
import com.theodore.aero.input.Input;

public class Player {

    private GameObject gameObject;

    public Player(){
        gameObject = new GameObject(Mesh.get("cube"), Material.getDefaultMaterials());
    }

    public void input(float delta){

        if(Aero.input.getKeyDown(Input.KEY_W)){

        }

        if(Aero.input.getKeyDown(Input.KEY_S)){

        }

        if(Aero.input.getKeyDown(Input.KEY_A)){

        }

        if(Aero.input.getKeyDown(Input.KEY_D)){

        }

    }

    public void update(float delta){

    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
