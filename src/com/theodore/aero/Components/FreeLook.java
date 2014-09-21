package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Input;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

public class FreeLook extends GameComponent {

    private static final float MAX_LOOK_ANGLE = 89.99f;
    private static final float MIN_LOOK_ANGLE = -89.99f;

    private static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
    public float upAngle = 0;

    private boolean mouseLocked = false;
    private float sensitivity;
    private int unlockMouseKey;

    public FreeLook(float sensitivity) {
        this(sensitivity, Input.KEY_ESCAPE);
    }

    public FreeLook(float sensitivity, int unlockMouseKey) {
        this.sensitivity = sensitivity;
        this.unlockMouseKey = unlockMouseKey;
    }

    @Override
    public void input(float delta) {
        Vector2 centerPosition = new Vector2(Window.getWidth() / 2, Window.getHeight() / 2);

        if (Aero.input.getKeyUp(unlockMouseKey)) {
            Aero.input.setMouseGrabbed(true);
            mouseLocked = false;
        }
        if (Aero.input.getMouseDown(2)) {
            Aero.input.setMousePosition(centerPosition);
            Aero.input.setMouseGrabbed(false);
            mouseLocked = true;
        }

        if (mouseLocked) {
            Vector2 deltaPos = Aero.input.getMousePosition().sub(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                getTransform().rotate(Y_AXIS, (float) Math.toRadians(deltaPos.getX() * sensitivity));
            if (rotX) {
                float amt = -deltaPos.getY() * sensitivity;
                if (amt + upAngle > -MIN_LOOK_ANGLE) {
                    getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-MIN_LOOK_ANGLE - upAngle));
                    upAngle = -MIN_LOOK_ANGLE;
                } else if (amt + upAngle < -MAX_LOOK_ANGLE) {
                    getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-MAX_LOOK_ANGLE - upAngle));
                    upAngle = -MAX_LOOK_ANGLE;
                } else {
                    getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(amt));
                    upAngle += amt;
                }
            }

            if (rotY || rotX)
                Aero.input.setMousePosition(centerPosition);
        }
    }
}
