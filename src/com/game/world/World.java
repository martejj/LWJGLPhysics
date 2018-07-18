package com.game.world;

import com.game.Game;
import com.game.graphics.Colour;
import com.game.graphics.LineCollectionModelFactory;
import com.game.graphics.Model;
import com.game.graphics.Renderer;
import com.game.states.ui.input.PolygonCreator;
import com.game.utils.VectorUtils;
import com.game.world.objects.Polygon;
import com.game.world.objects.Rectangle;
import com.game.world.objects.WorldObject;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2dc;

import java.util.ArrayList;

public class World {

    Matrix4f cameraPosition;

    ArrayList<WorldObject> objects;

    Rectangle rectangle;

    Model line;

    PolygonCreator creator;

    public World() {

        objects = new ArrayList<>();


    }

    public void load(Game game) {

        // Rectangle Test
        objects.add(new Rectangle(100, 50, new Vector2d(150, 150)));

        // Square polygon Test
        ArrayList<Vector2d> vectors = new ArrayList<>(4);
        vectors.add(new Vector2d(-1,  1));
        vectors.add(new Vector2d(-1, -1));
        vectors.add(new Vector2d(1, -1));
        vectors.add(new Vector2d(1,  1));

        objects.add(new Polygon(new Vector2d(100, 100),10, new Vector2d(300, 500), vectors));

        // Pentagon polygon Test
        ArrayList<Vector2d> vectors2 = new ArrayList<>(5);
        vectors2.add(VectorUtils.getVectorFromAngle(2*Math.PI*(1)/5));
        vectors2.add(VectorUtils.getVectorFromAngle(2*Math.PI*(2)/5));
        vectors2.add(VectorUtils.getVectorFromAngle(2*Math.PI*(3)/5));
        vectors2.add(VectorUtils.getVectorFromAngle(2*Math.PI*(4)/5));
        vectors2.add(VectorUtils.getVectorFromAngle(2*Math.PI*(5)/5));

        objects.add(new Polygon(new Vector2d(100, 100), 10, new Vector2d(800, 400), vectors2));

        // Line collection Test
        ArrayList<Vector2d> vectors3 = new ArrayList<>();
        vectors3.add(new Vector2d(100, 100));
        vectors3.add(new Vector2d(200, 300));
        vectors3.add(new Vector2d(200, 100));

        line = LineCollectionModelFactory.makeModel(vectors3, true);

        creator = new PolygonCreator();

        creator.init(game);

    }

    public void update(Game game) {

        for (var object : objects) {

            object.update(this);

        }

        if (creator.isFinished()) {

            ArrayList<Vector2d> vertices = creator.stop(game);

            objects.add(new Polygon(new Vector2d(100, 100), 10, VectorUtils.getCentre(vertices), vertices));

        }

    }

    public void render(Renderer renderer) {

        for (var object : objects) {

            object.render(renderer);

        }

        renderer.drawModel(new Colour(0, 1, 0), line);

        creator.render(renderer);

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
