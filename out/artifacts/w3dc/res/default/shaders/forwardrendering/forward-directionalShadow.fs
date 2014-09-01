#version 330

in vec4 shadowCoord0;
in vec4 shadowCoord1;
in vec4 shadowCoord2;
in vec4 shadowCoord3;

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

uniform sampler2D shadowMap0;
uniform sampler2D shadowMap1;
uniform sampler2D shadowMap2;
uniform sampler2D shadowMap3;

uniform vec3 eyePos;
uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform sampler2D bumpMap;

uniform float alpha;
uniform float specularIntensity;
uniform float specularPower;
uniform float scale;
uniform float bias;
uniform float shadowMapSize;

uniform DirectionalLight directionalLight;

float calcShadowFactor(){
    vec4 shadowCoord;
    float distanceToCamera = length(worldPos0 - eyePos);

    float offsetFactor;

    if(distanceToCamera < 2.5)
        shadowCoord = shadowCoord0;
    else if(distanceToCamera < 7.5)
        shadowCoord = shadowCoord1;
    else if(distanceToCamera < 30)
        shadowCoord = shadowCoord2;
    else
        shadowCoord = shadowCoord3;

    shadowCoord /= shadowCoord.w;

    float shadowFactor = 0.0;

    float PCFOffset = 1.0/shadowMapSize;//0.001;
    const float PCFFactor = 3.0;
    const float SHADOW_BIAS = 0.005;

    for(float i = -PCFOffset; i < PCFOffset; i += 2*PCFOffset/(PCFFactor)){
        for(float j = -PCFOffset; j < PCFOffset; j += 2*PCFOffset/(PCFFactor)){
            float shadowMapValue;

            if(distanceToCamera < 2.5)
                shadowMapValue = texture(shadowMap0, shadowCoord.xy + vec2(i,j)).r;
            else if(distanceToCamera < 7.5)
                shadowMapValue = texture(shadowMap1, shadowCoord.xy + vec2(i,j)).r;
            else if(distanceToCamera < 30)
                shadowMapValue = texture(shadowMap2, shadowCoord.xy + vec2(i,j)).r;
            else
                shadowMapValue = texture(shadowMap3, shadowCoord.xy + vec2(i,j)).r;

            if(shadowMapValue < (shadowCoord.z - SHADOW_BIAS))
                shadowFactor += 0.0;
            else
                shadowFactor += 1.0;
        }
    }
    shadowFactor /= (PCFFactor * PCFFactor);
    return shadowFactor;
}

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

    fragColor = textureColor * calcDirectionalLight(directionalLight, normalize(normal)) * calcShadowFactor();
}