package com.game;

import com.game.graphics.Canvas;
import com.game.graphics.Renderer;
import com.game.states.Gamestate;
import com.game.states.GamestateSplash;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import java.util.Hashtable;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private Canvas canvas;

    private Gamestate state;

    private Renderer renderer;

    private double mouseX, mouseY;

    private Hashtable<Integer, Gamestate> states;

    private int currKeyCallback = 0;
    public Hashtable<Integer, GLFWKeyCallbackI> keyCallbacks;

    private int currMouseButtonCallback = 0;
    public Hashtable<Integer, GLFWMouseButtonCallbackI> mouseButtonCallbacks;

    private int currScrollCallback = 0;
    public Hashtable<Integer, GLFWScrollCallbackI> scrollCallbacks;

    public Game() {

        canvas = new Canvas();

        states = new Hashtable<>();

        keyCallbacks = new Hashtable<>();
        mouseButtonCallbacks = new Hashtable<>();
        scrollCallbacks = new Hashtable<>();

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

    public void init() {

        setupCallbacks();

    }

    public void setupCallbacks() {

        glfwSetCursorPosCallback(getWindow(), (window, xpos, ypos) -> {

            this.mouseX = xpos;

            this.mouseY = getInitialHeight() - ypos; // Quick fix TODO

        });

        glfwSetKeyCallback(getWindow(), (window, key, scancode, action, mods) -> {

            for (var callback : this.keyCallbacks.values()) {

                callback.invoke(window, key, scancode, action, mods);

            }

        });

        glfwSetScrollCallback(getWindow(), (window, xoffset, yoffset) -> {

            for (var callback : this.scrollCallbacks.values()) {

                callback.invoke(window, xoffset, yoffset);

            }

        });

        glfwSetMouseButtonCallback(getWindow(), (window, button, action, mods) -> {

            for (var callback : this.mouseButtonCallbacks.values()) {

                callback.invoke(window, button, action, mods);

            }

        });

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

        game.init();

        game.run();

    }

    public int getInitialHeight() {

        return renderer.getInitialHeight();

    }

    public int getInitialWidth() {

        return renderer.getInitialWidth();

    }

    public long getWindow() {

        return this.canvas.getWindow();

    }

    public double getMouseX() {

        return mouseX;

    }

    public double getMouseY() {

        return mouseY;

    }

    public int getCurrKeyCallback() {
        currKeyCallback++;
        return currKeyCallback - 1;
    }

    public int getCurrMouseButtonCallback() {
        currMouseButtonCallback++;
        return currMouseButtonCallback - 1;
    }

    public int getCurrScrollCallback() {
        currScrollCallback++;
        return currScrollCallback - 1;
    }
}
