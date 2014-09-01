#version 330
#include "forwardrendering/lighting.glh"

in vec4 shadowCoord0;
in vec4 shadowCoord1;
in vec4 shadowCoord2;
in vec4 shadowCoord3;

in vec2 texCoord0;
in vec3 normal0;
in vec3 worldPos0;
in mat3 tbnMatrix0;

out vec4 fragColor;

uniform DirectionalLight directionalLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    fragColor = calcTextureColor() *
                calcDirectionalLight(directionalLight, normalize(calcNormal())) *
                calcShadowFactor(shadowCoord0, shadowCoord1, shadowCoord2, shadowCoord3);
}