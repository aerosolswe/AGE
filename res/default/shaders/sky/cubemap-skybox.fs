#version 330

in vec3 texCoord0;

out vec4 fragColor;

uniform samplerCube cubeMapTexture;
uniform vec3 color;

void main(){
    fragColor = (texture(cubeMapTexture, texCoord0) * vec4(color, 1.0));
}