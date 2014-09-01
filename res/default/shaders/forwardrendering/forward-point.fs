#version 330
#include "forwardrendering/lighting.glh"

in vec2 texCoord0;
in vec3 normal0;
in vec3 worldPos0;
in mat3 tbnMatrix0;

out vec4 fragColor;

uniform PointLight pointLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    fragColor = calcTextureColor() *
                calcPointLight(pointLight, normalize(calcNormal()));
}