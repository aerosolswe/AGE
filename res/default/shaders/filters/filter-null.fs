#version 330

varying vec2 texCoord0;

uniform sampler2D filterTexture;

void main(){
    gl_FragColor = texture2D(filterTexture, texCoord0);
}