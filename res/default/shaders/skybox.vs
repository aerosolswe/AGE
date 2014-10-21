#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec3 texCoord0;

uniform mat4 MVP;

void main(){
    vec4 MVP_Pos = MVP * vec4(position, 1.0);
    gl_Position = MVP_Pos.xyww;
    texCoord0 = position;
}