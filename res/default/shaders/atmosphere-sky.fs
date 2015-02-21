#version 330

in vec3 texCoord0;
in vec3 direction;
in vec3 rayleighColor;
in vec3 mieColor;

out vec4 fragColor;

uniform sampler2D starmap;

uniform vec3 lightDir;

uniform float sunAngle;
uniform float starBrightness;

uniform float exposure;

uniform float g;
uniform float g2;

void main() {
    float cos = dot(normalize(lightDir), direction) / length(direction);
	float rayleighPhase = 0.75 * (2.0 + 0.5 * cos*cos);
	float miePhase = 1.5 * ((1.0 - g2) / (2.0 + g2)) * (1.0 + cos*cos) / pow(1.0 + g2 - 2.0 * g * cos, 1.5);

    vec3 finalColor = rayleighPhase * rayleighColor + miePhase * mieColor;

	fragColor.rgb = 1.0 - exp(-exposure * finalColor);

	float darkness = max(0.0, 0.16 - length(fragColor.rgb)) * 6.0;
	const float PI = 3.1415926;
	if (darkness > 0.05) {
		vec2 coord = vec2(((atan(texCoord0.y, texCoord0.x) + sunAngle) / PI + 1.0) * 0.5, asin(texCoord0.z) / PI + 0.5 );
		vec3 stars = texture(starmap, coord).rgb;
		stars = pow(stars, vec3(3.0)) * starBrightness;

		fragColor.rgb = mix(fragColor.rgb, stars, darkness * darkness);
	}
	fragColor.a = fragColor.b;
}