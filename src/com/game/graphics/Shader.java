package com.game.graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int program; // OpenGL binding for this shader
    private int vs; // Describes shape
    private int fs; // Describes colour

    private String filename;

    public Shader(String filename) {

        this.filename = filename;

        program = glCreateProgram();

        // Compile the vertex shader

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename + ".vs"));
        glCompileShader(vs);

        // Check if there was an error
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == GLFW_FALSE) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        // Compile the fragment shader

        fs = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fs, readFile(filename + ".fs"));
        glCompileShader(fs);

        // Check if there was an error
        if(glGetShaderi(fs, GL_COMPILE_STATUS) == GLFW_FALSE) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        glAttachShader(program, vs);
        glAttachShader(program, fs);

        // We only ever link the initial vertices once. Afterwards we use the projeciton uniform to scale/transform/rotate it
        glBindAttribLocation(program, 0, "vertices");

        glLinkProgram(program);

        // Check for more errors

        if(glGetProgrami(program, GL_LINK_STATUS) == GLFW_FALSE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }

        glValidateProgram(program);

        if(glGetProgrami(program, GL_VALIDATE_STATUS) == GLFW_FALSE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }

    }

    /**
     * @param name name of uniform/shader property to change
     * @param value int value to set it to
     */
    public void setUniform(String name, int value) {

        int location = glGetUniformLocation(program, name);

        if (location == -1) {

            System.out.println("Cannot find uniform " + name + " in " + filename);

            System.exit(1);
        }

        glUniform1i(location, value);

    }

    /**
     * @param name uniform/shader property to change
     * @param value matrix value to change it to
     */
    public void setUniform(String name, Matrix4f value) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);

        value.get(buffer); // Load values into buffer

        int location = glGetUniformLocation(program, name);

        if (location == -1) {

            System.out.println("Cannot find uniform " + name + " in " + filename);

            System.exit(1);
        }

        glUniformMatrix4fv(location, false, buffer);

    }

    public void setColour(int red, int green, int blue) {

        setUniform("red", red);
        setUniform("green", green);
        setUniform("blue",  blue);

    }

    /**
     * Call when intending rendering this shader
     */
    public void bind() {

        glUseProgram(program);

    }

    private String readFile(String filename) {

        StringBuilder string = new StringBuilder();

        BufferedReader reader;

        try {

            reader = new BufferedReader(new FileReader(new File("./shaders/" + filename + ".glsl")));
            String line;

            while ((line = reader.readLine()) != null) {
                string.append(line);
                string.append("\n"); // As reader ignores new lines
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return string.toString();

    }
}