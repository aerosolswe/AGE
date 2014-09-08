#version 330
#include "forwardrendering/lighting.glh"

out vec4 fragColor;

uniform DirectionalLight directionalLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    vec4 lightingAmount = calcDirectionalLight(directionalLight, normalize(calcNormal())) * calcShadowAmount(shadowMap, shadowMapCoords0);

    fragColor = calcTextureColor() * lightingAmount;
}