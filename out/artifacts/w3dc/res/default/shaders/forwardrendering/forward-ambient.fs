#version 330

varying vec2 texCoord0;

uniform vec3 ambientIntensity;
uniform sampler2D sampler;
uniform float alpha;

void main()
{
    vec4 texel = texture(sampler, texCoord0);

    if(texel.a < 0.5)
       discard;

    gl_FragColor = texture2D(sampler, texCoord0.xy) * vec4(ambientIntensity, 1);
}