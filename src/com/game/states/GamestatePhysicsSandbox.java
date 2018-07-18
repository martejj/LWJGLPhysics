package com.game.states;

import com.game.Game;
import com.game.graphics.Renderer;
import com.game.world.World;

import static org.lwjgl.glfw.GLFW.*;

public class GamestatePhysicsSandbox extends Gamestate{

    World world;

    @Override
    public void update(Game game) {

        world.update(game);

    }

    @Override
    public void render(Renderer renderer) {

        world.render(renderer);

    }

    @Override
    public void init(Game game) {


    }

    @Override
    public void load(Game game) {

        world = new World();
        world.load(game);

        glfwSetKeyCallback(game.getWindow(), (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_0 && action == GLFW_RELEASE )
                System.out.println("1");
        });

    }

    @Override
    public void unload(Game game) {

    }

    @Override
    public String getName() {
        return "Physics Sandbox";
    }
}
