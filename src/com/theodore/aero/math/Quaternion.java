package com.theodore.aero.math;


public class Quaternion {
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion() {
        this(0, 0, 0, 1);
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Vector3 direction) {
        Quaternion res = new Matrix4().initRotation(direction, Vector3.UP).toQuaternion().normalized();
        x = res.getX();
        y = res.getY();
        z = res.getZ();
        w = res.getW();
    }

    public Quaternion(Vector3 from, Vector3 to) {
        Vector3 H = from.add(to).normalized();

        w = from.dot(H);
        x = from.getY() * H.getZ() - from.getZ() * H.getY();
        y = from.getZ() * H.getX() - from.getX() * H.getZ();
        z = from.getX() * H.getY() - from.getY() * H.getX();
    }

    public Quaternion(Vector3 axis, float angle) {
        float halfAngle = (float) Math.toRadians(angle / 2);
        axis = axis.normalized();

        float sin = (float) Math.sin(halfAngle);
        float cos = (float) Math.cos(halfAngle);

        x = axis.getX() * sin;
        y = axis.getY() * sin;
        z = axis.getZ() * sin;
        w = cos;
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

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float dot(Quaternion r) {
        return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
    }

    public Quaternion normalized() {
        float length = length();

        return new Quaternion(x / length, y / length, z / length, w / length);
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion add(Quaternion r) {
        return new Quaternion(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
    }

    public Quaternion add(float amt) {
        return new Quaternion(x + amt, y + amt, z + amt, w + amt);
    }

    public Quaternion sub(Quaternion r) {
        return new Quaternion(x - r.getX(), x - r.getY(), z - r.getZ(), w - r.getW());
    }

    public Quaternion sub(float amt) {
        return new Quaternion(x - amt, y - amt, z - amt, w - amt);
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

    public Quaternion mul(float amt) {
        return new Quaternion(x * amt, y * amt, z * amt, w * amt);
    }

    public Quaternion div(float amt) {
        return new Quaternion(x / amt, y / amt, z / amt, w / amt);
    }

    public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest) {
        Quaternion correctedDest = dest;

        if (shortest && this.dot(dest) < 0)
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());

        return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
    }

    public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest) {
        final float EPSILON = 1e3f;

        float cos = this.dot(dest);
        Quaternion correctedDest = dest;

        if (shortest && cos < 0) {
            cos = -cos;
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        if (Math.abs(cos) >= 1 - EPSILON)
            return nlerp(correctedDest, lerpFactor, false);

        float sin = (float) Math.sqrt(1.0f - cos * cos);
        float angle = (float) Math.atan2(sin, cos);
        float invSin = 1.0f / sin;

        float srcFactor = (float) Math.sin((1.0f - lerpFactor) * angle) * invSin;
        float destFactor = (float) Math.sin((lerpFactor) * angle) * invSin;

        return this.mul(srcFactor).add(correctedDest.mul(destFactor));
    }

    public Vector3 getEulerAngles() {
        float sqx = x * x;
        float sqy = y * y;
        float sqz = z * z;
        float sqw = w * w;
        float unit = sqx + sqy + sqz + sqw;
        float test = x * y + z * w;

        float heading = 0;
        float attitude = 0;
        float bank = 0;

        if (test > 0.499f * unit) { // singularity at north pole
            heading = 2.0f * (float) Math.atan2(x, w);
            attitude = (float) Math.PI / 2.0f;
            bank = 0.0f;
        } else if (test < -0.499 * unit) { // singularity at south pole
            heading = -2.0f * (float) Math.atan2(x, w);
            attitude = -(float) Math.PI / 2.0f;
            bank = 0.0f;
        } else {
            heading = (float) Math.atan2(2.0f * y * w - 2.0f * x * z, sqx - sqy - sqz + sqw);
            attitude = (float) Math.asin(2.0f * test / unit);
            bank = (float) Math.atan2(2.0f * x * w - 2.0f * y * z, -sqx + sqy - sqz + sqw);
        }

        return new Vector3((float) Math.toDegrees(bank), (float) Math.toDegrees(heading), (float) Math.toDegrees(attitude));
    }

    public Matrix4 getRotationMatrix() {
        Vector3 right = getRight();
        Vector3 up = getUp();
        Vector3 forward = getForward();

        float[][] m = new float[][]{{right.getX(), right.getY(), right.getZ(), 0.0f},
                {up.getX(), up.getY(), up.getZ(), 0.0f},
                {forward.getX(), forward.getY(), forward.getZ(), 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}};
        return new Matrix4(m);
    }

    /*public Matrix4 toRotationMatrix()
    {
        Vector3 forward =  new Vector3(2.0f * (x*z - w*y), 2.0f * (y*z + w*x), 1.0f - 2.0f * (x*x + y*y));
        Vector3 up = new Vector3(2.0f * (x*y + w*z), 1.0f - 2.0f * (x*x + z*z), 2.0f * (y*z - w*x));
        Vector3 right = new Vector3(1.0f - 2.0f * (y*y + z*z), 2.0f * (x*y - w*z), 2.0f * (x*z + w*y));

        return new Matrix4().initRotation(forward, up, right);
    }*/

    public Vector3 getForward() {
        return new Vector3(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
    }

    public Vector3 getBack() {
        return new Vector3(-2.0f * (x * z - w * y), -2.0f * (y * z + w * x), -(1.0f - 2.0f * (x * x + y * y)));
    }

    public Vector3 getUp() {
        return new Vector3(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
    }

    public Vector3 getDown() {
        return new Vector3(-2.0f * (x * y + w * z), -(1.0f - 2.0f * (x * x + z * z)), -2.0f * (y * z - w * x));
    }

    public Vector3 getRight() {
        return new Vector3(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));
    }

    public Vector3 getLeft() {
        return new Vector3(-(1.0f - 2.0f * (y * y + z * z)), -2.0f * (x * y - w * z), -2.0f * (x * z + w * y));
    }

    public String toString() {
        return "(" + x + " " + y + " " + z + " " + w + ")";
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

    public Quaternion set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Quaternion set(Quaternion q) {
        this.x = q.getX();
        this.y = q.getY();
        this.z = q.getZ();
        this.w = q.getW();

        return this;
    }

    public boolean equals(Quaternion o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quaternion that = (Quaternion) o;

        if (Float.compare(that.w, w) != 0) return false;
        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;
        if (Float.compare(that.z, z) != 0) return false;

        return true;
    }

}
