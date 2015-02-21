package com.theodore.aero.graphics.shaders;

import com.theodore.aero.components.BaseLight;
import com.theodore.aero.components.DirectionalLight;
import com.theodore.aero.components.PointLight;
import com.theodore.aero.components.SpotLight;
import com.theodore.aero.core.Transform;
import com.theodore.aero.core.Util;
import com.theodore.aero.graphics.Graphics;
import com.theodore.aero.graphics.g3d.Material;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;
import com.theodore.aero.resources.ShaderResource;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;

public class Shader {

    private static HashMap<String, ShaderResource> loadedShaders = new HashMap<String, ShaderResource>();

    private ShaderResource resource;
    private String fileName;

    private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

    public Shader(String fileName) {
        this.fileName = fileName;

        ShaderResource oldResource = loadedShaders.get(fileName);

        if (oldResource != null) {
            resource = oldResource;
            resource.addReference();
        } else {
            resource = new ShaderResource(fileName);

            loadedShaders.put(fileName, resource);
        }
    }

    @Override
    protected void finalize() {
        if (resource.removeReference() && !fileName.isEmpty()) {
            loadedShaders.remove(fileName);
        }
    }

    public void compileShader() {
        resource.compileShader();
    }

    public void setAttribLocation(String attributeName, int location) {
        glBindAttribLocation(resource.program, location, attributeName);
    }

    public void addUniform(String uniform) {
        resource.addUniform(uniform);
    }

    public void bind() {
        glUseProgram(resource.program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void updateUniforms(Transform transform, Material material, Graphics graphics) {

    }

    public void updateTextureUniform(String uniformName, int texture) {
    }

    public void setUniformi(String uniformName, int value) {
        glUniform1i(resource.uniforms.get(uniformName), value);
    }

    public void setUniformf(String uniformName, float value) {
        glUniform1f(resource.uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3 value) {
        glUniform3f(resource.uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Matrix4 value) {
        buffer = Util.createFlippedBuffer(value);
        glUniformMatrix4(resource.uniforms.get(uniformName), true, buffer);
    }

    public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
        setUniformBaseLight(uniformName + ".base", directionalLight);
        setUniform(uniformName + ".direction", directionalLight.getDirection().getForward());
    }

    public void setUniformPointLight(String uniformName, PointLight pointLight) {
        setUniformBaseLight(uniformName + ".base", pointLight);
        setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
        setUniform(uniformName + ".position", pointLight.getTransform().getTransformedPos());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }

    public void setUniformSpotLight(String uniformName, SpotLight spotLight) {
        setUniformPointLight(uniformName + ".pointLight", spotLight);
        setUniform(uniformName + ".direction", spotLight.getTransform().getTransformedRot().getForward());
        setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
    }

}
