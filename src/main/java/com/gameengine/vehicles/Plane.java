package com.gameengine.vehicles;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Plane extends Vehicle {

    private static final float BOUND_X_MIN = -10f;
    private static final float BOUND_X_MAX = 10f;
    private static final float BOUND_Y_MIN = 0f;
    private static final float BOUND_Y_MAX = 10f;
    private static final float BOUND_Z_MIN = -10f;
    private static final float BOUND_Z_MAX = 10f;

    public Plane() {
        super(PLANE_MESH, new Vector4f(0.0f, 0.8f, 1.0f, 1.0f));
        onGround = false;
    }

    @Override
    public void updateMovement(float deltaTime) {
        Vector3f pos = getPosition();
        pos.x += velocity.x * deltaTime;
        pos.y += velocity.y * deltaTime;
        pos.z += velocity.z * deltaTime;

        if (pos.x < BOUND_X_MIN) {
            pos.x = BOUND_X_MIN;
            velocity.x = -velocity.x;
        } else if (pos.x > BOUND_X_MAX) {
            pos.x = BOUND_X_MAX;
            velocity.x = -velocity.x;
        }

        if (pos.y < BOUND_Y_MIN) {
            pos.y = BOUND_Y_MIN;
            velocity.y = -velocity.y;
        } else if (pos.y > BOUND_Y_MAX) {
            pos.y = BOUND_Y_MAX;
            velocity.y = -velocity.y;
        }

        if (pos.z < BOUND_Z_MIN) {
            pos.z = BOUND_Z_MIN;
            velocity.z = -velocity.z;
        } else if (pos.z > BOUND_Z_MAX) {
            pos.z = BOUND_Z_MAX;
            velocity.z = -velocity.z;
        }

        setPosition(pos.x, pos.y, pos.z);
    }
}
