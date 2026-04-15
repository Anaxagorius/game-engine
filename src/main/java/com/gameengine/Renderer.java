package com.gameengine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final Vector3f LIGHT_DIR     = new Vector3f(-0.6f, -1.0f, -0.5f);
    private static final Vector3f LIGHT_COLOR   = new Vector3f(1.0f, 1.0f, 0.95f);
    private static final Vector3f AMBIENT_COLOR = new Vector3f(0.25f, 0.25f, 0.30f);

    private ShaderProgram shaderProgram;
    private Window window;
    private boolean wireframe = false;

    public void init(Window window) {
        this.window = window;
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.1f, 0.15f, 1.0f);
        shaderProgram = new ShaderProgram("/shaders/vertex.glsl", "/shaders/fragment.glsl");
    }

    public void render(Scene scene, Camera camera) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shaderProgram.bind();

        Matrix4f projMatrix = camera.getProjectionMatrix(60f, window.getWidth(), window.getHeight());
        Matrix4f viewMatrix = camera.getViewMatrix();

        shaderProgram.setUniform("projMatrix", projMatrix);
        shaderProgram.setUniform("viewMatrix", viewMatrix);
        shaderProgram.setUniform("lightDir", LIGHT_DIR);
        shaderProgram.setUniform("lightColor", LIGHT_COLOR);
        shaderProgram.setUniform("ambientColor", AMBIENT_COLOR);
        shaderProgram.setUniform("viewPos", camera.getPosition());

        for (GameItem item : scene.getItems()) {
            shaderProgram.setUniform("modelMatrix", item.getModelMatrix());
            shaderProgram.setUniform("color", item.getColor());
            item.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void toggleWireframe() {
        wireframe = !wireframe;
        glPolygonMode(GL_FRONT_AND_BACK, wireframe ? GL_LINE : GL_FILL);
    }

    public boolean isWireframe() {
        return wireframe;
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
