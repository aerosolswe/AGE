package com.theodore.aero.core;


import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

public class SunSystem {

    public Quaternion direction;

    public float time;

    public SunSystem(float hour) {
        setTime(hour);
    }

    public void setTime(float hour) {
        time = ((hour / 24) * 360) + 90;

        direction = new Quaternion(new Vector3(1, 0, 0), ((float) Math.toRadians(time)));
    }

    public void addTime(float timeToAdd) {
        time += timeToAdd * Aero.graphics.getFloat("delta");

        direction = new Quaternion(new Vector3(1, 0, 0), ((float) Math.toRadians(time)));
    }

    public Quaternion getDirection() {
        return direction;
    }

    public void setDirection(Quaternion direction) {
        this.direction = direction;
    }

}
