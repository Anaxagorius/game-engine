package com.gameengine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private static final float MIN_PITCH = -89f;
    private static final float MAX_PITCH =  89f;

    private final Vector3f position;
    private float pitch;
    private float yaw;

    public Camera() {
        position = new Vector3f(0, 15, 30);
        pitch = -25f;
        yaw = 0f;
    }

    public Matrix4f getViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(yaw),   new Vector3f(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix(float fov, float width, float height) {
        return new Matrix4f().perspective(
                (float) Math.toRadians(fov),
                width / height,
                0.1f,
                1000.0f
        );
    }

    // ---------------------------------------------------------------
    //  Movement helpers
    // ---------------------------------------------------------------

    /** Move along the horizontal look direction (no vertical component). */
    public void moveForward(float amount) {
        position.x += (float) Math.sin(Math.toRadians(yaw)) * amount;
        position.z -= (float) Math.cos(Math.toRadians(yaw)) * amount;
    }

    public void moveBackward(float amount) {
        moveForward(-amount);
    }

    /** Strafe right (perpendicular to forward in the XZ plane). */
    public void strafeRight(float amount) {
        position.x += (float) Math.cos(Math.toRadians(yaw)) * amount;
        position.z += (float) Math.sin(Math.toRadians(yaw)) * amount;
    }

    public void strafeLeft(float amount) {
        strafeRight(-amount);
    }

    public void moveDown(float amount) {
        position.y -= amount;
    }

    public void moveUp(float amount) {
        position.y += amount;
    }

    /** Tilt up/down; clamped to ±89°. */
    public void adjustPitch(float delta) {
        pitch = Math.max(MIN_PITCH, Math.min(MAX_PITCH, pitch + delta));
    }

    /** Rotate left/right; result is always in [0, 360). */
    public void adjustYaw(float delta) {
        yaw = ((yaw + delta) % 360f + 360f) % 360f;
    }

    // ---------------------------------------------------------------
    //  Getters / setters
    // ---------------------------------------------------------------

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = Math.max(MIN_PITCH, Math.min(MAX_PITCH, pitch));
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
