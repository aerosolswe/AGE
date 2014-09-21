#version 330

in vec2 texCoord0;
out vec4 fragColor;

uniform vec3 ambientIntensity;
uniform sampler2D sampler;
uniform vec3 color;
uniform float alpha;

void main(){
    vec4 texel = texture(sampler, texCoord0);

    if(texel.a < 0.5)
       discard;

    fragColor = vec4(color, alpha) * texture2D(sampler, texCoord0.xy) * vec4(ambientIntensity, 1);
}