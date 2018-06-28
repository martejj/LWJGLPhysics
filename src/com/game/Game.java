package com.game;

import com.game.graphics.Canvas;
import com.game.graphics.Renderer;
import com.game.states.Gamestate;
import com.game.states.GamestateSplash;

import java.util.Hashtable;

public class Game {

    private Canvas canvas;

    Gamestate state;

    Renderer renderer;

    public Hashtable<Integer, Gamestate> states;

    public Game() {

        canvas = new Canvas();

        states = new Hashtable<>();

    }

    public void run() {

        this.changeState(new GamestateSplash());

        renderer = new Renderer(canvas);

        while (canvas.preRender()) {

            this.update();

            this.render();

            canvas.postRender();

        }

    }

    public void update() {

        state.update(this);

    }

    public void render() {

        state.render(renderer);

    }

    public void initState(Gamestate state) {

        if(!states.containsKey(state.getID())) {

            states.put(state.getID(), state);

            state.init(this);

        }

    }

    public void changeState(Gamestate state) {

        if (!states.containsKey(state.getID())) {

            System.out.println("iniitalising : " + state.getName());
            initState(state);

        }

        states.get(state.getID()).load(this);

        if (this.state != null) {
            this.state.unload(this);
        }

        this.state = states.get(state.getID());

    }

    public static void main(String[] args) {

        var game = new Game();

        game.run();

    }

    public int getInitialHeight() {

        return renderer.getInitialHeight();

    }

    public int getInitialWidth() {

        return renderer.getInitialWidth();

    }
}
