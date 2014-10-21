#version 330
#include "lighting.glh"

out vec4 fragColor;

uniform PointLight pointLight;

void main(){
    init(worldPos0, texCoord0, tbnMatrix0);

    vec4 texel = texture(diffuse, texCoord0);

    if(texel.a < 0.5)
        discard;

    vec3 lightDirection = worldPos - pointLight.position;

    fragColor = vec4(color, alpha) * calcTextureColor() *
                (calcPointLight(pointLight, normalize(calcNormal())) * calcShadowFactor(lightDirection));
}