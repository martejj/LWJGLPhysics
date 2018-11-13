package com.game.states.ui.input;

import com.game.Game;
import com.game.graphics.Colour;
import com.game.graphics.LineCollectionModelFactory;
import com.game.graphics.Model;
import com.game.graphics.Renderer;
import com.game.world.CollisionManager;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class PolygonCreator {

    public static final int STATUS_CREATE = 1;
    public static final int STATUS_QUIT = 2;
    public static final int STATUS_FINISH = 3;

    private int status;

    private int mouseButtonCallbackID;

    private ArrayList<Vector2d> vertices;

    double connectionRadius = 10;

    private Model lines;

    private double mouseX, mouseY;

    public void init(Game game) {

        this.status = STATUS_CREATE;

        vertices = new ArrayList<>();

        mouseButtonCallbackID = game.getCurrMouseButtonCallback();

        this.mouseX = game.getMouseX();

        this.mouseY = game.getMouseY();

        game.mouseButtonCallbacks.put(mouseButtonCallbackID, (window, button, action, mods) -> {

            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {

                this.onLeftClick();

            } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_RELEASE) {

                this.onRightClick();

            }

        });

    }

    private void onRightClick() {

        if (vertices.size() - 1 < 0) {

            this.status = STATUS_QUIT;

        } else {

            vertices.remove(vertices.size() - 1);

            if (vertices.size() >= 1) {

                updateLines();

            } else {

                lines = null;

            }

        }

    }

    private void onLeftClick() {

        if (vertices.size() >= 1) { // If we have enough vertices to make a triangle when we add this one

            if (CollisionManager.isWithinRadiusOf(mouseX, mouseY, connectionRadius, vertices.get(0).x, vertices.get(0).y)) {

                this.status = STATUS_FINISH;

            } else {

                vertices.add(new Vector2d(mouseX, mouseY));

                updateLines();

            }

        } else {

            vertices.add(new Vector2d(this.mouseX, this.mouseY));

        }

    }

    public void render(Renderer renderer) {

        if (lines != null) {

            renderer.drawModel(Colour.GREEN, lines);

        }

        if (vertices.size() >= 1) {

            ArrayList<Vector2d> currentLineVertices = new ArrayList<>(2);

            currentLineVertices.add(new Vector2d(vertices.get(vertices.size() - 1).x, vertices.get(vertices.size() - 1).y));

            currentLineVertices.add(new Vector2d(mouseX, mouseY));

            Model currentLines = LineCollectionModelFactory.makeModel(currentLineVertices, false);

            if (currentLines != null) {

                renderer.drawModel(Colour.BLUE, currentLines);

                currentLines.delete();

            }

        }

    }

    public void update(Game game) {

        this.mouseY = game.getMouseY();
        this.mouseX = game.getMouseX();

    }

    public ArrayList<Vector2d> stop(Game game) {

        lines.delete();

        game.mouseButtonCallbacks.remove(mouseButtonCallbackID);

        return (ArrayList<Vector2d>) vertices.clone();

    }

    public int getStatus() {

        return status;

    }

    private void updateLines() {

        if (lines != null) {

            lines.delete();

        }

        lines = LineCollectionModelFactory.makeModel(vertices, false);

    }

}
