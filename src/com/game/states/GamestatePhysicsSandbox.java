package com.game.states;

import com.game.Game;
import com.game.graphics.Renderer;
import org.joml.Matrix4f;

public class GamestatePhysicsSandbox extends Gamestate{

    @Override
    public void update(Game game) {

    }

    @Override
    public void render(Renderer renderer) {

        Matrix4f projection = new Matrix4f()
                .ortho2D(0, renderer.getInitialWidth(), 0, renderer.getInitialHeight());

        Matrix4f scale = new Matrix4f().scale(30);

        renderer.shader.bind();
        renderer.shader.setColour(1, 1,0);
        renderer.shader.setUniform("projection", projection.mul(scale));
        renderer.rectangle.render();
    }

    @Override
    public void init(Game game) {

    }

    @Override
    public void load(Game game) {

    }

    @Override
    public void unload(Game game) {

    }

    @Override
    public String getName() {
        return "Physics Sandbox";
    }
}
