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
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import static java.lang.Math.PI;

public class World {

    Matrix4f cameraPosition;

    ArrayList<WorldObject> objects;

    Rectangle rectangle;

    Model line;

    public PolygonCreator creator;

    private int creatorCallbackID;

    private boolean pause;

    public World() {

        objects = new ArrayList<>();


    }

    public void load(Game game) {

        // Rectangle Test
        objects.add(new Rectangle(100, 100, new Vector2d(50, 50)));

        objects.add(new Rectangle(100, 100, new Vector2d(150, 150)));


        // Square polygon Test
        /*ArrayList<Vector2d> vectors = new ArrayList<>(4);
        vectors.add(new Vector2d(-1,  1));
        vectors.add(new Vector2d(-1, -1));
        vectors.add(new Vector2d(1, -1));
        vectors.add(new Vector2d(1,  1));

        objects.add(new Polygon(new Vector2d(100, 100),10, new Vector2d(300, 500), vectors));
        */
        // Pentagon polygon Test
        /*ArrayList<Vector2d> vectors2 = new ArrayList<>(5);
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

        line = LineCollectionModelFactory.makeModel(vectors3, true);*/

        this.creatorCallbackID = game.getCurrKeyCallback();

        game.keyCallbacks.put(creatorCallbackID, (window, key, scancode, action, mods) -> {

            if (key == GLFW.GLFW_KEY_C && action == GLFW.GLFW_RELEASE && creator == null) {

                creator = new PolygonCreator();

                creator.init(game);

            }

        });

    }

    public void update(Game game) {

        if (creator != null) {

            creator.update(game);

        }

        if (creator != null && creator.getStatus() == PolygonCreator.STATUS_FINISH) {

            ArrayList<Vector2d> retVertices = creator.stop(game);

            ArrayList<Vector2d> vertices = VectorUtils.centralise(retVertices);

            objects.add(new Polygon(new Vector2d(100, 100), 10, VectorUtils.getCentre(retVertices), vertices));

            creator = null;

        } else if (creator != null && creator.getStatus() == PolygonCreator.STATUS_QUIT) {

            creator = null;

        }

        if (this.pause) {

            return;

        }

        for (var object : objects) {

            object.update(this);

        }

        WorldObject test1 = objects.get(0);
        WorldObject test2 = objects.get(1);

        test1.move(game.getMouseX(), game.getMouseY());

        rotation += PI/360;

        test1.rotate(rotation);
        test2.rotate(-rotation);

        boolean collision = isColliding(test1, test2);

        System.out.println("Is colliding " + (collision ? "TRUE" : "FALSE"));

        int x;

    }

    double rotation = 0;

    public void render(Renderer renderer) {

        for (var object : objects) {

            object.render(renderer);

        }

        //renderer.drawModel(new Colour(0, 1, 0), line);

        if (creator != null) {

            creator.render(renderer);

        }

    }

    // using Separating axis theory (SAT) from http://dyn4j.org/2010/01/sat/#sat-top
    public static boolean isColliding(WorldObject object1, WorldObject object2) {

        // Get the rotated vertices of each object
        ArrayList<Vector2d> o1vertices = object1.getVertices();
        //System.out.println("Object 1 vertices: " + Arrays.toString(o1vertices.toArray()));

        ArrayList<Vector2d> o2vertices = object2.getVertices();
        //System.out.println("Object 2 vertices: " + Arrays.toString(o2vertices.toArray()));

        // Create a list of all normals
        var normalsList = new ArrayList<Vector2d>();
        normalsList.addAll(object1.getRightHandNormals());
        normalsList.addAll(object2.getRightHandNormals());

        // Put these normals into a HashSet so we remove any excess normal calculations later
        var normals = new HashSet<>(normalsList);

        Vector2d o1position = object1.getPosition();
        Vector2d o2position = object2.getPosition();

        // A vector from object1 to object 2 that we can project onto the normals
        var displacementVec = new Vector2d(o2position.x - o1position.x, o2position.y - o1position.y);

        // Loop over every unique normal
        for (var normal : normals) {

            // The maximum magnitude and corresponding vector found
            double o1maxProjMag = 0;
            Vector2d o1maxProjVec = null;

            // Loop over every vertex in o1 and project them along the current normal
            for (var o1vertex : o1vertices) {

                Vector2d currProjVec = VectorUtils.getProjection(o1vertex, normal);

                double currProjMag = VectorUtils.getMagnitude(currProjVec);

                // We want the projection to be positive (along the same direction as the normal)
                if (currProjMag > o1maxProjMag && isProjectionPositive(currProjVec, normal)) {

                    o1maxProjMag = currProjMag;
                    o1maxProjVec = currProjVec;

                }

            }

            //if (o1maxProjVec != null)
            //    System.out.println(normal.toString() + " : " + o1maxProjVec.toString());

            double o2maxProjMag = 0;
            Vector2d o2maxProjVec = null;

            // Loop over evert vertex in o2 and project them along the current normal
            for (var o2vertex : o2vertices) {

                Vector2d currProjVec = VectorUtils.getProjection(o2vertex, normal);

                double currProjMag = VectorUtils.getMagnitude(currProjVec);

                // We want the projection to be negative (along the opposite direction as the normal)
                if (currProjMag > o2maxProjMag && !isProjectionPositive(currProjVec, normal)) {

                    o2maxProjMag = currProjMag;
                    o2maxProjVec = currProjVec;

                }

            }

            // Project the displacement between the two objects onto the normal
            Vector2d displacementProj = VectorUtils.getProjection(displacementVec, normal);

            //if (o2maxProjVec != null)
            //    System.out.println(normal.toString() + " : " + o2maxProjVec.toString());

            //if (displacementProjection != null)
            //    System.out.println(normal.toString() + " : " + displacementProj.toString());

            // System.out.println(o1maxProjMag + " " + o1maxProjMag + " " + VectorUtils.getMagnitude(displacementProj));

            // Vector2d finalVector = new Vector2d(o1maxProjVec.x + o2maxProjVec.x + displacementProj.x, o1maxProjVec.y + o2maxProjVec.y + displacementProj.y);

            // Now check if the objects overlap along this normal, if so we can exit early
            if ((int) o1maxProjMag + (int) o2maxProjMag <= (int) VectorUtils.getMagnitude(displacementProj))
                return false;

            // System.out.println(finalVector.toString());

        }

        return true;

    }

    static private boolean isPositive(double a) {
        return a > 0;
    }

    static private boolean isProjectionPositive(Vector2d projection, Vector2d vector) {

        return (isPositive(projection.x) == isPositive(vector.x)
             && isPositive(projection.y) == isPositive(vector.y));

    }

    public void pauseSimulation(boolean flag) {

        this.pause = flag;

    }

}
