package com.game.states;

import com.game.Game;
import com.game.graphics.Renderer;

public class GamestateSplash extends Gamestate {

    public GamestateSplash() {
        super();


    }

    @Override
    public void update(Game game) {
        game.changeState(new GamestateMainMenu());
    }

    @Override
    public void render(Renderer renderer) {

    }

    @Override
    public void init(Game game) {

    }

    @Override
    public void load(Game game) {

        game.initState(new GamestateMainMenu());

        game.initState(new GamestatePhysicsSandbox());

    }

    @Override
    public void unload(Game game) {

    }

    @Override
    public String getName() {
        return "Splash Screen";
    }

}
