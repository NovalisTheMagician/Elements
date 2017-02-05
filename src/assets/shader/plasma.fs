#version 330

uniform float time;

const float PI = 3.1415926535897932384626433832795;

in vec2 pass_TexCoord;

out vec4 out_Color;

void main()
{
	float t = mod(time, 12 * PI);

	float value = 0.0;
    vec2 coord = pass_TexCoord * 16.0 - 8.0;
    
    float cx = coord.x;
    float cy = coord.y;
    
    value += sin(cx + t);
    value += sin((cy + t) / 2.0);
    value += sin((cx + cy + t) / 2.0);
    cx += 8.0 * sin(t / 3.0);
    cy += 8.0 * cos(t / 2.0);
    value += sin(sqrt(cx * cx + cy * cy + 1.0) + t);
    value = value / 2.0;
    
    vec3 col = vec3(1, sin(PI * value), cos(PI * value));

	out_Color = vec4(col, 1);
}
