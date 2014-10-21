package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Input;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class DayNightCycle extends GameComponent {

    public static final Vector3 DAY = new Vector3(0.75f, 0.74f, 0.67f);
    public static final Vector3 SUNRISE = new Vector3(0.71f, 0.49f, 0.35f);
    public static final Vector3 SUNSET = new Vector3(0.71f, 0.49f, 0.35f);
    public static final Vector3 NIGHT = new Vector3(0.08f, 0.08f, 0.08f);

    private DirectionalLight directionalLight;

    private Vector3 color;
    private float startTime;
    private float timeAcceleration;
    private float inputAcceleration = 0;

    private float time;

    public DayNightCycle(DirectionalLight directionalLight, float startTime, float timeAcceleration) {
        this.directionalLight = directionalLight;
        this.startTime = startTime;
        this.timeAcceleration = timeAcceleration;
        this.color = DAY;
        this.time = startTime;
    }

    @Override
    public void input(float delta) {
        super.input(delta);

        if (Aero.input.getKeyDown(Input.KEY_UP)) {
            inputAcceleration = 10;
        } else if (Aero.input.getKeyUp(Input.KEY_UP)) {
            inputAcceleration = 0;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        time += delta * (timeAcceleration + inputAcceleration);

        directionalLight.setColor(color);

        /*if (time >= 370 || time <= -360)
            time = 100;

        if (time >= 264 && time <= 276) {
            time = 277;
        }*/

        if (time > 170 && time < 210) {
            color = color.lerp(SUNRISE, delta * timeAcceleration / time);
        } else if (time > 210 && time < 300) {
            color = color.lerp(DAY, delta * timeAcceleration / time);
        } else if (time > 300 && time < 340) {
            color = color.lerp(SUNSET, delta * timeAcceleration / time);
        } else if (time > 340 || time < 150) {
            color = color.lerp(NIGHT, delta * timeAcceleration / time);
        }

        getTransform().setRotation(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(time)));
    }
}
