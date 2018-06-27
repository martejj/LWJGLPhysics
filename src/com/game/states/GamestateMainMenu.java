package com.game.states;

import com.game.Game;
import com.game.graphics.Renderer;

public class GamestateMainMenu extends Gamestate{

    // Placeholder class for a main menu, currently just goes straight to game
    @Override
    public void update(Game game) {
        game.changeState(new GamestatePhysicsSandbox());
    }

    @Override
    public void render(Renderer renderer) {

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
        return "Main Menu";
    }
}
