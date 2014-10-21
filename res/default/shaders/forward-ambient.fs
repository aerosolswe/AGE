#version 330
#include "lighting.glh"

out vec4 fragColor;

uniform vec3 ambientLight;

void main(){
    vec4 texel = texture(diffuse, texCoord0);

    if(texel.a < 0.5)
       discard;

    fragColor = vec4(color, alpha) * texture2D(diffuse, texCoord0.xy) * vec4(ambientLight, 1);
}