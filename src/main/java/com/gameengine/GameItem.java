package com.gameengine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GameItem {

    private final Vector3f position;
    private final Vector3f rotation;
    private final Vector3f scale;
    private final Mesh mesh;
    private final Vector4f color;

    public GameItem(Mesh mesh, Vector4f color) {
        this.mesh = mesh;
        this.color = new Vector4f(color);
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
    }

    public Matrix4f getModelMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.identity()
                .translate(position)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
        return matrix;
    }

    /**
     * Returns bounding box as float[6]: [minX, maxX, minY, maxY, minZ, maxZ]
     */
    public float[] getBounds() {
        float hw = (mesh.getWidth() * scale.x) / 2f;
        float hh = (mesh.getHeight() * scale.y) / 2f;
        float hd = (mesh.getDepth() * scale.z) / 2f;
        return new float[]{
            position.x - hw, position.x + hw,
            position.y - hh, position.y + hh,
            position.z - hd, position.z + hd
        };
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.set(x, y, z);
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(float x, float y, float z) {
        scale.set(x, y, z);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
    }
}
