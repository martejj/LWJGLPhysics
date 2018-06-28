package com.game.world.objects;

import com.game.graphics.Renderer;
import com.game.world.World;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;

public class WorldObject {

    Vector2d position; // Of centre of object

    Vector2d velocity;

    Vector2d force;

    ArrayList<Vector2d> verticies;

    double rotation;

    double mass;

    double density;

    public static final double CALC_DENSITY = -1;

    public WorldObject(double mass, Vector2d position) {

        this.position = position;
        velocity = new Vector2d();
        force = new Vector2d();

        verticies = new ArrayList<>();

        this.mass = mass;

    }

    public void update(World world) {

    }

    public void render(Renderer renderer) {


    }

    public ArrayList<Vector2d> getPositionVertices() {

        ArrayList<Vector2d> ret = new ArrayList<>();

        for (var vertex : verticies) {

            ret.add(new Vector2d((vertex.x)*Math.cos(rotation) - (vertex.y)*Math.sin(rotation) + position.x,
                                 (vertex.x)*Math.sin(rotation) + (vertex.y)*Math.cos(rotation) + position.y));

        }

        return ret;

    }

    /**
     *
     * @param xMax the maximum left distance
     * @param xMin the minimum right distance
     * @param yMax the maximum height
     * @param yMin the minimum height e.g. bottom of the world
     * @return The point on the object where the collision will occur
     */

    public Vector2d getBoxCollisionVertex(double xMax, double xMin, double yMax, double yMin) {

        ArrayList<Vector2d> points = getPositionVertices();

        for (var point : points) {

            if (point.x < xMin || point.x > xMax) {

                return point;

            } else if (point.y < yMin || point.y > yMax) {

                return point;

            }

        }

        return null;

    }

}
