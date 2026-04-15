# 3D Game Engine

A lightweight 3D game engine built with Java 17, OpenGL 3.3 (via LWJGL), and Dear ImGui. It features a free-fly camera, Blinn-Phong shading, axis-aligned bounding box (AABB) collision detection, and an in-engine GUI for spawning and controlling vehicles in real time.

## Features

- **OpenGL 3.3 Core Profile** rendering with a custom GLSL shader pipeline
- **Blinn-Phong lighting** – ambient, diffuse, and specular components
- **Free-fly camera** – keyboard-driven translation and rotation
- **Box mesh generation** – VAO/VBO geometry with per-face flat normals
- **Scene graph** – hierarchical list of `GameItem` objects with transform matrices
- **Vehicle simulation** – `Car` (ground) and `Plane` (aerial) with AABB collision response and visual flash feedback
- **Collision logging** – structured log of every collision event via SLF4J / Logback
- **Dear ImGui HUD** – live vehicle spawner, controls panel, stats, and collision log overlay
- **Wireframe toggle** – press `F` to switch between solid and wireframe rendering
- **FPS counter** – displayed in the window title bar

## Requirements

| Requirement | Version |
|-------------|---------|
| Java JDK | 17 or later |
| Maven | 3.8 or later |
| OS | Linux or Windows (macOS not currently supported) |
| GPU | OpenGL 3.3 Core Profile support |

## Building

```bash
mvn package
```

This produces a fat/shaded JAR at:

```
target/game-engine-1.0-SNAPSHOT.jar
```

## Running

```bash
java -jar target/game-engine-1.0-SNAPSHOT.jar
```

The engine opens an 800 × 600 window titled **"3D Game Engine"** and renders a green ground plane. The window is resizable.

## Controls

### Camera

| Key | Action |
|-----|--------|
| `W` / `S` | Move forward / backward |
| `A` / `D` | Strafe left / right |
| `Q` / `E` | Move up / down |
| `↑` / `↓` | Look up / down (pitch) |
| `←` / `→` | Look left / right (yaw) |

### Scene

| Key | Action |
|-----|--------|
| `T` | Spawn a preset collision test (2 cars + 2 planes) |
| `F` | Toggle wireframe rendering |
| `ESC` | Exit |

### ImGui Vehicle Controls Panel (top-left)

| Button | Action |
|--------|--------|
| **Add Car** | Spawn a car at a random position with a random velocity |
| **Add Plane** | Spawn a plane at a random position with a random velocity |
| **Clear All** | Remove all vehicles from the scene |
| **Faster** / **Slower** | Accelerate / decelerate the last-spawned vehicle |
| **Left** / **Right** / **Fwd** / **Back** | Apply an impulse to the last-spawned vehicle |
| **Up** / **Down** | Apply a vertical impulse to the last-spawned vehicle |

The panel also shows live position / velocity of the last vehicle and a scrolling log of the five most recent collision events.

## Project Structure

```
src/main/java/com/gameengine/
├── Main.java              # Entry point
├── GameEngine.java        # Main loop, input handling, scene setup
├── Window.java            # GLFW window, key/mouse callbacks
├── Camera.java            # Free-fly camera (view & projection matrices)
├── Renderer.java          # OpenGL render loop, shader binding, wireframe toggle
├── ShaderProgram.java     # GLSL shader compilation and uniform setters
├── Mesh.java              # VAO/VBO box mesh builder and renderer
├── GameItem.java          # Base scene entity (position, rotation, scale, color, AABB)
├── Scene.java             # Scene graph: item list, vehicle update loop, collision checks
├── collision/
│   └── CollisionLog.java  # Static AABB collision log (SLF4J-backed)
├── gui/
│   └── GameGui.java       # Dear ImGui HUD – vehicle panel + controls reference
└── vehicles/
    ├── Vehicle.java        # Abstract vehicle (velocity, acceleration, collision response)
    ├── Car.java            # Ground vehicle – orange, bounded XZ movement
    └── Plane.java          # Aerial vehicle – cyan, full 3D bounded movement

src/main/resources/shaders/
├── vertex.glsl            # Transforms vertices; outputs world position and normal
└── fragment.glsl          # Blinn-Phong lighting; writes final RGBA colour
```

## Architecture Overview

```
Main
 └─ GameEngine.run()
      ├─ init()   – creates Window, Renderer, Camera, Scene, GameGui
      ├─ loop()   – per-frame: processInput → update → render
      └─ cleanup()

Scene.update(dt)
  ├─ Vehicle.update(dt)       – flash timer + updateMovement()
  └─ pairwise Vehicle.collide() – AABB overlap → onCollision() + CollisionLog

Renderer.render(scene, camera)
  ├─ bind ShaderProgram
  ├─ upload projection / view / lighting uniforms
  └─ for each GameItem: upload model matrix + colour → Mesh.render()

GameGui.render()
  └─ ImGui panels (vehicle controls, stats, collision log, key bindings)
```

## Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| [LWJGL](https://www.lwjgl.org/) | 3.3.3 | OpenGL and GLFW bindings |
| [JOML](https://joml-ci.github.io/JOML/) | 1.10.5 | Vector / matrix math |
| [imgui-java](https://github.com/SpaiR/imgui-java) | 1.86.11 | Dear ImGui Java binding |
| [SLF4J](https://www.slf4j.org/) | 2.0.9 | Logging facade |
| [Logback](https://logback.qos.ch/) | 1.4.12 | Logging implementation |

Native binaries for both **Linux** and **Windows** are bundled automatically by Maven.
