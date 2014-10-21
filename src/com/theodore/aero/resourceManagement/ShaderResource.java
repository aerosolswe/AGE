package com.theodore.aero.resourceManagement;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.util.glu.Util.*;

public class ShaderResource {

    public HashMap<String, Integer> uniforms;

    public int program;

    private int refCount;

    public ShaderResource(String filename) {
        this.program = glCreateProgram();
        this.refCount = 1;

        if (program == 0) {
            System.err.println("Shader creation failed: Could not find valid memory location in constructor");
            System.exit(1);
        }

        uniforms = new HashMap<String, Integer>();

        String vertexShaderText = loadShader(filename + ".vs");
        String fragmentShaderText = loadShader(filename + ".fs");

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);
    }

    public void addUniform(String uniform) {
        int uniformLocation = glGetUniformLocation(program, uniform);

        if (uniformLocation == 0xFFFFFFFF) {
            System.err.println("Error: Could not find uniform: " + uniform);
            new Exception().printStackTrace();
            System.exit(1);
        }

        uniforms.put(uniform, uniformLocation);
    }

    public void compileShader() {
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);

        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }

    }

    private void addProgram(String text, int type) {
        int shader = glCreateShader(type);

        if (shader == 0) {
            System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
            System.exit(1);
        }

        glShaderSource(shader, text);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(program, shader);
    }

    private void addVertexShader(String text) {
        addProgram(text, GL_VERTEX_SHADER);
    }

    private void addGeometryShader(String text) {
        addProgram(text, GL_GEOMETRY_SHADER);
    }

    private void addFragmentShader(String text) {
        addProgram(text, GL_FRAGMENT_SHADER);
    }


    private static String loadShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;
        final String INCLUDE_DIRECTIVE = "#include";

        try {
            shaderReader = new BufferedReader(new FileReader("./res/default/shaders/" + fileName));
            String line;

            while ((line = shaderReader.readLine()) != null) {
                if (line.startsWith(INCLUDE_DIRECTIVE)) {
                    shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
                } else
                    shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        return shaderSource.toString();
    }

    @Override
    protected void finalize() {
        glDeleteBuffers(program);
    }

    public void addReference() {
        refCount++;
    }

    public boolean removeReference() {
        refCount--;
        return refCount == 0;
    }
}
