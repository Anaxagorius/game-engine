package com.gameengine.gui;

import com.gameengine.Scene;
import com.gameengine.Window;
import com.gameengine.collision.CollisionLog;
import com.gameengine.vehicles.Car;
import com.gameengine.vehicles.Plane;
import com.gameengine.vehicles.Vehicle;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

public class GameGui {

    private final Scene scene;
    private final Window window;
    private Vehicle lastVehicle;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final Random random = new Random();

    public GameGui(Scene scene, Window window) {
        this.scene = scene;
        this.window = window;
    }

    public void init() {
        ImGui.createContext();
        imGuiGlfw.init(window.getWindowHandle(), true);
        imGuiGl3.init("#version 330 core");
    }

    public void render(long windowHandle) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        // Vehicle Controls Panel
        ImGui.setNextWindowPos(10, 10, imgui.flag.ImGuiCond.Always);
        ImGui.setNextWindowSize(250, 400, imgui.flag.ImGuiCond.Always);
        ImGui.begin("Vehicle Controls", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);

        if (ImGui.button("Add Car")) {
            Car car = new Car();
            float x = (random.nextFloat() - 0.5f) * 10f;
            float z = (random.nextFloat() - 0.5f) * 10f;
            car.setPosition(x, 0, z);
            float vx = (random.nextFloat() - 0.5f) * 6f;
            float vz = (random.nextFloat() - 0.5f) * 6f;
            car.setVelocity(vx, 0, vz);
            scene.addItem(car);
            lastVehicle = car;
        }

        if (ImGui.button("Add Plane")) {
            Plane plane = new Plane();
            float x = (random.nextFloat() - 0.5f) * 10f;
            float y = 2f + random.nextFloat() * 6f;
            float z = (random.nextFloat() - 0.5f) * 10f;
            plane.setPosition(x, y, z);
            float vx = (random.nextFloat() - 0.5f) * 6f;
            float vy = (random.nextFloat() - 0.5f) * 6f;
            float vz = (random.nextFloat() - 0.5f) * 6f;
            plane.setVelocity(vx, vy, vz);
            scene.addItem(plane);
            lastVehicle = plane;
        }

        if (ImGui.button("Clear All")) {
            scene.removeAllVehicles();
            lastVehicle = null;
        }

        ImGui.separator();
        ImGui.text("Last Vehicle Controls:");

        if (ImGui.button("Faster")) {
            if (lastVehicle != null) {
                Vector3f vel = lastVehicle.getVelocity();
                Vector3f dir;
                if (vel.lengthSquared() > 0.001f) {
                    dir = new Vector3f(vel).normalize();
                } else {
                    dir = new Vector3f(0, 0, -1);
                }
                lastVehicle.accelerate(dir);
            }
        }

        if (ImGui.button("Slower")) {
            if (lastVehicle != null) {
                lastVehicle.getVelocity().mul(0.8f);
            }
        }

        if (ImGui.button("Left")) {
            if (lastVehicle != null) {
                lastVehicle.accelerate(new Vector3f(-1, 0, 0));
            }
        }

        if (ImGui.button("Right")) {
            if (lastVehicle != null) {
                lastVehicle.accelerate(new Vector3f(1, 0, 0));
            }
        }

        if (ImGui.button("Up")) {
            if (lastVehicle != null) {
                lastVehicle.accelerate(new Vector3f(0, 1, 0));
            }
        }

        if (ImGui.button("Down")) {
            if (lastVehicle != null) {
                lastVehicle.accelerate(new Vector3f(0, -1, 0));
            }
        }

        ImGui.separator();
        ImGui.text("Collision Log:");

        List<String> allLogs = CollisionLog.getLogs();
        int start = Math.max(0, allLogs.size() - 5);
        for (int i = start; i < allLogs.size(); i++) {
            ImGui.textWrapped(allLogs.get(i));
        }

        ImGui.end();

        // Instructions panel (top-right)
        float panelWidth = 200;
        ImGui.setNextWindowPos(window.getWidth() - panelWidth - 10, 10, imgui.flag.ImGuiCond.Always);
        ImGui.setNextWindowSize(panelWidth, 80, imgui.flag.ImGuiCond.Always);
        ImGui.begin("Instructions", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.text("T: Start collision test");
        ImGui.text("ESC: Exit");
        ImGui.end();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public void cleanup() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    public void setLastVehicle(Vehicle v) {
        this.lastVehicle = v;
    }
}
