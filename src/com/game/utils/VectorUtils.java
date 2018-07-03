package com.game.utils;

import org.joml.Vector2d;

import java.util.ArrayList;

public class VectorUtils {

    /**
     *
     * @param a
     * @param b
     * @return proj b (a)
     */

    public static Vector2d getProjection(Vector2d a, Vector2d b) {

        double LHS = (a.x * b.x + a.y * b.y)/(b.x * b.x + b.y * b.y);

        return new Vector2d(LHS*b.x, LHS*b.y);

    }

    public static double getMagnitude(Vector2d a) {

        return Math.sqrt(Math.pow(a.x, 2) + Math.pow(a.y, 2));

    }

    /**
     *
     * @param normal the normal/axis to be projected onto
     * @param vectors the vectors to test
     * @return maximum projection vectors
     */
    public static Vector2d getMaxProjection(Vector2d normal, ArrayList<Vector2d> vectors) {

        Vector2d ret = null;

        double max = 0;

        for (var vector : vectors) {

            Vector2d projection = getProjection(vector, normal);

            double magnitude = getMagnitude(projection);

            if (magnitude > max) {

                max = magnitude;

                ret = projection;

            }

        }

        return ret;

    }

    public static boolean isSameDirection (Vector2d u, Vector2d v) {

        return u.x/v.x == u.y/v.y;

    }

}
