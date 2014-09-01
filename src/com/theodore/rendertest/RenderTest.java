package com.theodore.rendertest;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.GameObject;
import com.theodore.aero.core.Transform;
import com.theodore.aero.graphics.*;
import com.theodore.aero.graphics.g2d.gui.Image;
import com.theodore.aero.graphics.g2d.gui.Label;
import com.theodore.aero.graphics.g3d.lighting.DirectionalLight;
import com.theodore.aero.graphics.g3d.lighting.PointLight;
import com.theodore.aero.graphics.g3d.lighting.SpotLight;
import com.theodore.aero.input.Input;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class RenderTest extends Screen {

    private Label fpsLabel;

    private GameObject cube;
    private GameObject axe;
    private GameObject fighter;

    private DirectionalLight dirLight;
    private SpotLight spotLight;

    private FreeLookCamera camera;

    public RenderTest(){
//        Aero.graphics.setDeferredRendering();
        Aero.graphics.setAmbientLight(new Vector3(0.4f, 0.4f, 0.4f));

        cube = addGameObject("cube", "diffuse.png", "normal.png", "bump.png");
        cube.getTransform().setScale(1, 1, 1);
        cube.getTransform().setPosition(0, 0, 0);

        axe = addGameObject("axe/ax.obj", "axe/diff.png", "", "");
        axe.getTransform().setScale(0.1f, 0.1f, 0.1f);
        axe.getTransform().setPosition(0, 1, 3);

        Material fighterMat = new Material();
        fighterMat.setDiffuseTexture(Texture.get("fighter/diffuse.png", Aero.graphicsUtil.GL_NEAREST(), Aero.graphicsUtil.GL_REPEAT(), false));
        fighterMat.setSpecularIntensity(0.25f);
        fighterMat.setSpecularPower(1f);

        fighter = addGameObject("fighter/fighter.obj", fighterMat);
        fighter.getTransform().setScale(1f, 1f, 1f);
        fighter.getTransform().setPosition(5, 2, 0);

        Material fl = Material.getDefaultMaterials();
        fl.setTextureRepeat(50);
        fl.setSpecularIntensity(0.1f);
        fl.setSpecularPower(8f);

        GameObject floor =addGameObject("cube", fl);
        floor.getTransform().setScale(100, 1, 100);
        floor.getTransform().setPosition(0, -1, 0);

        fpsLabel = new Label("fps " + 60, 50, 0);

        gui.add(fpsLabel);

        dirLight = new DirectionalLight(new Vector3(1,0.9f,0.8f), 0.4f, new Quaternion(new Vector3(1, 0, 0), 120));
        spotLight = new SpotLight(new Vector3(2, 2, 2), new Vector3(1, 1, 1), 1f, new Quaternion(new Vector3(0, 1, 0), 120));

        addLight(dirLight);
        addLight(spotLight);

        camera = new FreeLookCamera();
        camera.setPosition(new Vector3(0, 5, -10));
        Transform.setCamera(camera);
    }

    @Override
    public void create() {

    }

    @Override
    public void input(float delta) {
        camera.input(delta);

        if(Aero.input.getKeyDown(Input.KEY_F1)){
            spotLight.getTransform().setPosition(camera.getPosition());
            spotLight.setDirection(camera.getRotation());
        }
    }

    @Override
    public void update(float delta) {
        fpsLabel.setText("fps " + Aero.graphics.getFramesPerSeconds());

        axe.getTransform().rotate(new Vector3(5 * delta, 5 * delta, 5 * delta));
    }

    private GameObject addGameObject(String mesh, Material material){
        GameObject go = new GameObject(Mesh.get(mesh), material);

        addGameObject(go);

        return go;
    }

    private GameObject addGameObject(String mesh, String diffuse, String normal, String bump){
        Material mat = new Material();
        mat.setDiffuseTexture(Texture.get(diffuse));

        if(!normal.isEmpty()){
            mat.setNormalTexture(Texture.get("normal.png"));
        }

        if(!bump.isEmpty()){
            mat.setBumpTexture(Texture.get("bump.png"));
        }

        mat.setSpecularIntensity(0.25f);
        mat.setSpecularPower(1f);

        GameObject go = new GameObject(Mesh.get(mesh), mat);

        addGameObject(go);

        return go;
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {

    }

    public static void main(String[] args){
        new Aero(1280, 720, "Render test", 120, false, 5).start(new RenderTest());
    }
}
