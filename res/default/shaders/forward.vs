#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

out vec2 texCoord0;
out vec2 texCoordUnModified;
out vec3 normal0;
out vec3 worldPos0;
out vec4 shadowMapCoords0;
out mat3 tbnMatrix0;
out float visibility;

uniform mat4 lightMatrix;
uniform mat4 model;
uniform mat4 MVP;
uniform mat4 viewProjectionMatrix;

uniform float density;
uniform float gradient;

uniform int textureRepeat;

void main(){
    gl_Position = MVP * vec4(position, 1.0);
    texCoord0 = texCoord * textureRepeat;
    texCoordUnModified = texCoord;
    normal0 = (model * vec4(normal, 0.0)).xyz;
    worldPos0 = (model * vec4(position, 1.0)).xyz;

    vec4 positionRelativeToCamera = viewProjectionMatrix * vec4(worldPos0, 1.0);

    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0, 1.0);

    shadowMapCoords0 = lightMatrix * vec4(position, 1.0);

    vec3 tangent0 = (model * vec4(tangent, 0.0)).xyz;
    vec3 n = normalize(normal0);

    vec3 t = normalize(tangent0);
    t = normalize(t - dot(t, n) * n);

    vec3 bitangent = cross(t, n);

    tbnMatrix0 = mat3(t, bitangent, n);
}