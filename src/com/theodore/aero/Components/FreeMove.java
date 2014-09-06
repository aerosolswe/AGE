package com.theodore.aero.Components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Input;
import com.theodore.aero.math.Vector3;

public class FreeMove extends GameComponent {

    private float speed;
    private int forwardKey;
    private int backKey;
    private int leftKey;
    private int rightKey;
    private int upKey;
    private int downKey;
    private int walkKey;

    public FreeMove(float speed) {
        this(speed, Input.KEY_W, Input.KEY_S, Input.KEY_A, Input.KEY_D, Input.KEY_SPACE, Input.KEY_LCONTROL, Input.KEY_LSHIFT);
    }

    public FreeMove(float speed, int forwardKey, int backKey, int leftKey, int rightKey, int upKey, int downKey, int walkKey) {
        this.speed = speed;
        this.forwardKey = forwardKey;
        this.backKey = backKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.walkKey = walkKey;
    }

    @Override
    public void input(float delta) {
        float movAmt;

        if (Aero.input.getKey(walkKey))
            movAmt = (speed / 3) * delta;
        else
            movAmt = speed * delta;


        if (Aero.input.getKey(forwardKey))
            move(getTransform().getRotation().getForward(), movAmt);
        if (Aero.input.getKey(backKey))
            move(getTransform().getRotation().getForward(), -movAmt);
        if (Aero.input.getKey(leftKey))
            move(getTransform().getRotation().getLeft(), movAmt);
        if (Aero.input.getKey(rightKey))
            move(getTransform().getRotation().getRight(), movAmt);
        if (Aero.input.getKey(upKey))
            move(getTransform().getRotation().getUp(), movAmt);
        if (Aero.input.getKey(downKey))
            move(getTransform().getRotation().getUp(), -movAmt);
    }

    private void move(Vector3 dir, float amt) {
        getTransform().setPosition(getTransform().getPosition().add(dir.mul(amt)));
    }
}
