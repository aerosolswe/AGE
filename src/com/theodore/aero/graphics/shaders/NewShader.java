package com.theodore.aero.graphics.shaders;

import com.theodore.aero.core.Aero;
import com.theodore.aero.graphics.UniformData;

import java.util.HashMap;

public class NewShader {

    private static final HashMap<String, NewShader> shaderPrograms = new HashMap<String, NewShader>();

    private final int[] shaders;
    private final UniformData[] uniforms;
    private final int program;

    public static NewShader get(String shaderName) {
        if (shaderPrograms.containsKey(shaderName))
            return shaderPrograms.get(shaderName);
        else {
            shaderPrograms.put(shaderName, new NewShader(shaderName));
            return shaderPrograms.get(shaderName);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Aero.graphicsUtil.deleteShaderProgram(program, shaders);
    }

    private NewShader(String name) {
        shaders = new int[2];
        String shaderText = Shader.loadShader(name + ".glsl");

        shaders[0] = (Aero.graphicsUtil.createVertexShader(shaderText));
        shaders[1] = (Aero.graphicsUtil.createFragmentShader(shaderText));

        program = Aero.graphicsUtil.createShaderProgram(shaders);
        uniforms = Aero.graphicsUtil.createShaderUniforms(shaderText, program);
    }

    public void bind() {
        Aero.graphicsUtil.bindShaderProgram(program);
    }

    public void validate() {
        Aero.graphicsUtil.validateShaderProgram(program);
    }

    /*public void update(Transform transform, Material material){
        final String prefix = "X_";
        final int preLength = prefix.length();

        for(UniformData uniform : uniforms){
            if(uniform.getName().length() > preLength
                    && uniform.getName().substring(0, preLength).equals(prefix)){
                String name = uniform.getName().substring(preLength);
                if(name.equals("MVP"))
                    Aero.getRenderer().setUniformMatrix4(uniform.getLocation(), transform.calcMVP());
                else if(name.equals("Transform"))
                    Aero.getRenderer().setUniformMatrix4(uniform.getLocation(), transform.calcModel());
            }
            else if(!Aero.getRenderingEngine().updateUniform(uniform, transform, material)){
                if(uniform.getType().equals("sampler2D"))
                    material.getTexture(uniform.getName()).bind(0);//TODO: Read texture unit from Material!
                else if(uniform.getType().equals("vec3"))
                    Aero.getRenderer().setUniformVector3(uniform.getLocation(), material.getVector(uniform.getName()));
                else if(uniform.getType().equals("float"))
                    Aero.getRenderer().setUniformFloat(uniform.getLocation(), material.getParameter(uniform.getName()));
            }
        }
    }*/
}
