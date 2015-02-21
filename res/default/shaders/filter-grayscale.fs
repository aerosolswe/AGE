#version 330

in vec2 texCoord0;
out vec4 fragColor;

uniform sampler2D filterTexture;

void main(){
    fragColor = texture(filterTexture, texCoord0) * 5.5;
}