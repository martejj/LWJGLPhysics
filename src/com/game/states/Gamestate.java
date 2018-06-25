package com.game.states;

import com.game.Game;
import com.game.graphics.Renderer;

public abstract class Gamestate {

    int id;

    public Gamestate() {

        id = this.getName().hashCode();

    }

    public abstract void update(Game game);

    public abstract void render(Renderer renderer);

    public abstract void init(Game game);

    public abstract void load(Game game);

    public abstract void unload(Game game);

    public abstract String getName();

    public int getID() {
        return id;
    }

}
