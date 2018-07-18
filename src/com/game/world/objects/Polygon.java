package com.game.world.objects;

import com.game.graphics.ConvexPolygonModelFactory;
import com.game.graphics.Model;
import com.game.graphics.Renderer;
import org.joml.Vector2d;

import java.util.ArrayList;

public class Polygon extends WorldObject {

    private Model model;

    Vector2d size;

    public Polygon(Vector2d size, double mass, Vector2d position, ArrayList<Vector2d> vertices) {
        super(mass, position);

        this.model = ConvexPolygonModelFactory.makeModel(vertices);

        this.verticies = vertices;

        this.size = size;



        this.colour.setColour(1, 1, 0);

    }

    @Override
    public void render(Renderer renderer) {

        renderer.drawModel(size.x, size.y, position.x, position.y, this.colour, 0, model);

    }

}
