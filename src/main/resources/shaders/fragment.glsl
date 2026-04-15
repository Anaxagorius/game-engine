#version 330 core
uniform vec4 color;
uniform vec3 lightDir;
uniform vec3 lightColor;
uniform vec3 ambientColor;
uniform vec3 viewPos;

in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColor;

void main() {
    vec3 norm        = normalize(fragNormal);
    vec3 toLight     = normalize(-lightDir);

    // Diffuse
    float diff   = max(dot(norm, toLight), 0.0);
    vec3  diffuse = diff * lightColor;

    // Specular (Blinn-Phong)
    vec3  viewDir   = normalize(viewPos - fragPos);
    vec3  halfDir   = normalize(toLight + viewDir);
    float spec      = pow(max(dot(norm, halfDir), 0.0), 48.0);
    vec3  specular  = 0.25 * spec * lightColor;

    vec3 result = (ambientColor + diffuse + specular) * color.rgb;
    fragColor   = vec4(result, color.a);
}
