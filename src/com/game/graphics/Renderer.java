package com.game.graphics;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;

public class Renderer {

    public Model rectangle;

    public Shader shader;

    Canvas canvas;

    int initialWidth;
    int initialHeight;

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

    public int getInitialWidth() {
        return initialWidth;
    }

    public int getInitialHeight() {
        return initialHeight;
    }
}
