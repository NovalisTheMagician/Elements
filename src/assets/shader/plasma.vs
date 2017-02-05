#version 330

uniform mat4 world;
uniform mat4 view;
uniform mat4 proj;

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec2 in_TexCoord;

out vec2 pass_TexCoord;

void main() 
{
	mat4 wvp = proj * view * world;
	gl_Position = wvp * vec4(in_Position, 1);

	pass_TexCoord = in_TexCoord;
}

