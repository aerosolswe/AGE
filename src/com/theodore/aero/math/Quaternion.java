package com.theodore.aero.math;


public class Quaternion {

    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternion() {
        this(0, 0, 0, 1);
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Vector3 axis, float angle) {
        float sinHalfAngle = (float) Math.sin(angle / 2);
        float cosHalfAngle = (float) Math.cos(angle / 2);

        this.x = axis.getX() * sinHalfAngle;
        this.y = axis.getY() * sinHalfAngle;
        this.z = axis.getZ() * sinHalfAngle;
        this.w = cosHalfAngle;
    }

    public Quaternion(Vector3 direction, Vector3 up) {
        Quaternion res = new Matrix4().initRotation(direction, up).toQuaternion().normalized();
        x = res.getX();
        y = res.getY();
        z = res.getZ();
        w = res.getW();
    }

    public Quaternion(float heading, float attitude, float bank) {
        float c1 = (float) Math.cos(heading / 2);
        float s1 = (float) Math.sin(heading / 2);
        float c2 = (float) Math.cos(attitude / 2);
        float s2 = (float) Math.sin(attitude / 2);
        float c3 = (float) Math.cos(bank / 2);
        float s3 = (float) Math.sin(bank / 2);
        float c1c2 = c1 * c2;
        float s1s2 = s1 * s2;
        this.w = c1c2 * c3 - s1s2 * s3;
        this.x = c1c2 * s3 + s1s2 * c3;
        this.y = s1 * c2 * c3 + c1 * s2 * s3;
        this.z = c1 * s2 * c3 - s1 * c2 * s3;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion normalized() {
        float length = length();

        return new Quaternion(x / length, y / length, z / length, z / length);
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion mul(float r) {
        return new Quaternion(x * r, y * r, z * r, w * r);
    }

    public Quaternion mul(Quaternion r) {
        float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
        float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
        float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
        float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion mul(Vector3 r) {
        float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
        float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
        float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
        float z_ = w * r.getZ() + x * r.getY() - y * r.getX();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Vector3 mulv(Vector3 v) {
        Vector3 r = new Vector3();

        float vx = v.x, vy = v.y, vz = v.z;
        r.x = w * w * vx + 2 * y * w * vz - 2 * z * w * vy + x * x
                * vx + 2 * y * x * vy + 2 * z * x * vz - z * z * vx - y
                * y * vx;
        r.y = 2 * x * y * vx + y * y * vy + 2 * z * y * vz + 2 * w
                * z * vx - z * z * vy + w * w * vy - 2 * x * w * vz - x
                * x * vy;
        r.z = 2 * x * z * vx + 2 * y * z * vy + z * z * vz - 2 * w
                * y * vx - y * y * vz + 2 * w * x * vy - x * x * vz + w
                * w * vz;

        return r;
    }

    public Quaternion sub(Quaternion r) {
        return new Quaternion(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
    }

    public Quaternion add(Quaternion r) {
        return new Quaternion(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
    }

    public Matrix4 toRotationMatrix() {
        Vector3 forward = new Vector3(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
        Vector3 up = new Vector3(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
        Vector3 right = new Vector3(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

        return new Matrix4().initRotation(forward, up, right);
    }

    public float dot(Quaternion r) {
        return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
    }

    public Quaternion nLerp(Quaternion dest, float lerpFactor, boolean shortest) {
        Quaternion correctedDest = dest;

        if (shortest && this.dot(dest) < 0)
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());

        return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
    }

    public Quaternion sLerp(Quaternion dest, float lerpFactor, boolean shortest) {
        final float EPSILON = 1e3f;

        float cos = this.dot(dest);
        Quaternion correctedDest = dest;

        if (shortest && cos < 0) {
            cos = -cos;
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        if (Math.abs(cos) >= 1 - EPSILON)
            return nLerp(correctedDest, lerpFactor, false);

        float sin = (float) Math.sqrt(1.0f - cos * cos);
        float angle = (float) Math.atan2(sin, cos);
        float invSin = 1.0f / sin;

        float srcFactor = (float) Math.sin((1.0f - lerpFactor) * angle) * invSin;
        float destFactor = (float) Math.sin((lerpFactor) * angle) * invSin;

        return this.mul(srcFactor).add(correctedDest.mul(destFactor));
    }

    public Quaternion(Matrix4 rot) {
        float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

        if (trace > 0) {
            float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
            w = 0.25f / s;
            x = (rot.get(1, 2) - rot.get(2, 1)) * s;
            y = (rot.get(2, 0) - rot.get(0, 2)) * s;
            z = (rot.get(0, 1) - rot.get(1, 0)) * s;
        } else {
            if (rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
                w = (rot.get(1, 2) - rot.get(2, 1)) / s;
                x = 0.25f * s;
                y = (rot.get(1, 0) + rot.get(0, 1)) / s;
                z = (rot.get(2, 0) + rot.get(0, 2)) / s;
            } else if (rot.get(1, 1) > rot.get(2, 2)) {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
                w = (rot.get(2, 0) - rot.get(0, 2)) / s;
                x = (rot.get(1, 0) + rot.get(0, 1)) / s;
                y = 0.25f * s;
                z = (rot.get(2, 1) + rot.get(1, 2)) / s;
            } else {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
                w = (rot.get(0, 1) - rot.get(1, 0)) / s;
                x = (rot.get(2, 0) + rot.get(0, 2)) / s;
                y = (rot.get(1, 2) + rot.get(2, 1)) / s;
                z = 0.25f * s;
            }
        }

        float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        x /= length;
        y /= length;
        z /= length;
        w /= length;
    }

    public Vector3 getForward() {
        return new Vector3(0, 0, 1).rotate(this);
    }

    public Vector3 getBack() {
        return new Vector3(0, 0, -1).rotate(this);
    }

    public Vector3 getUp() {
        return new Vector3(0, 1, 0).rotate(this);
    }

    public Vector3 getDown() {
        return new Vector3(0, -1, 0).rotate(this);
    }

    public Vector3 getRight() {
        return new Vector3(1, 0, 0).rotate(this);
    }

    public Vector3 getLeft() {
        return new Vector3(-1, 0, 0).rotate(this);
    }

    public Quaternion set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternion set(Quaternion r) {
        set(r.getX(), r.getY(), r.getZ(), r.getW());
        return this;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public boolean equals(Quaternion r) {
        return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
    }


}
