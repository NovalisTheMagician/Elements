#version 330 core

uniform mat4 world;
uniform mat4 view;
uniform mat4 proj;

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec3 in_Normal;
layout(location = 2) in vec2 in_TexCoord;

out vec3 pass_Normal;
out vec2 pass_TexCoord;
out vec3 pass_FragPos;

void main() 
{
	mat4 wvp = proj * view * world;
	gl_Position = wvp * vec4(in_Position, 1);

	pass_Normal = mat3(world) * in_Normal;
	pass_TexCoord = in_TexCoord;
	pass_FragPos = (world * vec4(in_Position, 1)).xyz;
}
