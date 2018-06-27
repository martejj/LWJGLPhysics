package com.game.world;

import com.game.Game;
import com.game.graphics.Renderer;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;

public class World {

    Matrix4f cameraPosition;

    public void update(Game game) {

        // Set origin as bottom left
        cameraPosition = new Matrix4f().ortho2D(0, game.getInitialWidth(), 0, game.getInitialHeight());

    }

    public void render(Renderer renderer) {

        renderer.pushTransform(cameraPosition);

        Matrix4f projection = new Matrix4f()
                .ortho2D(0, renderer.getInitialWidth(), 0, renderer.getInitialHeight());

        Matrix4f scale = new Matrix4f().scale(50).rotate(new AxisAngle4f((float) (Math.PI/4), 0, 0, 1));

        renderer.shader.bind();
        renderer.shader.setColour(1, 1,0);
        renderer.shader.setUniform("projection", projection.mul(scale));
        renderer.rectangle.render();
        renderer.shader.setUniform("projection", projection.mul(scale).invert());

        //renderer.popTransform(cameraPosition);
    }

}
