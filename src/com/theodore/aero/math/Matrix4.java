package com.theodore.aero.math;

public class Matrix4 {

    public float[][] m;
    public float[][] tmp;

    public Matrix4() {
        m = new float[4][4];
        tmp = new float[4][4];
    }

    public Matrix4 initIdentity() {
        m[0][0] = 1;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = 1;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 1;
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initTranslation(float x, float y, float z) {
        m[0][0] = 1;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = x;
        m[1][0] = 0;
        m[1][1] = 1;
        m[1][2] = 0;
        m[1][3] = y;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 1;
        m[2][3] = z;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initRotation(float x, float y, float z) {
        Matrix4 rx = new Matrix4();
        Matrix4 ry = new Matrix4();
        Matrix4 rz = new Matrix4();

        x = (float) Math.toRadians(x);
        y = (float) Math.toRadians(y);
        z = (float) Math.toRadians(z);

        rz.m[0][0] = (float) Math.cos(z);
        rz.m[0][1] = -(float) Math.sin(z);
        rz.m[0][2] = 0;
        rz.m[0][3] = 0;
        rz.m[1][0] = (float) Math.sin(z);
        rz.m[1][1] = (float) Math.cos(z);
        rz.m[1][2] = 0;
        rz.m[1][3] = 0;
        rz.m[2][0] = 0;
        rz.m[2][1] = 0;
        rz.m[2][2] = 1;
        rz.m[2][3] = 0;
        rz.m[3][0] = 0;
        rz.m[3][1] = 0;
        rz.m[3][2] = 0;
        rz.m[3][3] = 1;

        rx.m[0][0] = 1;
        rx.m[0][1] = 0;
        rx.m[0][2] = 0;
        rx.m[0][3] = 0;
        rx.m[1][0] = 0;
        rx.m[1][1] = (float) Math.cos(x);
        rx.m[1][2] = -(float) Math.sin(x);
        rx.m[1][3] = 0;
        rx.m[2][0] = 0;
        rx.m[2][1] = (float) Math.sin(x);
        rx.m[2][2] = (float) Math.cos(x);
        rx.m[2][3] = 0;
        rx.m[3][0] = 0;
        rx.m[3][1] = 0;
        rx.m[3][2] = 0;
        rx.m[3][3] = 1;

        ry.m[0][0] = (float) Math.cos(y);
        ry.m[0][1] = 0;
        ry.m[0][2] = -(float) Math.sin(y);
        ry.m[0][3] = 0;
        ry.m[1][0] = 0;
        ry.m[1][1] = 1;
        ry.m[1][2] = 0;
        ry.m[1][3] = 0;
        ry.m[2][0] = (float) Math.sin(y);
        ry.m[2][1] = 0;
        ry.m[2][2] = (float) Math.cos(y);
        ry.m[2][3] = 0;
        ry.m[3][0] = 0;
        ry.m[3][1] = 0;
        ry.m[3][2] = 0;
        ry.m[3][3] = 1;

        m = rz.mul(ry.mul(rx)).getMatrix();

        return this;
    }

    public Matrix4 initScale(float x, float y, float z) {
        m[0][0] = x;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = y;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = z;
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
        float tanHalfFOV = (float) Math.tan(fov / 2);
        float zRange = zNear - zFar;

        m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = 1.0f / tanHalfFOV;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = (-zNear - zFar) / zRange;
        m[2][3] = 2 * zFar * zNear / zRange;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 1;
        m[3][3] = 0;


        return this;
    }

    public Matrix4 initOrthographic(float left, float right, float bottom, float top, float near, float far) {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        m[0][0] = 2 / width;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = -(right + left) / width;
        m[1][0] = 0;
        m[1][1] = 2 / height;
        m[1][2] = 0;
        m[1][3] = -(top + bottom) / height;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = -2 / depth;
        m[2][3] = -(far + near) / depth;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initRotation(Vector3 forward, Vector3 up) {
        Vector3 f = forward.normalized();

        Vector3 r = up.normalized();
        r = r.cross(f);

        Vector3 u = f.cross(r);

        return initRotation(f, u, r);
    }

    public Matrix4 initRotation(Vector3 forward, Vector3 up, Vector3 right) {
        Vector3 f = forward;
        Vector3 r = right;
        Vector3 u = up;

        m[0][0] = r.getX();
        m[0][1] = r.getY();
        m[0][2] = r.getZ();
        m[0][3] = 0;
        m[1][0] = u.getX();
        m[1][1] = u.getY();
        m[1][2] = u.getZ();
        m[1][3] = 0;
        m[2][0] = f.getX();
        m[2][1] = f.getY();
        m[2][2] = f.getZ();
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Vector3 transform(Vector3 r) {
        return new Vector3(
                m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3],
                m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3],
                m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3]);
    }

    public Matrix4 fromAngleNormalAxis(Vector3 axis, float angle) {
        initIdentity();
        m[3][3] = 1;

        float fCos = (float) Math.cos(angle);
        float fSin = (float) Math.sin(angle);
        float fOneMinusCos = ((float) 1.0) - fCos;
        float fX2 = axis.x * axis.x;
        float fY2 = axis.y * axis.y;
        float fZ2 = axis.z * axis.z;
        float fXYM = axis.x * axis.y * fOneMinusCos;
        float fXZM = axis.x * axis.z * fOneMinusCos;
        float fYZM = axis.y * axis.z * fOneMinusCos;
        float fXSin = axis.x * fSin;
        float fYSin = axis.y * fSin;
        float fZSin = axis.z * fSin;

        m[0][0] = fX2 * fOneMinusCos + fCos;
        m[0][1] = fXYM - fZSin;
        m[0][2] = fXZM + fYSin;
        m[1][0] = fXYM + fZSin;
        m[1][1] = fY2 * fOneMinusCos + fCos;
        m[1][2] = fYZM - fXSin;
        m[2][0] = fXZM - fYSin;
        m[2][1] = fYZM + fXSin;
        m[2][2] = fZ2 * fOneMinusCos + fCos;

        return this;
    }

    public Quaternion toQuaternion() {
        float trace = m[0][0] + m[1][1] + m[2][2];

        float w = 1;
        float x = 0;
        float y = 0;
        float z = 0;
        if (trace > 0) {
            float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
            w = 0.25f / s;
            x = (m[1][2] - m[2][1]) * s;
            y = (m[2][0] - m[0][2]) * s;
            z = (m[0][1] - m[1][0]) * s;
        } else {
            if (m[0][0] > m[1][1] && m[0][0] > m[2][2]) {
                float s = 2.0f * (float) Math.sqrt(1.0f + m[0][0] - m[1][1] - m[2][2]);
                w = (m[1][2] - m[2][1]) / s;
                x = 0.25f * s;
                y = (m[1][0] + m[0][1]) / s;
                z = (m[2][0] + m[0][2]) / s;
            } else if (m[1][1] > m[2][2]) {
                float s = 2.0f * (float) Math.sqrt(1.0f + m[1][1] - m[0][0] - m[2][2]);
                w = (m[2][0] - m[0][2]) / s;
                x = (m[1][0] + m[0][1]) / s;
                y = 0.25f * s;
                z = (m[2][1] + m[1][2]) / s;
            } else {
                float s = 2.0f * (float) Math.sqrt(1.0f + m[2][2] - m[0][0] - m[1][1]);
                w = (m[0][1] - m[1][0]) / s;
                x = (m[2][0] + m[0][2]) / s;
                y = (m[1][2] + m[2][1]) / s;
                z = 0.25f * s;
            }
        }

        return new Quaternion(x, y, z, w);
    }

    public Matrix4 inv() {
        float l_det =
                m[3][0] * m[2][1] * m[1][2] * m[0][3] - m[2][0] * m[3][1] * m[1][2] * m[0][3] - m[3][0] * m[1][1]
                        * m[2][2] * m[0][3] + m[1][0] * m[3][1] * m[2][2] * m[0][3] + m[2][0] * m[1][1] * m[3][2] * m[0][3] - m[1][0]
                        * m[2][1] * m[3][2] * m[0][3] - m[3][0] * m[2][1] * m[0][2] * m[1][3] + m[2][0] * m[3][1] * m[0][2] * m[1][3]
                        + m[3][0] * m[0][1] * m[2][2] * m[1][3] - m[0][0] * m[3][1] * m[2][2] * m[1][3] - m[2][0] * m[0][1] * m[3][2]
                        * m[1][3] + m[0][0] * m[2][1] * m[3][2] * m[1][3] + m[3][0] * m[1][1] * m[0][2] * m[2][3] - m[1][0] * m[3][1]
                        * m[0][2] * m[2][3] - m[3][0] * m[0][1] * m[1][2] * m[2][3] + m[0][0] * m[3][1] * m[1][2] * m[2][3] + m[1][0]
                        * m[0][1] * m[3][2] * m[2][3] - m[0][0] * m[1][1] * m[3][2] * m[2][3] - m[2][0] * m[1][1] * m[0][2] * m[3][3]
                        + m[1][0] * m[2][1] * m[0][2] * m[3][3] + m[2][0] * m[0][1] * m[1][2] * m[3][3] - m[0][0] * m[2][1] * m[1][2]
                        * m[3][3] - m[1][0] * m[0][1] * m[2][2] * m[3][3] + m[0][0] * m[1][1] * m[2][2] * m[3][3];
        if (l_det == 0f) throw new RuntimeException("non-invertible matrix");
        float inv_det = 1.0f / l_det;
        tmp[0][0] = m[1][2] * m[2][3] * m[3][1] - m[1][3] * m[2][2] * m[3][1] + m[1][3] * m[2][1] * m[3][2] - m[1][1]
                * m[2][3] * m[3][2] - m[1][2] * m[2][1] * m[3][3] + m[1][1] * m[2][2] * m[3][3];
        tmp[0][1] = m[0][3] * m[2][2] * m[3][1] - m[0][2] * m[2][3] * m[3][1] - m[0][3] * m[2][1] * m[3][2] + m[0][1]
                * m[2][3] * m[3][2] + m[0][2] * m[2][1] * m[3][3] - m[0][1] * m[2][2] * m[3][3];
        tmp[0][2] = m[0][2] * m[1][3] * m[3][1] - m[0][3] * m[1][2] * m[3][1] + m[0][3] * m[1][1] * m[3][2] - m[0][1]
                * m[1][3] * m[3][2] - m[0][2] * m[1][1] * m[3][3] + m[0][1] * m[1][2] * m[3][3];
        tmp[0][3] = m[0][3] * m[1][2] * m[2][1] - m[0][2] * m[1][3] * m[2][1] - m[0][3] * m[1][1] * m[2][2] + m[0][1]
                * m[1][3] * m[2][2] + m[0][2] * m[1][1] * m[2][3] - m[0][1] * m[1][2] * m[2][3];
        tmp[1][0] = m[1][3] * m[2][2] * m[3][0] - m[1][2] * m[2][3] * m[3][0] - m[1][3] * m[2][0] * m[3][2] + m[1][0]
                * m[2][3] * m[3][2] + m[1][2] * m[2][0] * m[3][3] - m[1][0] * m[2][2] * m[3][3];
        tmp[1][1] = m[0][2] * m[2][3] * m[3][0] - m[0][3] * m[2][2] * m[3][0] + m[0][3] * m[2][0] * m[3][2] - m[0][0]
                * m[2][3] * m[3][2] - m[0][2] * m[2][0] * m[3][3] + m[0][0] * m[2][2] * m[3][3];
        tmp[1][2] = m[0][3] * m[1][2] * m[3][0] - m[0][2] * m[1][3] * m[3][0] - m[0][3] * m[1][0] * m[3][2] + m[0][0]
                * m[1][3] * m[3][2] + m[0][2] * m[1][0] * m[3][3] - m[0][0] * m[1][2] * m[3][3];
        tmp[1][3] = m[0][2] * m[1][3] * m[2][0] - m[0][3] * m[1][2] * m[2][0] + m[0][3] * m[1][0] * m[2][2] - m[0][0]
                * m[1][3] * m[2][2] - m[0][2] * m[1][0] * m[2][3] + m[0][0] * m[1][2] * m[2][3];
        tmp[2][0] = m[1][1] * m[2][3] * m[3][0] - m[1][3] * m[2][1] * m[3][0] + m[1][3] * m[2][0] * m[3][1] - m[1][0]
                * m[2][3] * m[3][1] - m[1][1] * m[2][0] * m[3][3] + m[1][0] * m[2][1] * m[3][3];
        tmp[2][1] = m[0][3] * m[2][1] * m[3][0] - m[0][1] * m[2][3] * m[3][0] - m[0][3] * m[2][0] * m[3][1] + m[0][0]
                * m[2][3] * m[3][1] + m[0][1] * m[2][0] * m[3][3] - m[0][0] * m[2][1] * m[3][3];
        tmp[2][2] = m[0][1] * m[1][3] * m[3][0] - m[0][3] * m[1][1] * m[3][0] + m[0][3] * m[1][0] * m[3][1] - m[0][0]
                * m[1][3] * m[3][1] - m[0][1] * m[1][0] * m[3][3] + m[0][0] * m[1][1] * m[3][3];
        tmp[2][3] = m[0][3] * m[1][1] * m[2][0] - m[0][1] * m[1][3] * m[2][0] - m[0][3] * m[1][0] * m[2][1] + m[0][0]
                * m[1][3] * m[2][1] + m[0][1] * m[1][0] * m[2][3] - m[0][0] * m[1][1] * m[2][3];
        tmp[3][0] = m[1][2] * m[2][1] * m[3][0] - m[1][1] * m[2][2] * m[3][0] - m[1][2] * m[2][0] * m[3][1] + m[1][0]
                * m[2][2] * m[3][1] + m[1][1] * m[2][0] * m[3][2] - m[1][0] * m[2][1] * m[3][2];
        tmp[3][1] = m[0][1] * m[2][2] * m[3][0] - m[0][2] * m[2][1] * m[3][0] + m[0][2] * m[2][0] * m[3][1] - m[0][0]
                * m[2][2] * m[3][1] - m[0][1] * m[2][0] * m[3][2] + m[0][0] * m[2][1] * m[3][2];
        tmp[3][2] = m[0][2] * m[1][1] * m[3][0] - m[0][1] * m[1][2] * m[3][0] - m[0][2] * m[1][0] * m[3][1] + m[0][0]
                * m[1][2] * m[3][1] + m[0][1] * m[1][0] * m[3][2] - m[0][0] * m[1][1] * m[3][2];
        tmp[3][3] = m[0][1] * m[1][2] * m[2][0] - m[0][2] * m[1][1] * m[2][0] + m[0][2] * m[1][0] * m[2][1] - m[0][0]
                * m[1][2] * m[2][1] - m[0][1] * m[1][0] * m[2][2] + m[0][0] * m[1][1] * m[2][2];
        m[0][0] = tmp[0][0] * inv_det;
        m[0][1] = tmp[0][1] * inv_det;
        m[0][2] = tmp[0][2] * inv_det;
        m[0][3] = tmp[0][3] * inv_det;
        m[1][0] = tmp[1][0] * inv_det;
        m[1][1] = tmp[1][1] * inv_det;
        m[1][2] = tmp[1][2] * inv_det;
        m[1][3] = tmp[1][3] * inv_det;
        m[2][0] = tmp[2][0] * inv_det;
        m[2][1] = tmp[2][1] * inv_det;
        m[2][2] = tmp[2][2] * inv_det;
        m[2][3] = tmp[2][3] * inv_det;
        m[3][0] = tmp[3][0] * inv_det;
        m[3][1] = tmp[3][1] * inv_det;
        m[3][2] = tmp[3][2] * inv_det;
        m[3][3] = tmp[3][3] * inv_det;
        return this;
    }

    public Matrix4 mul(Matrix4 r) {
        Matrix4 res = new Matrix4();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                res.set(i, j, m[i][0] * r.get(0, j) +
                        m[i][1] * r.get(1, j) +
                        m[i][2] * r.get(2, j) +
                        m[i][3] * r.get(3, j));
            }
        }

        return res;
    }


    public Vector3 mul(Vector3 v) {

        Vector3 result = new Vector3();

        float vx = v.x, vy = v.y, vz = v.z;
        result.x = m[0][0] * vx + m[0][1] * vy + m[0][2] * vz + m[0][3];
        result.y = m[1][0] * vx + m[1][1] * vy + m[1][2] * vz + m[1][3];
        result.z = m[2][0] * vx + m[2][1] * vy + m[2][2] * vz + m[2][3];

        return result;
    }

    public float[][] getMatrix() {
        float[][] res = new float[4][4];

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                res[i][j] = m[i][j];

        return res;
    }

    public float get(int x, int y) {
        return m[x][y];
    }

    public void setM(float[][] m) {
        this.m = m;
    }

    public void set(int x, int y, float mue) {
        m[x][y] = mue;
    }

}
