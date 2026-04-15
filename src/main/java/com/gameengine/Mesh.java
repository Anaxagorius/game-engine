package com.gameengine;

import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vaoId;
    private final int[] vboIds;
    private final int vertexCount;
    private final float width;
    private final float height;
    private final float depth;

    private Mesh(int vaoId, int[] vboIds, int vertexCount, float width, float height, float depth) {
        this.vaoId = vaoId;
        this.vboIds = vboIds;
        this.vertexCount = vertexCount;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public static Mesh createBox(float width, float height, float depth) {
        float hw = width / 2f;
        float hh = height / 2f;
        float hd = depth / 2f;

        // 6 faces × 4 vertices × 6 floats (xyz + nxnynz) = 144 floats
        // Each face has its own duplicated vertices so flat normals are correct.
        float[] vertexData = {
            // Front (+Z)
            -hw, -hh,  hd,  0, 0, 1,
             hw, -hh,  hd,  0, 0, 1,
             hw,  hh,  hd,  0, 0, 1,
            -hw,  hh,  hd,  0, 0, 1,
            // Back (-Z)
             hw, -hh, -hd,  0, 0,-1,
            -hw, -hh, -hd,  0, 0,-1,
            -hw,  hh, -hd,  0, 0,-1,
             hw,  hh, -hd,  0, 0,-1,
            // Left (-X)
            -hw, -hh, -hd, -1, 0, 0,
            -hw, -hh,  hd, -1, 0, 0,
            -hw,  hh,  hd, -1, 0, 0,
            -hw,  hh, -hd, -1, 0, 0,
            // Right (+X)
             hw, -hh,  hd,  1, 0, 0,
             hw, -hh, -hd,  1, 0, 0,
             hw,  hh, -hd,  1, 0, 0,
             hw,  hh,  hd,  1, 0, 0,
            // Top (+Y)
            -hw,  hh,  hd,  0, 1, 0,
             hw,  hh,  hd,  0, 1, 0,
             hw,  hh, -hd,  0, 1, 0,
            -hw,  hh, -hd,  0, 1, 0,
            // Bottom (-Y)
            -hw, -hh, -hd,  0,-1, 0,
             hw, -hh, -hd,  0,-1, 0,
             hw, -hh,  hd,  0,-1, 0,
            -hw, -hh,  hd,  0,-1, 0,
        };

        int[] indices = {
            // Front
             0,  1,  2,  2,  3,  0,
            // Back
             4,  5,  6,  6,  7,  4,
            // Left
             8,  9, 10, 10, 11,  8,
            // Right
            12, 13, 14, 14, 15, 12,
            // Top
            16, 17, 18, 18, 19, 16,
            // Bottom
            20, 21, 22, 22, 23, 20,
        };

        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        int vboId;
        int eboId;
        int stride = 6 * Float.BYTES;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer vertexBuffer = stack.mallocFloat(vertexData.length);
            vertexBuffer.put(vertexData).flip();

            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            // Position attribute (location 0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
            glEnableVertexAttribArray(0);

            // Normal attribute (location 1)
            glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, (long) 3 * Float.BYTES);
            glEnableVertexAttribArray(1);

            IntBuffer indexBuffer = stack.mallocInt(indices.length);
            indexBuffer.put(indices).flip();

            eboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        }

        glBindVertexArray(0);

        return new Mesh(vaoId, new int[]{vboId, eboId}, indices.length, width, height, depth);
    }

    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIds) {
            glDeleteBuffers(vboId);
        }
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }
}
