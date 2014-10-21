package com.theodore.aero.graphics;

import com.theodore.aero.core.Util;
import com.theodore.aero.math.MathUtils;
import com.theodore.aero.math.Matrix4;
import com.theodore.aero.math.Vector3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;

public class GraphicsUtil {

    public void init() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        setFrontFace(GL_CW);
        enableCullFace(GL_BACK);
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);

        setDepthTest(true);
        setDepthClamp(true);
        setTexture2D(true);

        enableBlending(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public void clearColorAndDeptAndStencil() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void clearColorAndDepth() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void clearColor() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void clearDepth() {
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public void setTexture2D(boolean value) {
        if (value)
            glEnable(GL_TEXTURE_2D);
        else
            glDisable(GL_TEXTURE_2D);
    }

    public void setDepthClamp(boolean value) {
        if (value)
            glEnable(GL_DEPTH_CLAMP);
        else
            glDisable(GL_DEPTH_CLAMP);
    }

    public void setFrontFace(int value) {
        glFrontFace(value);
    }

    public void enableCullFace(int value) {
        glEnable(GL_CULL_FACE);
        glCullFace(value);
    }

    public void disableCullFace() {
        glDisable(GL_CULL_FACE);
    }

    public void setDepthTest(boolean value) {
        if (value)
            glEnable(GL_DEPTH_TEST);
        else
            glDisable(GL_DEPTH_TEST);
    }

    public void setDepthFunc(int value) {
        glDepthFunc(value);
    }

    public void setDepthMask(boolean value) {
        glDepthMask(value);
    }

    public void enableBlending(int value, int value0) {
        glEnable(GL_BLEND);
        glBlendFunc(value, value0);
    }

    public void disableBlending() {
        glDisable(GL_BLEND);
    }

    public int GL_LINEAR_MIPMAP_LINEAR() {
        return GL_LINEAR_MIPMAP_LINEAR;
    }

    public int GL_LINEAR_MIPMAP_NEAREST() {
        return GL_LINEAR_MIPMAP_NEAREST;
    }

    public int GL_NEAREST_MIPMAP_LINEAR() {
        return GL_NEAREST_MIPMAP_LINEAR;
    }

    public int GL_NEAREST_MIPMAP_NEAREST() {
        return GL_NEAREST_MIPMAP_NEAREST;
    }

    public int GL_LINEAR() {
        return GL_LINEAR;
    }

    public int GL_NEAREST() {
        return GL_NEAREST;
    }

    public int GL_REPEAT() {
        return GL_REPEAT;
    }

    public int GL_CLAMP_TO_EDGE() {
        return GL_CLAMP_TO_EDGE;
    }

    public int GL_CLAMP() {
        return GL_CLAMP;
    }

    public int GL_CLAMP_TO_BORDER() {
        return GL_CLAMP_TO_BORDER;
    }

    public int createVertexBuffer(FloatBuffer data, boolean isStatic) {
        int buffer = glGenBuffers();
        int hint = GL_STATIC_DRAW;

        if (!isStatic)
            hint = GL_DYNAMIC_DRAW;

        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        glBufferData(GL_ARRAY_BUFFER, data, hint);

        return buffer;
    }

    public int createIndexBuffer(IntBuffer data, boolean isStatic) {
        int buffer = glGenBuffers();
        int hint = GL_STATIC_DRAW;

        if (!isStatic)
            hint = GL_DYNAMIC_DRAW;

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, hint);

        return buffer;
    }

    public void deleteBuffer(int buffer) {
        glDeleteBuffers(buffer);
    }

    private int String_Find(String string, String key) {
        return String_Find(string, key, 0);
    }

    private int String_Find(String string, String key, int start) {
        for (int i = start; i < string.length(); i++) {
            if (string.charAt(i) == key.charAt(0)) {
                String test = string.substring(i, i + key.length());

                if (test.equals(key))
                    return i;
            }
        }

        return -1;
    }

    private String getSpecifiedVersion(String shaderText) {
        final String VERSION_KEY = "#version ";

        int versionLocation = String_Find(shaderText, VERSION_KEY);
        int versionNumberStart = versionLocation + VERSION_KEY.length();
        int versionNumberEnd = String_Find(shaderText, "\n", versionNumberStart);

        return shaderText.substring(versionNumberStart, versionNumberEnd);
    }

    private int String_FindClosingBrace(String text, int initialBraceLocation) {
        int currentLocation = initialBraceLocation + 1;

        while (currentLocation != -1) {
            int nextOpening = String_Find(text, "{", currentLocation);
            int nextClosing = String_Find(text, "}", currentLocation);

            if (nextClosing < nextOpening || nextOpening == -1)
                return nextClosing;

            currentLocation = nextClosing;
        }

        System.err.println("Error: Shader is missing a closing brace!");

        return -1;
    }

    private String eraseShaderFunction(String text, String functionHeader) {
        int begin = String_Find(text, functionHeader);

        if (begin == -1)
            return text;

        int end = String_FindClosingBrace(text, String_Find(text, "{", begin));

        String firstHalf = text.substring(0, begin);
        String secondHalf = text.substring(end + 1);

        return firstHalf.concat(secondHalf);
    }

    private String String_InsertCounter(String string) {
        final String KEY = "%d";

        int location = String_Find(string, KEY);
        int num = 0;

        while (location != -1) {
            String firstHalf = string.substring(0, location);
            String secondHalf = string.substring(location + KEY.length());

            string = firstHalf + num + secondHalf;
            num++;
            location = String_Find(string, KEY, location);
        }

        return string;
    }

    private String String_ReplaceLines(String text, String name) {
        int start = String_Find(text, name);

        while (start != -1) {
            int end = String_Find(text, "\n", start);

            String firstHalf = text.substring(0, start);
            String secondHalf = text.substring(end);

            text = firstHalf + secondHalf;

            start = String_Find(text, name, start);
        }

        return text;
    }

    private static void checkShaderError(int shader, int flag, boolean isProgram, String errorMessage) {
        int success;
        String error;

        if (isProgram)
            success = glGetProgrami(shader, flag);
        else
            success = glGetShaderi(shader, flag);

        if (success == 0) {
            if (isProgram)
                error = glGetProgramInfoLog(shader, 1024);
            else
                error = glGetShaderInfoLog(shader, 1024);

            System.err.println(errorMessage + ": '" + error + "'");
            System.exit(1);
        }
    }

    private int createShader(String text, int type) {
        int shader = glCreateShader(type);

        glShaderSource(shader, text);
        glCompileShader(shader);

        checkShaderError(shader, GL_COMPILE_STATUS, false, "Error compiling shader type " + type);

        return shader;
    }

    public int createVertexShader(String text) {
        String version = getSpecifiedVersion(text);
        String vertexShaderText = text.replace("void VSmain()", "void main()");

        vertexShaderText = eraseShaderFunction(vertexShaderText, "void FSmain()");

        if (version.equals("330")) {
            vertexShaderText = vertexShaderText.replaceAll("attribute", "layout(location = %d) in");
            vertexShaderText = String_InsertCounter(vertexShaderText);
            vertexShaderText = vertexShaderText.replaceAll("varying", "out");
        }

        return createShader(vertexShaderText, GL_VERTEX_SHADER);
    }

    public int createFragmentShader(String text) {
        String version = getSpecifiedVersion(text);
        String fragmentShaderText = text.replace("void FSmain()", "void main()");

        fragmentShaderText = eraseShaderFunction(fragmentShaderText, "void VSmain()");

        fragmentShaderText = String_ReplaceLines(fragmentShaderText, "attribute");

        if (version.equals("330")) {
            fragmentShaderText = fragmentShaderText.replace("varying", "in");
            fragmentShaderText = fragmentShaderText.replace("gl_FragColor", "OUT_Fragment_Color");

            String newFragout = "out vec4 OUT_Fragment_Color;\n";
            int start = String_Find(fragmentShaderText, "\n");

            String firstHalf = fragmentShaderText.substring(0, start + 1);
            String secondHalf = fragmentShaderText.substring(start + 1);

            fragmentShaderText = firstHalf + newFragout + secondHalf;
        }

        return createShader(fragmentShaderText, GL_FRAGMENT_SHADER);
    }

    public int createShaderProgram(int[] shaders) {
        int program = glCreateProgram();

        for (int shader : shaders) glAttachShader(program, shader);

        glLinkProgram(program);
        checkShaderError(program, GL_LINK_STATUS, true, "Error linking shader program");

        return program;
    }

    //TODO: Structs should be an arraylist of uniformData
    private void addUniform(String uniformName, String uniformType, int shaderProgram, ArrayList<UniformData> structs, ArrayList<UniformData> result) {
        boolean addThis = true;

        if (!addThis)
            return;

        int location = glGetUniformLocation(shaderProgram, uniformName);

        result.add(new UniformData(location, uniformType, uniformName));
    }

    //TODO: Add struct detection!
    public UniformData[] createShaderUniforms(String shaderText, int shaderProgram) {
        final String UNIFORM_KEY = "uniform";

        //ArrayList<UniformStruct> structs = FindUniformStructs(shaderText);

        ArrayList<UniformData> result = new ArrayList<UniformData>();

        int uniformLocation = String_Find(shaderText, UNIFORM_KEY);
        while (uniformLocation != -1) {
            boolean isCommented = false;
//			int lastLineEnd = shaderText.rfind(";", uniformLocation);
//
//			if(lastLineEnd != std::string::npos)
//			{
//				String potentialCommentSection = shaderText.substring(lastLineEnd,uniformLocation);
//				isCommented = potentialCommentSection.find("//") != std::string::npos;
//			}

            if (!isCommented) {
                int begin = uniformLocation + UNIFORM_KEY.length();
                int end = String_Find(shaderText, ";", begin);

                String uniformLine = shaderText.substring(begin + 1, end);

                begin = String_Find(uniformLine, " ");
                String uniformName = uniformLine.substring(begin + 1);
                String uniformType = uniformLine.substring(0, begin);

                //AddUniform(uniformName, uniformType, shaderProgram, structs, result);
                addUniform(uniformName, uniformType, shaderProgram, null, result);
            }
            uniformLocation = String_Find(shaderText, UNIFORM_KEY, uniformLocation + UNIFORM_KEY.length());
        }

        UniformData[] returnValue = new UniformData[result.size()];
        result.toArray(returnValue);

        return returnValue;
    }

    public void validateShaderProgram(int program) {
        glValidateProgram(program);
        checkShaderError(program, GL_VALIDATE_STATUS, true, "Invalid shader program");
    }

    private static int lastProgram = 0;

    public void bindShaderProgram(int program) {
        if (lastProgram != program) {
            glUseProgram(program);
            lastProgram = program;
        }
    }

    public void deleteShaderProgram(int program, int[] shaders) {
        for (int shader : shaders) {
            glDetachShader(program, shader);
            glDeleteShader(shader);
        }

        glDeleteProgram(program);
    }

    public void setUniformInt(int uniformLocation, int value) {
        glUniform1i(uniformLocation, value);
    }

    public void setUniformFloat(int uniformLocation, float value) {
        glUniform1f(uniformLocation, value);
    }

    public void setUniformVector3(int uniformLocation, Vector3 value) {
        glUniform3f(uniformLocation, value.getX(), value.getY(), value.getZ());
    }

    public void setUniformMatrix4(int uniformLocation, Matrix4 value) {
        glUniformMatrix4(uniformLocation, true, Util.createFlippedBuffer(value));
    }

    public int createTexture(int width, int height, ByteBuffer data, int textureTarget, int filters, int internalFormat, int format, boolean clamp) {
        int texture = glGenTextures();
        glBindTexture(textureTarget, texture);

        if (filters == GL_NEAREST_MIPMAP_LINEAR ||
                filters == GL_NEAREST_MIPMAP_NEAREST ||
                filters == GL_LINEAR_MIPMAP_LINEAR ||
                filters == GL_LINEAR_MIPMAP_NEAREST) {
            GL30.glGenerateMipmap(texture);
            glTexParameteri(textureTarget, GL_GENERATE_MIPMAP, GL_TRUE);

            float maxAnisotropy = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);

            glTexParameterf(textureTarget, GL_TEXTURE_MAX_ANISOTROPY_EXT, MathUtils.clamp(0.0f, 16.0f, maxAnisotropy));
        } else {
            glTexParameteri(textureTarget, GL_TEXTURE_BASE_LEVEL, 0);
            glTexParameteri(textureTarget, GL_TEXTURE_MAX_LEVEL, 0);
        }

        if (clamp) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        glTexParameterf(textureTarget, GL_TEXTURE_MIN_FILTER, filters);
        glTexParameterf(textureTarget, GL_TEXTURE_MAG_FILTER, filters);

        glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);

        glBindTexture(textureTarget, 0);

        return texture;
    }

    private static int lastTexture = 0;
    private static int lastUnit = 0;

    public void bindTexture(int texture, int unit) {
        if (lastTexture != texture) {
            if (unit != lastUnit) {
                //TODO: This may fail if GL_TEXTUREX is not ordered sequentially!
                assert (unit >= 0 && unit <= 31);
                glActiveTexture(GL_TEXTURE0 + unit);
                lastUnit = unit;
            }

            glBindTexture(GL_TEXTURE_2D, texture);
            lastTexture = texture;
        }
    }

    public void deleteTexture(int texture) {
        glDeleteTextures(texture);
    }

    public static String getOpenglVersion() {
        return glGetString(GL_VERSION);
    }
}
