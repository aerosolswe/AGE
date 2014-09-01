package com.theodore.aero.graphics;

import com.theodore.aero.core.Aero;
import com.theodore.aero.input.Input;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;

public class FreeLookCamera extends Camera {

    private static final float MAX_LOOK_ANGLE = 89.99f;
    private static final float MIN_LOOK_ANGLE = -89.99f;

    boolean mouseLocked = false;

    float defaultMoveSpeed = 10;

    public void input(float delta) {
        float sensitivity = 0.3f;
        float movAmt = (defaultMoveSpeed * delta);

        if (Aero.input.getKey(Input.KEY_LSHIFT))
            movAmt /= 4;
        if (Aero.input.getMouseWheelMoved()) {
            final float factor = (float) (Math.sqrt(2) / 2.0);

            if (Aero.input.getMouseWheelAmount() > 0)
                defaultMoveSpeed *= factor;
            else
                defaultMoveSpeed /= factor;
            if (defaultMoveSpeed < 0)
                defaultMoveSpeed = 0;
        }

        if (Aero.input.getKey(Input.KEY_ESCAPE)) {
            Aero.input.setCursor(true);
            mouseLocked = false;
        }
        if (Aero.input.getMouseDown(2)) {
            if (!mouseLocked) {
                Aero.input.setMousePosition(Window.getCenterPosition());
                Aero.input.setCursor(false);
                mouseLocked = true;
            } else {
                Aero.input.setCursor(true);
                mouseLocked = false;
            }
        }

        if (Aero.input.getKey(Input.KEY_W))
            move(rotation.getForward(), movAmt);
        if (Aero.input.getKey(Input.KEY_S))
            move(rotation.getForward(), -movAmt);
        if (Aero.input.getKey(Input.KEY_A))
            move(rotation.getLeft(), movAmt);
        if (Aero.input.getKey(Input.KEY_D))
            move(rotation.getRight(), movAmt);
        if (Aero.input.getKey(Input.KEY_LCONTROL))
            move(Vector3.DOWN, movAmt);
        if (Aero.input.getKey(Input.KEY_SPACE))
            move(Vector3.UP, movAmt);

        if (mouseLocked) {
            Vector2 deltaPos = Aero.input.getMousePosition().sub(Window.getCenterPosition());

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                rotateY(deltaPos.getX() * sensitivity);
            if (rotX) {
                float amt = -deltaPos.getY() * sensitivity;
                if (amt + upAngle > -MIN_LOOK_ANGLE) {
                    rotateX(-MIN_LOOK_ANGLE - upAngle);
                    upAngle = -MIN_LOOK_ANGLE;
                } else if (amt + upAngle < -MAX_LOOK_ANGLE) {
                    rotateX(-MAX_LOOK_ANGLE - upAngle);
                    upAngle = -MAX_LOOK_ANGLE;
                } else {
                    rotateX(amt);
                    upAngle += amt;
                }
            }

            if (rotY || rotX)
                Aero.input.setMousePosition(Window.getCenterPosition());
        }
    }

    public void mapInput(float delta) {
        float sensitivity = 0.3f;
        float movAmt = (defaultMoveSpeed * delta);

        if (Aero.input.getKey(Input.KEY_LSHIFT))
            movAmt /= 4;
        if (Aero.input.getMouseWheelMoved()) {
            final float factor = (float) (Math.sqrt(2) / 2.0);

            if (Aero.input.getMouseWheelAmount() > 0)
                defaultMoveSpeed *= factor;
            else
                defaultMoveSpeed /= factor;
            if (defaultMoveSpeed < 0)
                defaultMoveSpeed = 0;
        }

        if (Aero.input.getKey(Input.KEY_ESCAPE)) {
            Aero.input.setCursor(true);
            mouseLocked = false;
        }
        if (Aero.input.getMouseDown(2)) {
            if (!mouseLocked) {
                Aero.input.setMousePosition(Window.getCenterPosition());
                Aero.input.setCursor(false);
                mouseLocked = true;
            } else {
                Aero.input.setCursor(true);
                mouseLocked = false;
            }
        }

        if (Aero.input.getKey(Input.KEY_W))
            move(rotation.getForward(), movAmt);
        if (Aero.input.getKey(Input.KEY_S))
            move(rotation.getForward(), -movAmt);
        if (Aero.input.getKey(Input.KEY_A))
            move(rotation.getLeft(), movAmt);
        if (Aero.input.getKey(Input.KEY_D))
            move(rotation.getRight(), movAmt);
        if (Aero.input.getKey(Input.KEY_LCONTROL))
            move(Vector3.DOWN, movAmt);
        if (Aero.input.getKey(Input.KEY_SPACE))
            move(Vector3.UP, movAmt);

        if (mouseLocked) {
            Vector2 deltaPos = Aero.input.getMousePosition().sub(Window.getCenterPosition());

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                rotateY(deltaPos.getX() * sensitivity);
            if (rotX) {
                float amt = -deltaPos.getY() * sensitivity;
                if (amt + upAngle > -MIN_LOOK_ANGLE) {
                    rotateX(-MIN_LOOK_ANGLE - upAngle);
                    upAngle = -MIN_LOOK_ANGLE;
                } else if (amt + upAngle < -MAX_LOOK_ANGLE) {
                    rotateX(-MAX_LOOK_ANGLE - upAngle);
                    upAngle = -MAX_LOOK_ANGLE;
                } else {
                    rotateX(amt);
                    upAngle += amt;
                }
            }

            if (rotY || rotX)
                Aero.input.setMousePosition(Window.getCenterPosition());
        }

        if (Aero.input.getMouse(1)) {
            Vector2 deltaPos = Aero.input.getMouseDelta();

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                rotateY(deltaPos.getX() * sensitivity);
            if (rotX) {
                float amt = -deltaPos.getY() * sensitivity;
                if (amt + upAngle > -MIN_LOOK_ANGLE) {
                    rotateX(-MIN_LOOK_ANGLE - upAngle);
                    upAngle = -MIN_LOOK_ANGLE;
                } else if (amt + upAngle < -MAX_LOOK_ANGLE) {
                    rotateX(-MAX_LOOK_ANGLE - upAngle);
                    upAngle = -MAX_LOOK_ANGLE;
                } else {
                    rotateX(amt);
                    upAngle += amt;
                }
            }
        }
    }

    public void move(Vector3 dir, float amt) {
        position = position.add(dir.mul(amt));
    }
}
