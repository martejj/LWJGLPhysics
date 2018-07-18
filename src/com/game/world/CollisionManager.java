package com.game.world;

import com.game.world.objects.WorldObject;
import org.joml.Vector2d;

import java.util.ArrayList;

public class CollisionManager {

    /**
     *
     * @param xMax the maximum left distance
     * @param xMin the minimum right distance
     * @param yMax the maximum height
     * @param yMin the minimum height e.g. bottom of the world
     * @return The point on the object where the collision will occur
     */

    public Vector2d getAABBCollisionPoint(double xMax, double xMin, double yMax, double yMin, WorldObject object) {

        ArrayList<Vector2d> points = object.getPositionVertices();

        for (var point : points) {

            if (point.x < xMin || point.x > xMax) {

                return new Vector2d(point);

            } else if (point.y < yMin || point.y > yMax) {

                return new Vector2d(point);

            }

        }

        return null;

    }

    public boolean isAABBCollision(double xMax, double xMin, double yMax, double yMin, WorldObject object) {

        ArrayList<Vector2d> points = object.getPositionVertices();

        for (var point : points) {

            if (point.x < xMin || point.x > xMax) {

                return true;

            } else if (point.y < yMin || point.y > yMax) {

                return true;

            }

        }

        return false;
    }

    /**
     * @return true if (x1, y1) is within or on the circle of radius radius centred at (x2, y2)
     *
     */
    public static boolean isWithinRadiusOf(double x1, double y1, double radius, double x2, double y2) {

        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        return distance <= radius;

    }

}
