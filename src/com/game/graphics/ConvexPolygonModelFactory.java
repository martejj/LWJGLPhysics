package com.game.graphics;

import com.game.utils.VectorUtils;
import org.joml.Vector2d;

import java.util.ArrayList;

public class ConvexPolygonModelFactory {

    /**
     * Only useful for irregular polygons or regular polygons w/ vertices > 4
     * @param vectors  vectors of the vertices relative to the first one placed (assumed to be origin, (0, 0)).
     *                 This is important as it is also the axis of rotation.
     *                 Do not include (0, 0).
     * @return the openGL model of the polygon
     */

    public static Model makeModel(ArrayList<Vector2d> vectors) {

        // number of vectors is every edge and then +1 as we need one for the centre of the polygon (0, 0)
        float[] vertices = new float[vectors.size()*3 + 3];

        // Find the maximum vertex so we can normalise the remainder of the vectors to it.
        // We need them to be on a scale of 0 to 1.
        ArrayList<Vector2d> normalisedVectors = VectorUtils.normaliseToMax(vectors);

        if (normalisedVectors == null || !isConvex(vectors)) {

            return null;

        }

        // Put the vectors into the float array in the format of OpenGL

        vertices[0] = 0.0f;
        vertices[1] = 0.0f;
        vertices[2] = 0.0f;

        int index = 3; // as we just added the origin at index 0.

        for (var normalisedVertex : normalisedVectors) {

            // This data represents the index'th vertex in memory.
            vertices[index] = (float) normalisedVertex.x;
            vertices[index + 1] = (float) normalisedVertex.y;
            vertices[index + 2] = 0; // 2D model so z is 0.

            index += 3; // move to next vertex index

        }

        // Describes the index positions of the vertices of the triangles that make up the polygon.
        // Each means there will be a new triangle which in turn has 3 vertices (so it needs 3 indices.
        int[] indices = new int[vectors.size() * 3]; // size*3 as there will be that many triangles

        for (int curr = 1; curr < vectors.size(); curr++) {

            // form a triangle between the vertices described in the float array vertices
            indices[curr] = curr; // Starts at +1 as vertices[0] is (0, 0).

            if (curr + 1 > vectors.size()) {

                indices[curr*3 + 1] = 1; // Wrap back around

            } else {

                indices[curr*3 + 1] = curr + 1;

            }

            indices[curr*3 + 2] = 0; // always zero as the triangle connects to the origin (which is stored at index 0)

        }

        return new Model(vertices, indices);

    }

    // TODO
    public static boolean isConvex(ArrayList<Vector2d> vertices) {

        return true;

    }

}
