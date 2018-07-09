package com.game.world.objects;

import com.game.graphics.Colour;
import com.game.graphics.Renderer;
import com.game.world.World;
import org.joml.Vector2d;
import org.joml.Vector2i;

public class Rectangle extends  WorldObject {

    Vector2i dimensions;

    public static final double density = 10;

    public Rectangle(int width, int height, Vector2d position) {

        super(width*height*density, position);

        this.colour = new Colour(1, 0, 0);

        this.rotation = 0;

        this.dimensions = new Vector2i();
        this.dimensions.x = width;
        this.dimensions.y = height;

        // VERY specificly stored in a counter clockwise manner.
        this.verticies.add(new Vector2d(+ width/2, + height/2));
        this.verticies.add(new Vector2d(- width/2, + height/2));
        this.verticies.add(new Vector2d(- width/2, - height/2));
        this.verticies.add(new Vector2d(+ width/2, - height/2));

    }

    @Override
    public void update(World world) {
        super.update(world);



    }

    @Override
    public void render(Renderer renderer) {

        renderer.drawRectangle(dimensions.x, dimensions.y, position.x, position.y, colour, rotation);

    }
}
