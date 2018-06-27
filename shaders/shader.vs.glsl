#version 120

attribute vec3 vertices;

uniform mat4 projection;
uniform mat4 scale;

void main() {

    gl_Position = (projection * scale) * vec4(vertices, 1);
    //gl_Position = vec4(vertices, 1);

}
