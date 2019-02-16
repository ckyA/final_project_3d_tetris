package com.cky.a3dtetris.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.cky.a3dtetris.R;
import com.cky.a3dtetris.Utils;

public abstract class BaseBlock {

    public int height = 10; // the game space is 3 x 3 x 10, the blocks locate at 10 firstly.
    protected float r = 0;
    protected float g = 0;
    protected float b = 0;

    private static final float BLOCK_LENGTH = 0.1f;

    // used to record which cube should be draw at this 3 x 3 x3 space. true: need draw.
    protected boolean[][][] validSpace = new boolean[3][3][3];

    private int normalMatrix;
    private int modelViewMatrix;
    private int uMatrixLocation;
    private float[] projectionMatrix;
    private int blockCount;

    public BaseBlock(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        this.normalMatrix = normalMatrix;
        this.modelViewMatrix = modelViewMatrix;
        this.uMatrixLocation = uMatrixLocation;
        this.projectionMatrix = projectionMatrix;
    }

    public abstract void translate(int offsetX, int offsetY);

    /**
     * rotate around x axis.
     *
     * @param isUpDirection rotate direction
     */
    public abstract void rotateX(boolean isUpDirection);

    /**
     * rotate around y axis.
     *
     * @param isUpDirection rotate direction
     */
    public abstract void rotateY(boolean isUpDirection);


    public void refresh(int vPosition, int aTextureCoordinatesLocation, int vNormalPosition, int uColor) {

        float[] blockPosition = new float[0];
        float[] texturePosition = new float[0];
        float[] normalPosition = new float[0];
        blockCount = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (validSpace[i][j][k]) {
                        blockCount++;
                        blockPosition = CubeTool.combineArrays(blockPosition, CubeTool.getCubePosition(BLOCK_LENGTH,
                                BLOCK_LENGTH * 2 * (i - 1), BLOCK_LENGTH * 2 * (j - 1), BLOCK_LENGTH * 2 * (k - 1)));
                        texturePosition = CubeTool.combineArrays(texturePosition, CubeTool.getTexturePosition());
                        normalPosition = CubeTool.combineArrays(normalPosition, CubeTool.getNormalPosition());
                    }
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
                Utils.getFBVertices(blockPosition));
        GLES20.glEnableVertexAttribArray(vPosition);

        GLES20.glUniform4f(uColor, r, g, b, 1.0f);
    }

    public void draw() {
        float[] MVPM = new float[16];
        float[] MM = new float[16];

        Matrix.setIdentityM(MM, 0);
        Matrix.translateM(MM, 0, 0, (float) (0.1 * height), 0);
        Matrix.rotateM(MM, 0, 10f, 0, 0, 1);
        Matrix.rotateM(MM, 0, 20f, 0, 1, 0);
        Matrix.rotateM(MM, 0, 30f, 1, 0, 0);

        Matrix.multiplyMM(MVPM, 0, projectionMatrix, 0, MM, 0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MVPM, 0);
        GLES20.glUniformMatrix4fv(modelViewMatrix, 1, false, MM, 0);
        GLES20.glUniformMatrix3fv(normalMatrix, 1, false, Utils.mat4ToMat3(MM), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36 * blockCount);
    }
}