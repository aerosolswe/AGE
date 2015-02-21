package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Input;
import com.theodore.aero.graphics.Window;
import com.theodore.aero.input.Mapping;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

public class FreeLook extends GameComponent {

    private static final float MAX_LOOK_ANGLE = 89.99f;
    private static final float MIN_LOOK_ANGLE = -89.99f;

    private static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
    public float upAngle = 0;

    private boolean mouseLocked = false;
    private float sensitivity;

    public FreeLook(float sensitivity) {
        this.sensitivity = sensitivity;

        Aero.inputManager.addMouseMap("unlockMouse", Input.Buttons.MIDDLE);
    }

    @Override
    public void input(float delta) {
        Vector2 centerPosition = new Vector2(Aero.window.getWidth() / 2, Aero.window.getHeight() / 2);

        Mapping unlockMapping = Aero.inputManager.getMapping("unlockMouse");

        if (unlockMapping.isReleased() && mouseLocked) {
            Aero.input.showCursor();
            mouseLocked = false;
        } else if (unlockMapping.isReleased()) {
            Aero.input.setCursorPosition(centerPosition);
            Aero.input.hideCursor();
            mouseLocked = true;
        }

        if (mouseLocked) {
            Vector2 deltaPos = Aero.input.getCursorPosition().sub(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY) {
                float amt = (float) Math.toRadians(deltaPos.getX() * sensitivity);
                getTransform().rotate(Y_AXIS, amt);
            }
            if (rotX) {
                float amt = deltaPos.getY() * sensitivity;
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
                Aero.input.setCursorPosition(centerPosition);
        }
    }
}
