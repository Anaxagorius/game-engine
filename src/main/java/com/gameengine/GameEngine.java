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

    private static final float CAMERA_SPEED     = 10f;
    private static final float CAMERA_ROT_SPEED = 60f;

    private Window window;
    private Renderer renderer;
    private Scene scene;
    private Camera camera;
    private GameGui gui;

    private long lastTime;
    private int   fpsFrameCount;
    private float fpsTimer;

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
        scene  = new Scene();

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

            processInput(deltaTime);
            update(deltaTime);
            render();
        }
    }

    private void processInput(float deltaTime) {
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            window.setShouldClose(true);
        }
        if (window.isKeyJustPressed(GLFW_KEY_T)) {
            startCollisionTest();
        }
        if (window.isKeyJustPressed(GLFW_KEY_F)) {
            renderer.toggleWireframe();
        }

        // Camera translation – WASD + Q/E
        float camSpeed = CAMERA_SPEED * deltaTime;
        if (window.isKeyPressed(GLFW_KEY_W)) camera.moveForward(camSpeed);
        if (window.isKeyPressed(GLFW_KEY_S)) camera.moveBackward(camSpeed);
        if (window.isKeyPressed(GLFW_KEY_A)) camera.strafeLeft(camSpeed);
        if (window.isKeyPressed(GLFW_KEY_D)) camera.strafeRight(camSpeed);
        if (window.isKeyPressed(GLFW_KEY_Q)) camera.moveUp(camSpeed);
        if (window.isKeyPressed(GLFW_KEY_E)) camera.moveDown(camSpeed);

        // Camera rotation – arrow keys
        float rotSpeed = CAMERA_ROT_SPEED * deltaTime;
        if (window.isKeyPressed(GLFW_KEY_UP))    camera.adjustPitch(-rotSpeed);
        if (window.isKeyPressed(GLFW_KEY_DOWN))  camera.adjustPitch(rotSpeed);
        if (window.isKeyPressed(GLFW_KEY_LEFT))  camera.adjustYaw(-rotSpeed);
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) camera.adjustYaw(rotSpeed);
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

        // FPS counter – update window title once per second
        fpsFrameCount++;
        fpsTimer += deltaTime;
        if (fpsTimer >= 1.0f) {
            int fps = Math.round(fpsFrameCount / fpsTimer);
            window.setTitle("3D Game Engine | FPS: " + fps
                    + (renderer.isWireframe() ? " [Wireframe]" : ""));
            fpsFrameCount = 0;
            fpsTimer = 0f;
        }
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
