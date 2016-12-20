#version 150 core

uniform vec3 viewPos;

struct Light 
{
	vec3 position;
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

struct Material 
{
	sampler2D diffuseMap;
	sampler2D specularMap;
	float roughness;
};

uniform Light light;
uniform Material material;

in vec3 pass_Normal;
in vec2 pass_TexCoord;
in vec3 pass_FragPos;

out vec4 out_Color;

void main(void) 
{
	vec3 normal = normalize(pass_Normal);
	vec3 lightDir = normalize(light.position - pass_FragPos);
	vec3 viewDir = normalize(viewPos - pass_FragPos);
	vec3 halfWayDir = normalize(lightDir + viewDir);

	float diff = max(dot(normal, lightDir), 0.0);
	float spec = pow(max(dot(normal, halfWayDir), 0.0), material.roughness);

	vec3 diffTex = texture(material.diffuseMap, pass_TexCoord).rgb;
	vec3 specTex = texture(material.specularMap, pass_TexCoord).rgb;

	vec3 ambient = light.ambient * diffTex;
	vec3 diffuse = light.diffuse * diff * diffTex;
	vec3 specular = light.specular * spec * specTex;

	out_Color = vec4(ambient + diffuse + specular, 1.0);
}
