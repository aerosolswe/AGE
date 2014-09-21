#version 330

layout (location = 0) in vec3 position;

out vec3 texCoord0;
out vec3 worldPos0;

uniform mat4 MVP;
uniform mat4 model;

void main(){
    vec4 MVP_Pos = MVP * vec4(position, 1.0);
    gl_Position = MVP_Pos.xyww;
    texCoord0 = position;

    worldPos0 = (model * vec4(position, 1.0)).xyz;
}