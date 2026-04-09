package com.gameengine.vehicles;

import com.gameengine.GameItem;
import com.gameengine.Mesh;
import com.gameengine.collision.CollisionLog;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Vehicle extends GameItem {

    protected static final Mesh CAR_MESH = Mesh.createBox(1.5f, 0.75f, 3.0f);
    protected static final Mesh PLANE_MESH = Mesh.createBox(3.0f, 0.5f, 2.0f);

    protected final Vector3f velocity;
    protected float speed;
    protected float acceleration;
    protected float gravity = 9.8f;
    protected boolean onGround;

    private static final float MAX_SPEED = 20.0f;

    public Vehicle(Mesh mesh, Vector4f color) {
        super(mesh, color);
        this.velocity = new Vector3f(0, 0, 0);
        this.speed = 0f;
        this.acceleration = 5.0f;
        this.onGround = false;
    }

    public abstract void updateMovement(float deltaTime);

    public void update(float deltaTime) {
        updateMovement(deltaTime);
    }

    public boolean collide(Vehicle other) {
        float[] a = this.getBounds();
        float[] b = other.getBounds();
        boolean overlap =
                a[0] < b[1] && a[1] > b[0] &&
                a[2] < b[3] && a[3] > b[2] &&
                a[4] < b[5] && a[5] > b[4];
        if (overlap) {
            onCollision(other);
            other.onCollision(this);
            CollisionLog.log(this, other);
            return true;
        }
        return false;
    }

    public void onCollision(Vehicle other) {
        float[] a = this.getBounds();
        float[] b = other.getBounds();

        float overlapX = Math.min(a[1], b[1]) - Math.max(a[0], b[0]);
        float overlapY = Math.min(a[3], b[3]) - Math.max(a[2], b[2]);
        float overlapZ = Math.min(a[5], b[5]) - Math.max(a[4], b[4]);

        if (overlapX <= overlapY && overlapX <= overlapZ) {
            velocity.x = -velocity.x;
        } else if (overlapY <= overlapX && overlapY <= overlapZ) {
            velocity.y = -velocity.y;
        } else {
            velocity.z = -velocity.z;
        }
    }

    public void accelerate(Vector3f direction) {
        Vector3f norm = new Vector3f(direction);
        if (norm.lengthSquared() > 0) {
            norm.normalize();
        }
        velocity.add(norm.mul(acceleration));
        if (velocity.length() > MAX_SPEED) {
            velocity.normalize(MAX_SPEED, velocity);
        }
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(float x, float y, float z) {
        velocity.set(x, y, z);
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
