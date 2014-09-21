#version 330

in vec3 worldPos0;

uniform vec3 lightWorldPos;
          
out float fragColor;
        
void main()
{
    vec3 lightToVertex = worldPos0 - lightWorldPos;

    float lightToPixelDistance = length(lightToVertex);

    fragColor = lightToPixelDistance;
}

