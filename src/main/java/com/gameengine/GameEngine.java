package com.gameengine;

import com.gameengine.gui.GameGui;
import com.gameengine.vehicles.Car;
import com.gameengine.vehicles.Plane;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.*;

public class GameEngine {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private Window window;
    private Renderer renderer;
    private Scene scene;
    private Camera camera;
    private GameGui gui;

    private long lastTime;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        window = new Window(800, 600, "3D Game Engine");
        window.init();

        renderer = new Renderer();
        renderer.init(window);

        camera = new Camera();
        scene = new Scene();

        // Ground plane
        Mesh groundMesh = Mesh.createBox(22f, 0.1f, 22f);
        GameItem ground = new GameItem(groundMesh, new Vector4f(0.3f, 0.5f, 0.3f, 1.0f));
        ground.setPosition(0, -0.1f, 0);
        scene.addItem(ground);

        gui = new GameGui(scene, window);
        gui.init();

        lastTime = System.nanoTime();
    }

    private void loop() {
        while (!window.shouldClose()) {
            long now = System.nanoTime();
            float deltaTime = (now - lastTime) / 1_000_000_000f;
            lastTime = now;

            processInput();
            update(deltaTime);
            render();
        }
    }

    private void processInput() {
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            window.setShouldClose(true);
        }
        if (window.isKeyPressed(GLFW_KEY_T)) {
            startCollisionTest();
        }
    }

    private void startCollisionTest() {
        scene.removeAllVehicles();

        Car car1 = new Car();
        car1.setPosition(-5, 0, 0);
        car1.setVelocity(2, 0, 0);

        Car car2 = new Car();
        car2.setPosition(5, 0, 0);
        car2.setVelocity(-2, 0, 0);

        Plane plane1 = new Plane();
        plane1.setPosition(-5, 5, 0);
        plane1.setVelocity(2, 0, 0);

        Plane plane2 = new Plane();
        plane2.setPosition(5, 5, 0);
        plane2.setVelocity(-2, 0, 0);

        scene.addItem(car1);
        scene.addItem(car2);
        scene.addItem(plane1);
        scene.addItem(plane2);

        logger.info("Collision test started");
    }

    private void update(float deltaTime) {
        scene.update(deltaTime);
    }

    private void render() {
        renderer.render(scene, camera);
        gui.render(window.getWindowHandle());
        window.update();
    }

    private void cleanup() {
        gui.cleanup();
        renderer.cleanup();
        window.cleanup();
    }
}
