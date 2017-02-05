#version 330 core

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
	float specular;
	float roughness;
};

uniform Light[4] lights;
uniform Material material;

in vec3 pass_Normal;
in vec2 pass_TexCoord;
in vec3 pass_FragPos;

out vec4 out_Color;

vec3 calculateLight(Light light, Material mat, vec3 normal, vec3 viewDir, vec3 fragPos, vec2 texCoord)
{
	vec3 lightDir = normalize(light.position - fragPos);
	vec3 halfWayDir = normalize(lightDir + viewDir);

	float diff = max(dot(normal, lightDir), 0.0);
	float spec = pow(max(dot(normal, halfWayDir), 0.0), mat.roughness);

	vec3 diffTex = texture(mat.diffuseMap, texCoord).rgb;

	vec3 ambient = light.ambient * diffTex;
	vec3 diffuse = light.diffuse * diff * diffTex;
	vec3 specular = light.specular * spec * mat.specular;
	
	return ambient + diffuse + specular;
}

void main(void) 
{
	vec3 normal = normalize(pass_Normal);
	vec3 viewDir = normalize(viewPos - pass_FragPos);
	
	vec3 color = vec3(0, 0, 0);
	for(int i = 0; i < 4; i++)
	{
		color += calculateLight(lights[i], material, normal, viewDir, pass_FragPos, pass_TexCoord);
	}

	out_Color = vec4(color, 1.0);
}
