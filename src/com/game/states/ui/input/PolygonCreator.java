package com.game.states.ui.input;

import com.game.Game;
import com.game.graphics.Colour;
import com.game.graphics.LineCollectionModelFactory;
import com.game.graphics.Model;
import com.game.graphics.Renderer;
import com.game.world.CollisionManager;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import javax.sound.sampled.Line;
import java.util.ArrayList;

public class PolygonCreator {

    private int mouseButtonCallbackID;

    private ArrayList<Vector2d> vertices;

    private boolean finished;

    double connectionRadius = 10;

    private Model lines;

    private double mouseX, mouseY;

    public void init(Game game) {

        vertices = new ArrayList<>();

        mouseButtonCallbackID = game.getCurrMouseButtonCallback();

        game.mouseButtonCallbacks.put(mouseButtonCallbackID, (window, button, action, mods) -> {

            this.mouseX = game.getMouseX();

            this.mouseY = game.getMouseY();

            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {

                if (vertices.size() >= 2) { // If we have enough vertices to make a triangle when we add this one

                    if (CollisionManager.isWithinRadiusOf(mouseX, mouseY, connectionRadius, vertices.get(0).x, vertices.get(0).y)) {

                        finished = true;

                        System.out.println("finished");

                    } else {

                        System.out.println("new line segment");

                        vertices.add(new Vector2d(mouseX, mouseY));

                        if (lines != null) {

                            lines.delete();

                        }

                        lines = LineCollectionModelFactory.makeModel(vertices, false);

                    }

                } else {

                    System.out.println("new line segment 2");

                    vertices.add(new Vector2d(game.getMouseX(), game.getMouseY()));

                }

            } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {

                //TODO on right clicks

            }

        });

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

            currentLines.render();

            currentLines.delete();

        }

    }

    public ArrayList<Vector2d> stop(Game game) {

        lines.delete();

        game.mouseButtonCallbacks.remove(mouseButtonCallbackID);

        return vertices;

    }

    public boolean isFinished() {

        return finished;

    }

}
