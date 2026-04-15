package com.gameengine.gui;

import com.gameengine.Scene;
import com.gameengine.Window;
import com.gameengine.collision.CollisionLog;
import com.gameengine.vehicles.Car;
import com.gameengine.vehicles.Plane;
import com.gameengine.vehicles.Vehicle;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
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
    private final ImGuiImplGl3  imGuiGl3  = new ImGuiImplGl3();
    private final Random random = new Random();

    public GameGui(Scene scene, Window window) {
        this.scene  = scene;
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

        // ── Vehicle Controls Panel ────────────────────────────────────
        ImGui.setNextWindowPos(10, 10, ImGuiCond.Always);
        ImGui.setNextWindowSize(260, 440, ImGuiCond.Always);
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

        ImGui.sameLine();

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

        // Vehicle stats
        ImGui.separator();
        long carCount   = scene.getVehicles().stream().filter(v -> v instanceof Car).count();
        long planeCount = scene.getVehicles().stream().filter(v -> v instanceof Plane).count();
        ImGui.text(String.format("Cars: %d   Planes: %d   Total: %d",
                carCount, planeCount, scene.getVehicles().size()));

        // Last-vehicle controls
        ImGui.separator();
        ImGui.text("Last Vehicle Controls:");

        if (ImGui.button("Faster")) {
            if (lastVehicle != null) {
                Vector3f vel = lastVehicle.getVelocity();
                Vector3f dir = vel.lengthSquared() > 0.001f
                        ? new Vector3f(vel).normalize()
                        : new Vector3f(0, 0, -1);
                lastVehicle.accelerate(dir);
            }
        }
        ImGui.sameLine();
        if (ImGui.button("Slower")) {
            if (lastVehicle != null) {
                lastVehicle.getVelocity().mul(0.8f);
            }
        }

        if (ImGui.button("Left"))  { if (lastVehicle != null) lastVehicle.accelerate(new Vector3f(-1, 0,  0)); }
        ImGui.sameLine();
        if (ImGui.button("Right")) { if (lastVehicle != null) lastVehicle.accelerate(new Vector3f( 1, 0,  0)); }
        ImGui.sameLine();
        if (ImGui.button("Fwd"))   { if (lastVehicle != null) lastVehicle.accelerate(new Vector3f( 0, 0, -1)); }
        ImGui.sameLine();
        if (ImGui.button("Back"))  { if (lastVehicle != null) lastVehicle.accelerate(new Vector3f( 0, 0,  1)); }

        if (ImGui.button("Up"))   { if (lastVehicle != null) lastVehicle.accelerate(new Vector3f(0,  1, 0)); }
        ImGui.sameLine();
        if (ImGui.button("Down")) { if (lastVehicle != null) lastVehicle.accelerate(new Vector3f(0, -1, 0)); }

        if (lastVehicle != null) {
            Vector3f p = lastVehicle.getPosition();
            Vector3f v = lastVehicle.getVelocity();
            ImGui.text(String.format("Pos: (%.1f, %.1f, %.1f)", p.x, p.y, p.z));
            ImGui.text(String.format("Vel: (%.2f, %.2f, %.2f)", v.x, v.y, v.z));
        }

        // Collision log
        ImGui.separator();
        ImGui.text("Collision Log:");

        List<String> allLogs = CollisionLog.getLogs();
        int start = Math.max(0, allLogs.size() - 5);
        for (int i = start; i < allLogs.size(); i++) {
            ImGui.textWrapped(allLogs.get(i));
        }

        ImGui.end();

        // ── Instructions Panel (top-right) ───────────────────────────
        float panelWidth = 230;
        float panelHeight = 155;
        ImGui.setNextWindowPos(window.getWidth() - panelWidth - 10, 10, ImGuiCond.Always);
        ImGui.setNextWindowSize(panelWidth, panelHeight, ImGuiCond.Always);
        ImGui.begin("Controls", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.text("Camera:");
        ImGui.text("  WASD       Move");
        ImGui.text("  Q/E        Up / Down");
        ImGui.text("  Arrow Keys Look");
        ImGui.separator();
        ImGui.text("Scene:");
        ImGui.text("  T          Run collision test");
        ImGui.text("  F          Toggle wireframe");
        ImGui.text("  ESC        Exit");
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
