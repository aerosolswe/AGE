#version 330

struct BaseLight{
    vec3 color;
    float intensity;
};

struct DirectionalLight{
    BaseLight base;
    vec3 direction;
};

struct Attenuation{
    float constant;
    float linear;
    float exponent;
};

struct PointLight{
    BaseLight base;
    vec3 position;
    Attenuation atten;
    float range;
};

struct SpotLight{
    PointLight base;
    vec3 direction;
    float cutoff;
};

uniform sampler2D positionMap;
uniform sampler2D colorMap;
uniform sampler2D normalMap;
uniform DirectionalLight directionalLight;
uniform PointLight pointLight;
uniform SpotLight spotLight;
uniform vec3 eyePos;
uniform float specularIntensity;
uniform float specularPower;
uniform vec2 screenSize;

vec4 calcLightInternal(BaseLight light,
					   vec3 lightDirection,
					   vec3 worldPos,
					   vec3 normal){
    vec4 ambientColor = vec4(light.color, 1.0) * light.intensity;
    float diffuseFactor = dot(normal, -lightDirection);

    vec4 diffuseColor  = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);

    if (diffuseFactor > 0.0) {
        diffuseColor = vec4(light.color, 1.0) * light.intensity * diffuseFactor;

        vec3 vertexToEye = normalize(eyePos - worldPos);
        vec3 lightReflect = normalize(reflect(lightDirection, normal));
        float specularFactor = dot(vertexToEye, lightReflect);
        specularFactor = pow(specularFactor, specularPower);
        if (specularFactor > 0.0) {
            specularColor = vec4(light.color, 1.0) * specularIntensity * specularFactor;
        }
    }

    return (ambientColor + diffuseColor + specularColor);
}

vec4 calcDirectionalLight(vec3 worldPos, vec3 normal){
    return calcLightInternal(directionalLight.base,
							 directionalLight.direction,
							 worldPos,
							 normal);
}

vec4 calcPointLight(vec3 worldPos, vec3 normal){
    vec3 lightDirection = worldPos - pointLight.position;
    float distance = length(lightDirection);
    lightDirection = normalize(lightDirection);

    if(distance > pointLight.range)
            return vec4(0,0,0,0);

    vec4 color = calcLightInternal(pointLight.base, lightDirection, worldPos, normal);

    float attenuation =  pointLight.atten.constant +
                         pointLight.atten.linear * distance +
                         pointLight.atten.exponent * distance * distance;

    attenuation = max(1.0, attenuation);

    return color / attenuation;
}


vec2 calcTexCoord(){
    return gl_FragCoord.xy / screenSize;
}

out vec4 fragColor;

void main(){
    vec2 texCoord = calcTexCoord();
	vec3 worldPos = texture(positionMap, texCoord).xyz;
	vec3 color = texture(colorMap, texCoord).xyz;
	vec3 normal = texture(normalMap, texCoord).xyz;
	normal = normalize(normal);

	fragColor = vec4(color, 1.0) * calcDirectionalLight(worldPos, normal);
}
