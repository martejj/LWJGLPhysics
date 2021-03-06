package com.game.world.objects;

import com.game.graphics.Colour;
import com.game.graphics.Renderer;
import com.game.utils.VectorUtils;
import com.game.world.World;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;

public class WorldObject {

    Vector2d position; // Of centre of object

    Vector2d velocity;

    Vector2d force;

    Vector2d acceleration;

    Colour colour;

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
        this.velocity = new Vector2d();
        this.force = new Vector2d();
        this.acceleration = new Vector2d();

        this.verticies = new ArrayList<>();

        this.colour = new Colour();

        this.mass = mass;

    }

    public void update(World world) {

        // TODO only change if object is edited since last update

        normals = null;

        vectors = null;

        positionVertices = null;

        vectorMesh = null;

        this.acceleration.x = force.x/mass;
        this.acceleration.y = force.y/mass;

        this.velocity.add(acceleration);

        this.position.add(velocity);

    }

    public void applyForce(Vector2d force) {

        this.force.add(force);

    }

    public void render(Renderer renderer) {


    }

    // TODO fix up
    public ArrayList<Vector2d> getVertices() {

        ArrayList<Vector2d> ret = new ArrayList<>(verticies.size());

        for (var vertex : verticies) {

            ret.add(new Vector2d((vertex.x)*Math.cos(rotation) - (vertex.y)*Math.sin(rotation),
                    (vertex.x)*Math.sin(rotation) + (vertex.y)*Math.cos(rotation)));

        }

        return ret;

    }

    public ArrayList<Vector2d> getPositionVertices() {

        if (this.positionVertices != null) {

            return this.positionVertices;

        }

        ArrayList<Vector2d> ret = new ArrayList<>(verticies.size());

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

        ArrayList<Vector2d> vectors = new ArrayList<>(verticies.size());

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
        vectors.add(new Vector2d(first.x - prev.x, first.y - prev.y));

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

        ArrayList<Vector2d> vectors = getCounterClockwiseVectors();

        ArrayList<Vector2d> normals = new ArrayList<>(vectors.size());

        for (var vector : vectors) {

            // Normal vector equals negative reciprocal. in this case we use -vector.x to get the right hand normal
            normals.add(new Vector2d(vector.y, -vector.x).normalize());

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

            for (var vertex2 : positionVertices.subList(i + 1, positionVertices.size())) {

                // Vector from 1 to 2 gives all of the counter clockwise vectors.
                vectorMesh.add(new Vector2d(vertex2.x - vertex1.x, vertex2.y - vertex1.y));

            }

            i++;

        }

        this.vectorMesh = vectorMesh;

        return vectorMesh;

    }

    public Vector2d getPosition() {

        return position;

    }

    public void move(double x, double y) {

        this.position.x = x;
        this.position.y = y;

    }

    public void rotate(double angle) {

        this.rotation = angle;

    }

}
