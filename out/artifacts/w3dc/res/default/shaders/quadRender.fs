#version 330

in vec2 texCoord0;

out vec4 fragColor;

uniform vec3 color;
uniform sampler2D sampler;

void main(){
    vec4 textureColor = texture(sampler, texCoord0.xy);

    vec4 finalColor = textureColor * vec4(color, 1);

    //float intensity = dot(finalColor.rgb, vec3(0.29, 0.59, 0.12));

    //if(intensity>0.99){
    //    fragColor = vec4(1.0, 1.0, 1.0, 1.0);
    //}else{
    //    fragColor = vec4(0.0, 0.0, 0.0, 1.0);
    //}


    //finalColor = vec4(vec3(intensity,intensity,intensity), 1);

    fragColor = finalColor;
}