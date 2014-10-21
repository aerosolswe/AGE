#version 330

in vec3 texCoord0;
in vec3 vertex;

out vec4 fragColor;

uniform sampler2D glow;
uniform sampler2D color;
uniform vec3 lightPos;

void main(){
    vec3 V = normalize(vertex);
    vec3 L = normalize(lightPos.xyz);
    float vl = dot(V, L);

    vec4 Kc = texture2D(color, vec2((L.y + 1.0) / 2.0, V.y));
    vec4 Kg = texture2D(glow,  vec2((L.y + 1.0) / 2.0, vl));

    fragColor = vec4(Kc.rgb + Kg.rgb * Kg.a / 2.0, Kc.a);
}

// funkar dåligt,ge ett värdigt försök att lägga in http://www.java-gaming.org/index.php?topic=28366.0 ist!