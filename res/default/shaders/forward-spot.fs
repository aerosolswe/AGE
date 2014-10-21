#version 330
#include "lighting.glh"

out vec4 fragColor;

uniform SpotLight spotLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    vec4 lightingAmount = calcSpotLight(spotLight, normalize(calcNormal())) * calcShadowAmount(shadowMap, shadowMapCoords0);

    vec4 texel = texture(diffuse, texCoord0);

        if(texel.a < 0.5)
           discard;

    fragColor = vec4(color, alpha) * calcTextureColor() * lightingAmount;
}