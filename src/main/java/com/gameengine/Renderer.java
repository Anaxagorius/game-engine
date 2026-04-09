package com.gameengine;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private ShaderProgram shaderProgram;

    public void init() {
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.1f, 0.15f, 1.0f);
        shaderProgram = new ShaderProgram("/shaders/vertex.glsl", "/shaders/fragment.glsl");
    }

    public void render(Scene scene, Camera camera) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shaderProgram.bind();

        Matrix4f projMatrix = camera.getProjectionMatrix(60f, 800f, 600f);
        Matrix4f viewMatrix = camera.getViewMatrix();

        shaderProgram.setUniform("projMatrix", projMatrix);
        shaderProgram.setUniform("viewMatrix", viewMatrix);

        for (GameItem item : scene.getItems()) {
            shaderProgram.setUniform("modelMatrix", item.getModelMatrix());
            shaderProgram.setUniform("color", item.getColor());
            item.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
