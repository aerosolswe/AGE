package com.theodore.aero.math;

import com.theodore.aero.physics.Plane;

public class Frustum {

    private static final int FRUSTUM_NEAR = 0;
    private static final int FRUSTUM_FAR = 1;
    private static final int FRUSTUM_LEFT = 2;
    private static final int FRUSTUM_RIGHT = 3;
    private static final int FRUSTUM_UP = 4;
    private static final int FRUSTUM_DOWN = 5;

    private Plane[] planes;

    public Frustum(Matrix4 m) {
        planes = new Plane[6];

        planes[FRUSTUM_RIGHT] = new Plane(new Vector3(m.get(0, 3) - m.get(0, 0), m.get(1, 3) - m.get(1, 0), m.get(2, 3) - m.get(2, 0)), m.get(3, 3) - m.get(3, 0));

        planes[FRUSTUM_LEFT] = new Plane(new Vector3(m.get(0, 3) - m.get(0, 0), m.get(1, 3) - m.get(1, 0), m.get(2, 3) - m.get(2, 0)), m.get(3, 3) - m.get(3, 0));

        planes[FRUSTUM_DOWN] = new Plane(new Vector3(m.get(0, 3) - m.get(0, 1), m.get(1, 3) - m.get(1, 1), m.get(2, 3) - m.get(2, 1)), m.get(3, 3) - m.get(3, 1));

        planes[FRUSTUM_UP] = new Plane(new Vector3(m.get(0, 3) - m.get(0, 1), m.get(1, 3) - m.get(1, 1), m.get(2, 3) - m.get(2, 1)), m.get(3, 3) - m.get(3, 1));

        planes[FRUSTUM_FAR] = new Plane(new Vector3(m.get(0, 3) - m.get(0, 2), m.get(1, 3) - m.get(1, 2), m.get(2, 3) - m.get(2, 2)), m.get(3, 3) - m.get(3, 2));

        planes[FRUSTUM_NEAR] = new Plane(new Vector3(m.get(0, 3) - m.get(0, 2), m.get(1, 3) - m.get(1, 2), m.get(2, 3) - m.get(2, 2)), m.get(3, 3) - m.get(3, 2));

        for (int i = 0; i < 6; i++)
            planes[i].normalized();
    }

    public boolean sphereIntersection(Vector3 center, float radius) {
        for (int i = 0; i < 6; i++) {
            if (center.dot(planes[i].getNormal()) + planes[i].getDistance() + radius <= 0)
                return false;
        }

        return true;
    }

}
