package com.theodore.aero.components;

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
        this(speed, Input.Keys.KEY_W, Input.Keys.KEY_S, Input.Keys.KEY_A, Input.Keys.KEY_D, Input.Keys.KEY_SPACE, Input.Keys.KEY_LEFT_CONTROL, Input.Keys.KEY_LEFT_SHIFT);
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

        if (Aero.input.getKey(walkKey) == Input.Actions.PRESS)
            movAmt = (speed / 3) * delta;
        else
            movAmt = speed * delta;


        if (Aero.input.getKey(forwardKey) == Input.Actions.PRESS)
            move(getTransform().getRotation().getForward(), movAmt);
        if (Aero.input.getKey(backKey) == Input.Actions.PRESS)
            move(getTransform().getRotation().getForward(), -movAmt);
        if (Aero.input.getKey(leftKey) == Input.Actions.PRESS)
            move(getTransform().getRotation().getLeft(), movAmt);
        if (Aero.input.getKey(rightKey) == Input.Actions.PRESS)
            move(getTransform().getRotation().getRight(), movAmt);
        if (Aero.input.getKey(upKey) == Input.Actions.PRESS)
            move(new Vector3(0, 1, 0), movAmt);
        if (Aero.input.getKey(downKey) == Input.Actions.PRESS)
            move(new Vector3(0, 1, 0), -movAmt);
    }

    private void move(Vector3 dir, float amt) {
        getTransform().setPosition(getTransform().getPosition().add(dir.mul(amt)));
    }
}
