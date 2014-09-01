#version 330

in vec2 texCoord0;

out vec4 fragColor;

uniform vec3 color;
uniform float alpha;
uniform sampler2D sampler;

void main(){
    vec4 texel = texture(sampler, texCoord0);

    if(texel.a < 0.5)
        discard;
    fragColor = texel * vec4(color, alpha);
}