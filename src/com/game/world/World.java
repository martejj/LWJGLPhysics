package com.game.world;

import com.game.Game;
import com.game.graphics.Renderer;
import com.game.world.objects.Rectangle;
import com.game.world.objects.WorldObject;
import org.joml.Matrix4f;
import org.joml.Vector2d;

import java.lang.reflect.Array;
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

            // needs to be done just before velocity is updated
            Vector2d collision = object.getBoxCollisionVertex(game.getInitialWidth(), 0, game.getInitialWidth(), 0);

            if (collision != null) {



            }

            object.update(this);

        }

    }

    public void render(Renderer renderer) {

        for (var object : objects) {

            object.render(renderer);

        }

    }

    public static boolean isColliding(WorldObject object1, WorldObject object2) {

        ArrayList<Vector2d> object1Vertices = object1.getPositionVertices();

        ArrayList<Vector2d> object2Vertices = object2.getPositionVertices();


        return true;

    }

}
