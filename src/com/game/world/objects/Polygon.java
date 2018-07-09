package com.game.world.objects;

import com.game.graphics.ConvexPolygonModelFactory;
import com.game.graphics.Model;
import com.game.graphics.Renderer;
import org.joml.Vector2d;

import java.util.ArrayList;

public class Polygon extends WorldObject {

    Model model;

    public Polygon(double mass, Vector2d position, ArrayList<Vector2d> vertices) {
        super(mass, position);

        this.model = ConvexPolygonModelFactory.makeModel(vertices);

        this.verticies = vertices;

        this.colour.setColour(1, 1, 0);
    }

    @Override
    public void render(Renderer renderer) {

        renderer.drawModel(100, 100, position.x, position.y, this.colour, 0, model);

    }
}
