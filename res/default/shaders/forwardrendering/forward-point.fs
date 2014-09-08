#version 330
#include "forwardrendering/lighting.glh"

out vec4 fragColor;

uniform PointLight pointLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    fragColor = calcTextureColor() *
                calcPointLight(pointLight, normalize(calcNormal()));
}