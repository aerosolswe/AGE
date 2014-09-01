package com.theodore.aero.math;


public class Vector2 {
    private float x;
    private float y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 r) {
        this.x = r.getX();
        this.y = r.getY();
    }

    public Vector2(Vector3 r) {
        this.x = r.getX();
        this.y = r.getY();
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float distance(Vector2 r) {
        return this.sub(r).length();
    }

    public float angleBetween(Vector2 r) {
        float cosTheta = this.dot(r) / (this.length() * r.length());

        return (float) Math.acos(cosTheta);
    }

    public float dot(Vector2 r) {
        return x * r.getX() + y * r.getY();
    }

    public float cross(Vector2 r) {
        return x * r.getY() - y * r.getX();
    }

    public Vector2 normalized() {
        float length = length();

        return new Vector2(x / length, y / length);
    }

    public Vector2 min(Vector2 r) {
        return new Vector2(Math.min(x, r.getX()), Math.min(y, r.getY()));
    }

    public Vector2 max(Vector2 r) {
        return new Vector2(Math.max(x, r.getX()), Math.max(y, r.getY()));
    }

    public Vector2 clamp(float maxLength) {
        if (lengthSquared() <= maxLength * maxLength)
            return this;

        return this.normalized().mul(maxLength);
    }

    public Vector2 towards(Vector2 r, float amt) {
        Vector2 result = this.sub(r);

        if (result.length() < amt)
            return result;

        return result.normalized().mul(amt);
    }

    public Vector2 rotate(float angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vector2((float) (x * cos - y * sin), (float) (x * sin + y * cos));
    }

    public Vector2 add(Vector2 r) {
        return new Vector2(x + r.getX(), y + r.getY());
    }

    public Vector2 add(float r) {
        return new Vector2(x + r, y + r);
    }

    public Vector2 sub(Vector2 r) {
        return new Vector2(x - r.getX(), y - r.getY());
    }

    public Vector2 sub(float r) {
        return new Vector2(x - r, y - r);
    }

    public Vector2 mul(Vector2 r) {
        return new Vector2(x * r.getX(), y * r.getY());
    }

    public Vector2 mul(float r) {
        return new Vector2(x * r, y * r);
    }

    public Vector2 div(Vector2 r) {
        return new Vector2(x / r.getX(), y / r.getY());
    }

    public Vector2 div(float r) {
        return new Vector2(x / r, y / r);
    }

    public Vector2 abs() {
        return new Vector2(Math.abs(x), Math.abs(y));
    }

    public Vector2 lerp(Vector2 newVector, float amt) {
        return this.sub(newVector).mul(amt).add(newVector);
    }

    public String toString() {
        return "(" + x + " " + y + ")";
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

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;

        return this;
    }

    public Vector2 set(Vector2 v) {
        this.set(v.getX(), v.getY());

        return this;
    }

    public boolean equals(Vector2 o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;

        if (Float.compare(vector2.x, x) != 0) return false;
        if (Float.compare(vector2.y, y) != 0) return false;

        return true;
    }
}
