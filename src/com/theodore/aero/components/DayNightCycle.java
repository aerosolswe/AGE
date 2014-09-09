package com.theodore.aero.components;

import com.theodore.aero.graphics.g3d.SkyBox;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class DayNightCycle extends GameComponent {

    public static final Vector3 DAY = new Vector3(0.75f, 0.74f, 0.67f);
    public static final Vector3 SUNRISE = new Vector3(0.71f, 0.49f, 0.35f);
    public static final Vector3 SUNSET = new Vector3(0.71f, 0.49f, 0.35f);
    public static final Vector3 NIGHT = new Vector3(0.08f, 0.08f, 0.08f);

    private DirectionalLight directionalLight;
    private SkyBox skyBox;

    private Vector3 color;
    private float startTime;
    private float timeAcceleration;

    private float time;

    public DayNightCycle(DirectionalLight directionalLight, SkyBox skyBox, float startTime, float timeAcceleration) {
        this.directionalLight = directionalLight;
        this.skyBox = skyBox;
        this.startTime = startTime;
        this.timeAcceleration = timeAcceleration;
        this.color = DAY;
        this.time = startTime;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        time += delta * timeAcceleration;

        skyBox.setColor(color);
        directionalLight.setColor(color);

        if (time >= 360 || time <= -360)
            time = 0;

        if (time > 170 && time < 210) {
            color = color.lerp(SUNRISE, delta * 1);
        } else if (time > 210 && time < 300) {
            color = color.lerp(DAY, delta * 1);
        } else if (time > 300 && time < 340) {
            color = color.lerp(SUNSET, delta * 1);
        } else if (time > 340 || time < 150) {
            color = color.lerp(NIGHT, delta * 1);
        }

        getTransform().setRotation(new Quaternion(new Vector3(1, 0, 0), (float) Math.toRadians(time)));
    }
}
