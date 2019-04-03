package com.cky.a3dtetris.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.cky.a3dtetris.Utils;

import static com.cky.a3dtetris.shape.BaseBlock.BLOCK_LENGTH;

public class Shadow {

    private final int normalMatrix;
    private final int modelViewMatrix;
    private final int uMatrixLocation;
    private final float[] projectionMatrix;

    public Shadow(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        this.normalMatrix = normalMatrix;
        this.modelViewMatrix = modelViewMatrix;
        this.uMatrixLocation = uMatrixLocation;
        this.projectionMatrix = projectionMatrix;
    }

    public void showShadow(boolean[][][] validSpace, BlockType[][][] blockList, int vPosition, int aTextureCoordinatesLocation, int vNormalPosition, int uColor, float rotationAngle) {
        float[] shadowPosition = new float[0];
        float[] texturePosition = new float[0];
        float[] normalPosition = new float[0];

        int shadowCount = 0;
        int[][] shadowList = new int[3][3]; // x y

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                shadowList[i][j] = -1;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) { // z
                    if (validSpace[i][k][j]) {
                        shadowList[i][j] = getTopBlockHeight(blockList, i, j);
                        shadowCount++;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (shadowList[i][j] != -1) {
                    shadowPosition = CubeUtil.combineArrays(shadowPosition, getShadowPosition(i, shadowList[i][j], j));
                    texturePosition = CubeUtil.combineArrays(texturePosition, Shadow.texturePosition);
                    normalPosition = CubeUtil.combineArrays(normalPosition, Shadow.normalPosition);
                }
            }
        }
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
                Utils.getFBVertices(shadowPosition));
        GLES20.glEnableVertexAttribArray(vPosition);
        // set color
        GLES20.glUniform4f(uColor, 0.1f, 1f, 0.1f, 1.0f);

        //draw

        float[] MVPM = new float[16];
        float[] MM = new float[16];

        Matrix.setIdentityM(MM, 0);
        // basic transformation :
        // Because of the transformation, the z axis become the y axis
        Matrix.rotateM(MM, 0, 20f, 1, 0, 0);
        Matrix.rotateM(MM, 0, 40f, 0, 1, 0);

        //checkRotationAnimation();
        //Matrix.rotateM(MM, 0, rotationAngle, 0, 1, 0);

        Matrix.translateM(MM, 0, 0, BLOCK_LENGTH * 2 * -4.4f, 0);

        Matrix.multiplyMM(MVPM, 0, projectionMatrix, 0, MM, 0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MVPM, 0);
        GLES20.glUniformMatrix4fv(modelViewMatrix, 1, false, MM, 0);
        GLES20.glUniformMatrix3fv(normalMatrix, 1, false, Utils.mat4ToMat3(MM), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36 * shadowCount);
    }

    public int getTopBlockHeight(BlockType[][][] blockList, int x, int y) {
        int res = 0;
        for (int i = 0; i < BaseBlock.MAX_HEIGHT; i++) {
            if (blockList[x][i][y] != null) {
                res = i + 1;
            }
        }

        return res;
    }

    private static float[] getShadowPosition(float sx, float sy, float sz) {
        float length = BLOCK_LENGTH;
        float height = 0.2f * length;
        float x = (sx - 1) * BLOCK_LENGTH * 2;
        float y = sy * BLOCK_LENGTH * 2;
        float z = (sz - 1) * BLOCK_LENGTH * 2;
        return new float[]{

                // Top face
                -length + x, height + y, length + z,
                -length + x, -height + y, length + z,
                length + x, height + y, length + z,
                -length + x, -height + y, length + z,
                length + x, -height + y, length + z,
                length + x, height + y, length + z,

                length + x, height + y, length + z,
                length + x, -height + y, length + z,
                length + x, height + y, -length + z,
                length + x, -height + y, length + z,
                length + x, -height + y, -length + z,
                length + x, height + y, -length + z,

                length + x, height + y, -length + z,
                length + x, -height + y, -length + z,
                -length + x, height + y, -length + z,
                length + x, -height + y, -length + z,
                -length + x, -height + y, -length + z,
                -length + x, height + y, -length + z,

                -length + x, height + y, -length + z,
                -length + x, -height + y, -length + z,
                -length + x, height + y, length + z,
                -length + x, -height + y, -length + z,
                -length + x, -height + y, length + z,
                -length + x, height + y, length + z,

                -length + x, height + y, -length + z,
                -length + x, height + y, length + z,
                length + x, height + y, -length + z,
                -length + x, height + y, length + z,
                length + x, height + y, length + z,
                length + x, height + y, -length + z,

                length + x, -height + y, -length + z,
                length + x, -height + y, length + z,
                -length + x, -height + y, -length + z,
                length + x, -height + y, length + z,
                -length + x, -height + y, length + z,
                -length + x, -height + y, -length + z
        };
    }

    private static float[] texturePosition = new float[]{
            // Front face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Right face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Back face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Left face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

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
            1.0f, 0.0f
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
