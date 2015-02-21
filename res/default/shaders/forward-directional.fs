#version 330
#include "lighting.glh"

out vec4 fragColor;

uniform DirectionalLight directionalLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    vec4 lightingAmount = calcDirectionalLight(directionalLight, normalize(calcNormal())) * calcShadowAmount(shadowMap, shadowMapCoords0);

    vec4 texel = texture(diffuse, texCoord0);

    if(texel.a < 0.5)
       discard;

    fragColor = vec4(color, alpha) * calcTextureColor() * lightingAmount;
    fragColor = calcFog(fragColor);
}