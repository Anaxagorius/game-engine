package com.gameengine.vehicles;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Car extends Vehicle {

    private static final float BOUND_X_MIN = -10f;
    private static final float BOUND_X_MAX = 10f;
    private static final float BOUND_Z_MIN = -10f;
    private static final float BOUND_Z_MAX = 10f;
    private static final float GROUND_Y = 0f;

    public Car() {
        super(CAR_MESH, new Vector4f(1.0f, 0.5f, 0.0f, 1.0f));
        onGround = true;
    }

    @Override
    public void updateMovement(float deltaTime) {
        if (velocity.y > 0) {
            velocity.y = 0;
        }

        Vector3f pos = getPosition();
        pos.x += velocity.x * deltaTime;
        pos.y = GROUND_Y;
        pos.z += velocity.z * deltaTime;

        if (pos.x < BOUND_X_MIN) {
            pos.x = BOUND_X_MIN;
            velocity.x = -velocity.x;
        } else if (pos.x > BOUND_X_MAX) {
            pos.x = BOUND_X_MAX;
            velocity.x = -velocity.x;
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
