#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

uniform mat4 MVP;
uniform mat4 model;

out vec3 worldPos0;

void main()
{
    vec4 pos = vec4(position, 1.0);
    gl_Position = MVP * pos;
    worldPos0 = (model * pos).xyz;
}
