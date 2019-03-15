package com.cky.a3dtetris.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.cky.a3dtetris.Utils;

public class Floor {

    private int normalMatrix;
    private int modelViewMatrix;
    private int uMatrixLocation;
    private float[] projectionMatrix;

    private float rotationAngle = 0;
    private float degreeNeedRotation = 0; // used to implement an animation

    public Floor(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        this.normalMatrix = normalMatrix;
        this.modelViewMatrix = modelViewMatrix;
        this.uMatrixLocation = uMatrixLocation;
        this.projectionMatrix = projectionMatrix;
    }

    public void refresh(int vPosition, int aTextureCoordinatesLocation, int vNormalPosition, int uColor) {

        // set normal line
        GLES20.glVertexAttribPointer(vNormalPosition, 3, GLES20.GL_FLOAT, false, 0,
                Utils.getFBVertices(normalPosition));
        GLES20.glEnableVertexAttribArray(vNormalPosition);

        //texture
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation, 2, GLES20.GL_FLOAT, false, 0,
                Utils.getFBVertices(texturePosition));
        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);

        // set shapes` location

        GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 0,
                Utils.getFBVertices(getFloorPosition(BaseBlock.BLOCK_LENGTH)));
        GLES20.glEnableVertexAttribArray(vPosition);

        GLES20.glUniform4f(uColor, 1f, 1f, 1f, 0f);
    }

    public void draw() {
        float[] MVPM = new float[16];
        float[] MM = new float[16];

        Matrix.setIdentityM(MM, 0);
        // basic rotate
        Matrix.rotateM(MM, 0, 24f, 1, 0, 0);
        Matrix.rotateM(MM, 0, 45f, 0, 1, 0);

        checkRotationAnimation();
        Matrix.rotateM(MM, 0, rotationAngle, 0, 1, 0);

        Matrix.translateM(MM, 0, 0, -1f, 0);

        Matrix.multiplyMM(MVPM, 0, projectionMatrix, 0, MM, 0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MVPM, 0);
        GLES20.glUniformMatrix4fv(modelViewMatrix, 1, false, MM, 0);
        GLES20.glUniformMatrix3fv(normalMatrix, 1, false, Utils.mat4ToMat3(MM), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    public void rotate(boolean positive) {
        if (degreeNeedRotation != 0) {
            return;
        }
        rotationAngle = rotationAngle % 360f;
        if (positive) {
            degreeNeedRotation += 90;
        } else {
            degreeNeedRotation -= 90;
        }
    }

    public void checkRotationAnimation() {
        if (degreeNeedRotation != 0) {
            if (degreeNeedRotation > 0) {
                rotationAngle += 5f;
                degreeNeedRotation -= 5f;
            } else {
                rotationAngle -= 5f;
                degreeNeedRotation += 5f;
            }
        }
    }

    /**
     * @param l : the length of a block.
     */
    private static float[] getFloorPosition(float l) {
        float length = 4.1f * l;
        float height = 0.2f * l;
        return new float[]{

                // Top face
                -length, height, length,
                -length, -height, length,
                length, height, length,
                -length, -height, length,
                length, -height, length,
                length, height, length,

                length, height, length,
                length, -height, length,
                length, height, -length,
                length, -height, length,
                length, -height, -length,
                length, height, -length,

                length, height, -length,
                length, -height, -length,
                -length, height, -length,
                length, -height, -length,
                -length, -height, -length,
                -length, height, -length,

                -length, height, -length,
                -length, -height, -length,
                -length, height, length,
                -length, -height, -length,
                -length, -height, length,
                -length, height, length,

                -length, height, -length,
                -length, height, length,
                length, height, -length,
                -length, height, length,
                length, height, length,
                length, height, -length,

                length, -height, -length,
                length, -height, length,
                -length, -height, -length,
                length, -height, length,
                -length, -height, length,
                -length, -height, -length
        };
    }

    // only the top has the texture
    private static float[] texturePosition = new float[]{
            // Front face
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,

            // Right face
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,

            // Back face
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,

            // Left face
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,

            // Top face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Bottom face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    private static float[] normalPosition = new float[]{
            // Front face
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,

            // Right face
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,

            // Back face
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,

            // Left face
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,

            // Top face
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,

            // Bottom face
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0
    };
}
