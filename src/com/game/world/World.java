package com.game.world;

import com.game.Game;
import com.game.graphics.Renderer;
import com.game.utils.VectorUtils;
import com.game.world.objects.Rectangle;
import com.game.world.objects.WorldObject;
import org.joml.Matrix4f;
import org.joml.Vector2d;

import java.util.ArrayList;

public class World {

    Matrix4f cameraPosition;

    ArrayList<WorldObject> objects;

    Rectangle rectangle;

    public World() {

        objects = new ArrayList<>();


    }

    public void load(Game game) {

        objects.add(new Rectangle(100, 50, new Vector2d(150, 150)));

    }

    public void update(Game game) {

        for (var object : objects) {

            object.update(this);

        }

    }

    public void render(Renderer renderer) {

        for (var object : objects) {

            object.render(renderer);

        }

    }

    // using Separating axis theory (SAT) from http://dyn4j.org/2010/01/sat/#sat-top
    public static boolean isColliding(WorldObject object1, WorldObject object2) {

        // These normals form the base axis that we want to project the shapes onto.

        ArrayList<Vector2d> object1Vectors = object1.getVectorMesh();

        ArrayList<Vector2d> object2Vectors = object2.getVectorMesh();

        ArrayList<Vector2d> object1Normals = object1.getRightHandNormals();

        ArrayList<Vector2d> object2Normals = object2.getRightHandNormals();

        for (var normal : object1Normals) {

            Vector2d max1 = VectorUtils.getMaxProjection(normal, object1Vectors);

            Vector2d max2 = VectorUtils.getMaxProjection(normal, object2Vectors);

            // TODO Check if max1 and max2 overlap -> if they dont they are colliding.
        }


        return true;

    }

}
