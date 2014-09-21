package com.theodore.aero.math;

public class Vector3 {

    public static final Vector3 UP = new Vector3(0, 1, 0);
    public static final Vector3 DOWN = new Vector3(0, -1, 0);
    public static final Vector3 LEFT = new Vector3(-1, 0, 0);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 FORWARD = new Vector3(0, 0, 1);
    public static final Vector3 BACK = new Vector3(0, 0, -1);
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 ONE = new Vector3(1, 1, 1);

    public float x;
    public float y;
    public float z;

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector3(Vector3 vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    public Vector3(Vector2 r) {
        this.x = r.getX();
        this.y = r.getY();
        this.z = 0;
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float distance(Vector3 r) {
        return this.sub(r).length();
    }

    public float angleBetween(Vector3 r) {
        float cosTheta = this.dot(r) / (this.length() * r.length());

        return (float) Math.acos(cosTheta);
    }

    public float dot(Vector3 r) {
        return x * r.getX() + y * r.getY() + z * r.getZ();
    }

    public Vector3 cross(Vector3 r) {
        float x_ = y * r.getZ() - z * r.getY();
        float y_ = z * r.getX() - x * r.getZ();
        float z_ = x * r.getY() - y * r.getX();

        return new Vector3(x_, y_, z_);
    }


    public Vector3 normalized() {
        float length = length();

        return new Vector3(x / length, y / length, z / length);
    }

    public Vector3 min(Vector3 r) {
        return new Vector3(Math.min(x, r.getX()), Math.min(y, r.getY()), Math.min(z, r.getZ()));
    }

    public Vector3 max(Vector3 r) {
        return new Vector3(Math.max(x, r.getX()), Math.max(y, r.getY()), Math.max(z, r.getZ()));
    }

    public float max() {
        return Math.max(x, Math.max(y, z));
    }

    public Vector3 clamp(float maxLength) {
        if (lengthSquared() <= maxLength * maxLength)
            return this;

        return this.normalized().mul(maxLength);
    }

    public Vector3 towards(Vector3 r, float amt) {
        Vector3 result = r.sub(this);

        if (result.length() < amt)
            return this.add(result);

        return this.add(result.normalized().mul(amt));
    }

    public Vector3 reflect(Vector3 normal) {
        return this.sub(normal.mul(this.dot(normal)).mul(2));
    }

    /*inline Vector<T,D> Reflect(const Vector<T,D>& normal) const
    {
        return *this - (normal * (this->Dot(normal) * 2));
    }*/

    public Vector3 rotate(float angle, Vector3 axis) {
        return this.cross(axis.mul((float) Math.sin(Math.toRadians(-angle)))).add(this.mul((float) Math.cos(Math.toRadians(-angle))));
    }

    public Vector3 rotate(Quaternion rotation) {
        float x1 = rotation.getY() * z - rotation.getZ() * y;
        float y1 = rotation.getZ() * x - rotation.getX() * z;
        float z1 = rotation.getX() * y - rotation.getY() * x;

        float x2 = rotation.getW() * x1 + rotation.getY() * z1 - rotation.getZ() * y1;
        float y2 = rotation.getW() * y1 + rotation.getZ() * x1 - rotation.getX() * z1;
        float z2 = rotation.getW() * z1 + rotation.getX() * y1 - rotation.getY() * x1;

        return new Vector3(x + 2.0f * x2, y + 2.0f * y2, z + 2.0f * z2);
    }

    public Vector3 lerp(Vector3 dest, float lerpFactor) {
        return dest.sub(this).mul(lerpFactor).add(this);
    }


    public Vector3 approach(Vector3 goal, float delta) {
        Vector3 current = new Vector3(x, y, z);

        Vector3 difference = goal.sub(current);

        if (difference.length() > delta)
            return current.add(delta);
        if (difference.length() < -delta)
            return current.sub(delta);

        return goal;
    }

    public Vector3 approachX(float goal, float delta) {
        float current = x;

        float difference = goal - current;

        if (difference > delta) {
            x = current + delta;
            return this;
        }

        if (difference < -delta) {
            x = current - delta;
            return this;
        }

        x = goal;

        return this;
    }

    public Vector3 approachY(float goal, float delta) {
        float current = y;

        float difference = goal - current;

        if (difference > delta) {
            y = current + delta;
            return this;
        }

        if (difference < -delta) {
            y = current - delta;
            return this;
        }

        y = goal;

        return this;
    }

    public Vector3 approachZ(float goal, float delta) {
        float current = z;

        float difference = goal - current;

        if (difference > delta) {
            z = current + delta;
            return this;
        }

        if (difference < -delta) {
            z = current - delta;
            return this;
        }

        z = goal;

        return this;
    }

    public Vector3 add(Vector3 r) {
        return new Vector3(x + r.getX(), y + r.getY(), z + r.getZ());
    }

    public Vector3 add(float r) {
        return new Vector3(x + r, y + r, z + r);
    }

    public Vector3 sub(Vector3 r) {
        return new Vector3(x - r.getX(), y - r.getY(), z - r.getZ());
    }

    public Vector3 sub(float r) {
        return new Vector3(x - r, y - r, z - r);
    }

    public Vector3 mul(Vector3 r) {
        return new Vector3(x * r.getX(), y * r.getY(), z * r.getZ());
    }

    public Vector3 mul(float r) {
        return new Vector3(x * r, y * r, z * r);
    }

    public Vector3 div(Vector3 r) {
        return new Vector3(x / r.getX(), y / r.getY(), z / r.getZ());
    }

    public Vector3 div(float r) {
        return new Vector3(x / r, y / r, z / r);
    }

    public Vector3 abs() {
        return new Vector3(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public Quaternion rotationBetween(Vector3 r) {
        Vector3 temp = this.cross(r);
        float w = (float) Math.sqrt((this.length() * r.length())) + this.dot(r);

        return new Quaternion(temp.getX(), temp.getY(), temp.getZ(), w);
    }

    public String toString() {
        return "(" + x + " " + y + " " + z + ")";
    }

    public Vector2 getXY() {
        return new Vector2(x, y);
    }

    public Vector2 getXZ() {
        return new Vector2(x, z);
    }

    public Vector2 getYZ() {
        return new Vector2(y, z);
    }

    public Vector2 getYX() {
        return new Vector2(y, x);
    }

    public Vector2 getZX() {
        return new Vector2(z, x);
    }

    public Vector2 getZY() {
        return new Vector2(z, y);
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

    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3 set(Vector3 v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();

        return this;
    }


    public boolean equals(Vector3 o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3 vector3 = (Vector3) o;

        if (Float.compare(vector3.x, x) != 0) return false;
        if (Float.compare(vector3.y, y) != 0) return false;
        if (Float.compare(vector3.z, z) != 0) return false;

        return true;
    }
}
