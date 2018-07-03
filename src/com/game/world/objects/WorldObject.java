package com.game.world.objects;

import com.game.graphics.Renderer;
import com.game.world.World;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Vector;

public class WorldObject {

    Vector2d position; // Of centre of object

    Vector2d velocity;

    Vector2d force;

    ArrayList<Vector2d> verticies;

    double rotation;

    double mass;

    double density;

    public static final double CALC_DENSITY = -1;

    // These should ALWAYS be stored in a counterclockwise manner
    private ArrayList<Vector2d> positionVertices = null;

    private ArrayList<Vector2d> vectors = null;

    private ArrayList<Vector2d> normals = null;

    private ArrayList<Vector2d> vectorMesh = null;

    public WorldObject(double mass, Vector2d position) {

        this.position = position;
        velocity = new Vector2d();
        force = new Vector2d();

        verticies = new ArrayList<>();

        this.mass = mass;

    }

    public void update(World world) {

        // TODO only change if object is edited since last update

        normals = null;

        vectors = null;

        positionVertices = null;

        vectorMesh = null;

    }

    public void render(Renderer renderer) {


    }

    /**
     *
     * @param xMax the maximum left distance
     * @param xMin the minimum right distance
     * @param yMax the maximum height
     * @param yMin the minimum height e.g. bottom of the world
     * @return The point on the object where the collision will occur
     */

    public Vector2d getBoxCollisionBoxVertex(double xMax, double xMin, double yMax, double yMin) {

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

    public ArrayList<Vector2d> getPositionVertices() {

        if (this.positionVertices != null) {

            return this.positionVertices;

        }

        ArrayList<Vector2d> ret = new ArrayList<>();

        for (var vertex : verticies) {

            ret.add(new Vector2d((vertex.x)*Math.cos(rotation) - (vertex.y)*Math.sin(rotation) + position.x,
                    (vertex.x)*Math.sin(rotation) + (vertex.y)*Math.cos(rotation) + position.y));

        }

        this.positionVertices = ret;

        return ret;

    }

    public ArrayList<Vector2d> getCounterClockwiseVectors() {

        if (this.vectors != null) {

            return this.vectors;

        }

        ArrayList<Vector2d> vertices = getPositionVertices();

        ArrayList<Vector2d> vectors = new ArrayList<>();

        Vector2d first = null;

        Vector2d prev = null;

        // This is possible as an ArrayList is stored sequentially from order of insertation.

        // Loop over every vertex and create a vector from the previous to the current.
        // Then increment previous and current.
        for (var curr : vertices) {

            if (prev != null) {

                vectors.add(new Vector2d(curr.x - prev.x, curr.y - prev.y));

            } else {

                first = curr;

            }

            prev = curr;

        }

        // Finally add the last vector
        vectors.add(new Vector2d(prev.x - first.x, prev.y - first.y));

        this.vectors = vectors;

        return vectors;

    }

    /**
     *
     * @return Very specifically returns the right hand normals.
     */
    public ArrayList<Vector2d> getRightHandNormals() {

        if (this.normals != null) {

            return this.normals;

        }

        ArrayList<Vector2d> normals = new ArrayList<>();

        ArrayList<Vector2d> vectors = getCounterClockwiseVectors();

        for (var vector : vectors) {

            // Normal vector equals negative reciprocal. in this case we use -vector.x to get the right hand normal
            normals.add(new Vector2d(vector.y, -vector.x));

        }

        this.normals = normals;

        return normals;

    }

    /**
     * @return all of the interior vectors of the object. Used for separating axis theorem collision detection.
     */
    public ArrayList<Vector2d> getVectorMesh() {

        if (this.vectorMesh != null) {

            return vectorMesh;

        }

        ArrayList<Vector2d> positionVertices = getPositionVertices();

        ArrayList<Vector2d> vectorMesh = new ArrayList<>();

        int i = 0;

        for (var vertex1 : positionVertices) {

            for (var vertex2 : positionVertices.subList(i, positionVertices.size())) {

                // Vector from 1 to 2 gives all of the counter clockwise vectors.
                vectorMesh.add(new Vector2d(vertex2.x - vertex1.x, vertex2.y - vertex1.y));

            }

            i++;

        }

        // Adding the final vector from the last vertex to the first.

        Vector2d last = positionVertices.get(positionVertices.size() - 1);

        Vector2d first = positionVertices.get(0);

        vectorMesh.add( new Vector2d(first.x - last.x, first.y - last.y));

        this.vectorMesh = vectorMesh;

        return vectorMesh;

    }

}
