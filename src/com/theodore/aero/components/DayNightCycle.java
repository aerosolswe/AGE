package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Input;
import com.theodore.aero.core.SunSystem;
import com.theodore.aero.math.Quaternion;
import com.theodore.aero.math.Vector3;

import java.util.Date;

public class DayNightCycle extends GameComponent {

    private SunSystem sunSystem;

    public DayNightCycle(float hour) {
        sunSystem = new SunSystem(hour);
    }

    @Override
    public void input(float delta) {
        super.input(delta);

        if (Aero.input.getKey(Input.Keys.KEY_UP) == Input.Actions.PRESS) {
            sunSystem.addTime(24);
        }
        if (Aero.input.getKey(Input.Keys.KEY_DOWN) == Input.Actions.PRESS) {
            sunSystem.addTime(-24);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        getTransform().setRotation(sunSystem.getDirection());
    }
}
