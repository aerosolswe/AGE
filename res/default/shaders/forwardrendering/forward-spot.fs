#version 330
#include "forwardrendering/lighting.glh"

out vec4 fragColor;

uniform SpotLight spotLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    vec4 lightingAmount = calcSpotLight(spotLight, normalize(calcNormal())) * calcShadowAmount(shadowMap, shadowMapCoords0);

    fragColor = calcTextureColor() * lightingAmount;
}