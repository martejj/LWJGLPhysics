package com.game.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;

public class Renderer {

    public Model rectangle;

    public Shader shader;

    Canvas canvas;

    private int initialWidth;
    private int initialHeight;

    private Stack<Matrix4f> transformStack;

    private Matrix4f currentTransformation;

    public Renderer(Canvas canvas) {

        this.canvas = canvas;

        this.initialHeight = getCurrentHeight();

        this.initialWidth = getCurrentWidth();

        // Vertices and indices for a rectangle

        float[] vertices = {
            -1f,   1f,   0, // TOP LEFT
             1f,   1f,   0, // TOP RIGHT
             1f,  -1f,   0, // BOTTOM RIGHT
            -1f,  -1f,   0  // BOTTOM LEFT
        };

        int[] indices = {
                0,  1,  2,
                0,  2,  3
        };

        shader = new Shader("shader");

        rectangle = new Model(vertices, indices);

        // Setup the transformation stack.

        transformStack = new Stack<>();

        currentTransformation = new Matrix4f().ortho2D(0, getInitialWidth(), 0, getInitialHeight());

        transformStack.push(currentTransformation);

    }

    public void drawRectangle(int width, int height, int x, int y, Colour colour) {

        // pushTransform(new Matrix4f().identity().scale(x, y, 1));
        Matrix4f projection = new Matrix4f()
                .ortho2D(-x, this.getInitialWidth() - x, -y, this.getInitialHeight() - y);

        Matrix4f scale = new Matrix4f().scaling(width, height, 1);

        shader.bind();
        //shader.setColour(colour.red, colour.green, colour.blue);
        shader.setColour(1f, 1f, 0f);
        shader.setUniform("projection", projection); // should be performed on gcard
        shader.setUniform("scale", scale);
        rectangle.render();

        // popTransform();

    }

    public int getCurrentWidth() {

        int width;

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(canvas.getWindow(), pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            width = pWidth.get();

        } // the stack frame is popped automatically

        return  width;

    }

    public int getCurrentHeight() {

        int height;

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(canvas.getWindow(), pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            height = pHeight.get();

        } // the stack frame is popped automatically

        return  height;

    }

    public void render(Model model) {

        shader.setUniform("projection", currentTransformation);

        model.render();

    }

    public int getInitialWidth() {
        return initialWidth;
    }

    public int getInitialHeight() {
        return initialHeight;
    }

    public void pushTransform(Matrix4f matrix) {

        currentTransformation.mul(matrix);
        transformStack.push(matrix);

    }

    public void popTransform() {

        currentTransformation.mul(transformStack.pop().invert());

    }

}
