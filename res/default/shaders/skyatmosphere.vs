#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec3 texCoord0;
out vec3 direction;
out vec3 rayleighColor;
out vec3 mieColor;

uniform mat4 MVP;

uniform vec3 lightDir;

uniform float krESun;
uniform float kmESun;
uniform float kr4PI;
uniform float km4PI;

uniform float innerRadius;
uniform float domeRadius;

uniform float scale;
uniform float scaleDepth;
uniform float scaleOverScaleDepth;

uniform vec3 cameraPos;

uniform float cameraHeight;

const vec3 wavelength = pow(vec3(0.650, 0.570, 0.475), vec3(4.0));
const vec3 invWavelength = vec3(1.0) / wavelength;

#define nSamples 6
#define samples 6.0

float scaleWith(float cos){
	float x = 1.0 - cos;
	return scaleDepth * exp(-0.00287 + x*(0.459 + x*(3.83 + x*(-6.80 + x*5.25))));
}

void main(){
    vec3 pos = position / domeRadius;
    pos.y += innerRadius;

    vec3 ray = pos - cameraPos;
    float far = length(ray);
    ray /= far;

	vec3 start = cameraPos;
	float height = length(start);
	float depth = exp(scaleOverScaleDepth * (innerRadius - cameraHeight));
	float startAngle = dot(ray, start) / height;
	float startOffset = depth * scaleWith(startAngle);

	float sampleLength = far / samples;
	float scaledLength = sampleLength * scale;
	vec3 sampleRay = ray * sampleLength;
	vec3 samplePoint = start + sampleRay * 0.5;

	vec3 frontColor = vec3(0.0);
	vec3 attenuate;

	for(int i = 0; i < nSamples; i++){
		float height = length(samplePoint);
		float depth = exp(scaleOverScaleDepth * (innerRadius - height));

		float lightAngle = dot(lightDir, samplePoint) / height;
		float cameraAngle = dot(ray, samplePoint) / height;

		float scatter = (startOffset + depth*(scaleWith(lightAngle) - scaleWith(cameraAngle)));
		attenuate = exp(-scatter * (invWavelength * kr4PI + km4PI));

		frontColor += attenuate * (depth * scaledLength);
		samplePoint += sampleRay;
	}

	mieColor.rgb = frontColor * kmESun;
	rayleighColor.rgb = frontColor * (invWavelength * krESun);

    vec4 MVP_Pos = MVP * vec4(position, 1.0);
    gl_Position = MVP_Pos.xyww;
    texCoord0 = position;

	direction = cameraPos - pos;
}
