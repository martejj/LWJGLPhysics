package com.game.graphics;

import com.game.utils.VectorUtils;
import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;

public class LineCollectionModelFactory {

    public static Model makeModel(ArrayList<Vector2d> vectors, boolean wrap) {

        // number of vertices is number of vectors*3 (3D)
        float[] vertices = new float[vectors.size()*3];

        if (vectors == null) {

            return null;

        }

        int index = 0;

        for (var vector : vectors) {

            // This data represents the index'th vertex in memory.
            vertices[index] = (float) vector.x;
            vertices[index + 1] = (float) vector.y;
            vertices[index + 2] = 0; // 2D model so z is 0.

            index += 3; // move to next vertex index

        }

        // Describes the index positions of the vertices of the lines

        int num;

        if (wrap) {

            num = vectors.size();

        } else {

            num = vectors.size() - 1;

        }

        int[] indices = new int[num * 2]; // size*2 as each line is connected by 2 vertices


        for (int curr = 0; curr < vectors.size() - 1; curr++) {

            // form a triangle between the vertices described in the float array vertices
            indices[curr*2] = curr; // Starts at +1 as vertices[0] is (0, 0).

            //System.out.print(curr + " ");

            indices[curr*2 + 1] = curr + 1;

        }

        // If we want it to wrap to the beginning then add the final array of indices from the start to the end.
        if (wrap) {

            indices[indices.length - 2] = vectors.size() - 1;
            indices[indices.length - 1] = 0;

        }

        System.out.println(Arrays.toString(vertices));
        System.out.println(Arrays.toString(indices));

        return new Model(vertices, indices, GL11.GL_LINE_STRIP);

    }

}
