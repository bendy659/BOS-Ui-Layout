#version 150

in vec2 screenPos;

uniform float Time;
uniform vec2 Size;
uniform vec2 Offset;
uniform vec4 Color;

out vec4 fragColor;

void main() {
    vec2 uv = (screenPos - Offset) / Size;
    float t = Time * 0.5;

    vec2 grid = fract(uv * 8.0) - 0.5;
    float lines = min(abs(grid.x), abs(grid.y));
    float gridGlow = 1.0 - smoothstep(0.0, 0.05, lines);

    vec3 gridColor = vec3(
        sin(t + uv.x * 3.0) * 0.5 + 0.5,
        sin(t * 1.3 + uv.y * 2.0) * 0.5 + 0.5,
        1.0
    );

    float bgGlow = sin(uv.x * 5.0 + t) * sin(uv.y * 3.0 + t * 0.7) * 0.15 + 0.05;
    vec3 bgColor = vec3(0.0, 0.2, 0.4) * bgGlow;

    vec3 col = bgColor + gridColor * gridGlow * 0.9;
    col *= Color.rgb;

    fragColor = vec4(col, Color.a * (gridGlow * 0.9 + bgGlow * 2.0));
}