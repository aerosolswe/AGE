#version 330

layout (location = 0) out vec3 worldPosOut;
layout (location = 1) out vec3 diffuseOut;
layout (location = 2) out vec3 normalOut;
layout (location = 3) out vec3 texCoordOut;

in vec2 texCoord0;                                                                  
in vec3 normal0;
in vec3 worldPos0;

uniform sampler2D colorMap;
											
void main(){
	worldPosOut = worldPos0;
	diffuseOut = texture(colorMap, texCoord0).xyz;
	normalOut = normalize(normal0);
	texCoordOut = vec3(texCoord0, 0.0);
}
