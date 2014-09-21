#version 330

in vec3 texCoord0;
in vec3 worldPos0;

out vec4 fragColor;

uniform sampler2D glow;
uniform sampler2D color;
uniform vec3 lightPos;

void main(){
    vec3 V = normalize(worldPos0);
    vec3 L = normalize(lightPos);

    float vl = dot(V, L);

    vec4 kc = texture2D(color, vec2((L.y + 1.0) / 2.0, V.y));
    vec4 kg = texture2D(glow, vec2((L.y + 1.0) / 2.0, vl));

    fragColor = vec4(kc.rgb * kg.a / 2.0, kc.a);
}