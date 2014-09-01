#version 330

in vec2 texCoord0;
in vec3 normal0;
in vec3 worldPos0;
in mat3 tbnMatrix;

out vec4 fragColor;

struct BaseLight{
    vec3 color;
    float intensity;
};

struct DirectionalLight{
    BaseLight base;
    vec3 direction;
};

uniform vec3 eyePos;
uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform sampler2D bumpMap;

uniform float specularIntensity;
uniform float specularPower;
uniform float scale;
uniform float bias;
uniform float alpha;


uniform DirectionalLight directionalLight;

vec4 calcLight(BaseLight base, vec3 direction, vec3 normal){
    float diffuseFactor = dot(normal, -direction);
    
    vec4 diffuseColor = vec4(0,0,0,0);
    vec4 specularColor = vec4(0,0,0,0);
    
    if(diffuseFactor > 0)
    {
        diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
        
        vec3 directionToEye = normalize(eyePos - worldPos0);
        vec3 reflectDirection = normalize(reflect(direction, normal));
        
        float specularFactor = dot(directionToEye, reflectDirection);
        specularFactor = pow(specularFactor, specularPower);
        
        if(specularFactor > 0)
        {
            specularColor = vec4(base.color, 1.0) * specularIntensity * specularFactor;
        }
    }
    
    return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal){
    return calcLight(directionalLight.base, -directionalLight.direction, normal);
}

void main(){
    vec2 texCoords = texCoord0.xy + ((normalize(eyePos - worldPos0) * tbnMatrix).xy *
                                (texture(bumpMap, texCoord0.xy).r * scale + bias));

    vec3 normal = normalize(tbnMatrix * (2.0 * texture(normalMap, texCoords).xyz - 1.0));

    vec4 textureColor = texture(diffuse, texCoords);

    if(textureColor.a < 0.5)
           discard;

    fragColor = textureColor * calcDirectionalLight(directionalLight, normalize(normal));
}