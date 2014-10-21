package com.theodore.aero.graphics.mesh;

import com.theodore.aero.graphics.mesh.meshLoading.IndexedModel;
import com.theodore.aero.math.Vector2;
import com.theodore.aero.math.Vector3;
import org.lwjgl.util.glu.Sphere;

public class Primitives {

    public static IndexedModel rectangle() {
        IndexedModel model = new IndexedModel();

        model.addVertex(new Vector3(-1f, -1f, 0));
        model.addTexCoord(new Vector2(0, 1));
        model.addVertex(new Vector3(-1f, 1f, 0));
        model.addTexCoord(new Vector2(0, 0));
        model.addVertex(new Vector3(1f, 1f, 0));
        model.addTexCoord(new Vector2(1, 0));
        model.addVertex(new Vector3(1f, -1f, 0));
        model.addTexCoord(new Vector2(1, 1));
        model.addFace(0, 1, 2);
        model.addFace(0, 2, 3);

        return model.finish();
    }

    public static IndexedModel plane() {
        IndexedModel model = new IndexedModel();

        model.addVertex(new Vector3(-1, 0.0f, -1));
        model.addTexCoord(new Vector2(0.0f, 0.0f));
        model.addVertex(new Vector3(-1, 0.0f, 1));
        model.addTexCoord(new Vector2(0.0f, 1.0f));
        model.addVertex(new Vector3(1, 0.0f, -1));
        model.addTexCoord(new Vector2(1.0f, 0.0f));
        model.addVertex(new Vector3(1, 0.0f, 1));
        model.addTexCoord(new Vector2(1.0f, 1.0f));
        model.addFace(0, 1, 2);
        model.addFace(2, 1, 3);

        return model.finish();
    }

    public static IndexedModel dome(float radius, float yOffset){
        IndexedModel model = new IndexedModel();

        // Marco - skydome generator that only creates what is necessary
        // Radius = PlanetRadius + 200000
        // Y offset is PlanetRadius
        // Don't need anything below the horizon (i.e. Y < 0)


        int rings = 160;
        int segments = 20;
        // set vertex count and index count

        float deltaRingAngle = ((float)Math.PI / rings);
        float deltaSegAngle = (2.0f * (float)Math.PI / segments);

        int verticeIndex = 0 ;
        // Generate the group of rings for the sphere

        for( int ring = 0; ring < rings + 1 ; ring++ ){
            float r0 = (float)Math.sin(ring * deltaRingAngle);
            float y0 = (float)Math.cos(ring * deltaRingAngle);

            // Generate the group of segments for the current ring

            for(int seg = 0; seg < segments + 1 ; seg++){
                float x0 = r0 * (float)Math.sin(seg * deltaSegAngle);
                float z0 = r0 * (float)Math.cos(seg * deltaSegAngle);

                // Add one vertices to the strip which makes up the sphere

                model.addVertex(new Vector3(x0 * radius, y0 * radius - yOffset, z0 * radius));
                // add two indices except for last ring

                if (ring != rings){
                    model.addFace(verticeIndex  + (segments + 1), verticeIndex, verticeIndex + (segments + 1));
                    verticeIndex++;
                }

            }
        }

        return model.finish();
    }

    public static IndexedModel cube() {
        IndexedModel model = new IndexedModel();

        model.addVertex(new Vector3(-0.5f, -0.5f, -0.5f));
        model.addTexCoord(new Vector2(0, 1));
        model.addVertex(new Vector3(-0.5f, 0.5f, -0.5f));
        model.addTexCoord(new Vector2(0, 0));
        model.addVertex(new Vector3(0.5f, 0.5f, -0.5f));
        model.addTexCoord(new Vector2(1, 0));
        model.addVertex(new Vector3(0.5f, -0.5f, -0.5f));
        model.addTexCoord(new Vector2(1, 1));
        model.addVertex(new Vector3(-0.5f, -0.5f, -0.5f));
        model.addTexCoord(new Vector2(1, 0));
        model.addVertex(new Vector3(0.5f, -0.5f, -0.5f));
        model.addTexCoord(new Vector2(0, 0));
        model.addVertex(new Vector3(0.5f, -0.5f, 0.5f));
        model.addTexCoord(new Vector2(0, 1));
        model.addVertex(new Vector3(-0.5f, -0.5f, 0.5f));
        model.addTexCoord(new Vector2(1, 1));
        model.addVertex(new Vector3(-0.5f, -0.5f, -0.5f));
        model.addTexCoord(new Vector2(1, 1));
        model.addVertex(new Vector3(-0.5f, -0.5f, 0.5f));
        model.addTexCoord(new Vector2(0, 1));
        model.addVertex(new Vector3(-0.5f, 0.5f, 0.5f));
        model.addTexCoord(new Vector2(0, 0));
        model.addVertex(new Vector3(-0.5f, 0.5f, -0.5f));
        model.addTexCoord(new Vector2(1, 0));
        model.addVertex(new Vector3(-0.5f, -0.5f, 0.5f));
        model.addTexCoord(new Vector2(1, 1));
        model.addVertex(new Vector3(0.5f, -0.5f, 0.5f));
        model.addTexCoord(new Vector2(0, 1));
        model.addVertex(new Vector3(0.5f, 0.5f, 0.5f));
        model.addTexCoord(new Vector2(0, 0));
        model.addVertex(new Vector3(-0.5f, 0.5f, 0.5f));
        model.addTexCoord(new Vector2(1, 0));
        model.addVertex(new Vector3(-0.5f, 0.5f, -0.5f));
        model.addTexCoord(new Vector2(0, 1));
        model.addVertex(new Vector3(-0.5f, 0.5f, 0.5f));
        model.addTexCoord(new Vector2(0, 0));
        model.addVertex(new Vector3(0.5f, 0.5f, 0.5f));
        model.addTexCoord(new Vector2(1, 0));
        model.addVertex(new Vector3(0.5f, 0.5f, -0.5f));
        model.addTexCoord(new Vector2(1, 1));
        model.addVertex(new Vector3(0.5f, -0.5f, -0.5f));
        model.addTexCoord(new Vector2(0, 1));
        model.addVertex(new Vector3(0.5f, 0.5f, -0.5f));
        model.addTexCoord(new Vector2(0, 0));
        model.addVertex(new Vector3(0.5f, 0.5f, 0.5f));
        model.addTexCoord(new Vector2(1, 0));
        model.addVertex(new Vector3(0.5f, -0.5f, 0.5f));
        model.addTexCoord(new Vector2(1, 1));
        model.addFace(0, 1, 2);
        model.addFace(0, 2, 3);
        model.addFace(4, 5, 6);
        model.addFace(4, 6, 7);
        model.addFace(8, 9, 10);
        model.addFace(8, 10, 11);
        model.addFace(12, 13, 14);
        model.addFace(12, 14, 15);
        model.addFace(16, 17, 18);
        model.addFace(16, 18, 19);
        model.addFace(20, 21, 22);
        model.addFace(20, 22, 23);

        return model.finish();
    }
}
