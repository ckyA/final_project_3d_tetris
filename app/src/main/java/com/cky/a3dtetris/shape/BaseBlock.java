package com.cky.a3dtetris.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.cky.a3dtetris.Utils;

public class BaseBlock {

    private final static String TAG = "BaseBlock";

    private int height = 9; // 0 to 9, the game space is 3 x 3 x 10, the blocks locate at 10 firstly.
    protected float r = 0;
    protected float g = 0;
    protected float b = 0;

    public static final float BLOCK_LENGTH = 0.1f;

    private BlockType type;

    // used to record which cube should be draw at this 3 x 3 x3 space. true: need draw.
    boolean[][][] validSpace = new boolean[3][3][3];

    private int normalMatrix;
    private int modelViewMatrix;
    private int uMatrixLocation;
    private float[] projectionMatrix;
    private int blockCount;

    BaseBlock(BlockType type, int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        this.normalMatrix = normalMatrix;
        this.modelViewMatrix = modelViewMatrix;
        this.uMatrixLocation = uMatrixLocation;
        this.projectionMatrix = projectionMatrix;
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public BlockType getType() {
        return type;
    }

    public enum Direction {
        X, Y, Z
    }

    /**
     * Move the block in 4 directions.
     * <p>
     * Because of the transformation, the z axis become the y axis ... it`s troublesome to fix it,
     * so let it go.
     *
     * @param direction : Direction.X or Direction.Y
     * @param positive  : front or back
     */
    public void move(Direction direction, boolean positive) {
        if (direction == Direction.X) {
            if (positive) {
                // whether block can be moved
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (validSpace[0][i][j]) {
                            Log.i(TAG, "Direction.X true: can`t move");
                            return;
                        }
                    }
                }
                // Then move
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[0][i][j] = validSpace[1][i][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[1][i][j] = validSpace[2][i][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[2][i][j] = false;
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (validSpace[2][i][j]) {
                            Log.i(TAG, "Direction.X false: can`t move");
                            return;
                        }
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[2][i][j] = validSpace[1][i][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[1][i][j] = validSpace[0][i][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[0][i][j] = false;
                    }
                }
            }
        } else if (direction == Direction.Y) {
            if (positive) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (validSpace[i][j][2]) {
                            Log.i(TAG, "Direction.Y true: can`t move");
                            return;
                        }
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][j][2] = validSpace[i][j][1];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][j][1] = validSpace[i][j][0];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][j][0] = false;
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (validSpace[i][j][0]) {
                            Log.i(TAG, "Direction.Y true: can`t move");
                            return;
                        }
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][j][0] = validSpace[i][j][1];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][j][1] = validSpace[i][j][2];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][j][2] = false;
                    }
                }
            }
        } else if (direction == Direction.Z) {
            if (positive) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (validSpace[i][2][j]) {
                            return;
                        }
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][2][j] = validSpace[i][1][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][1][j] = validSpace[i][0][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][0][j] = false;
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (validSpace[i][0][j]) {
                            return;
                        }
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][0][j] = validSpace[i][1][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][1][j] = validSpace[i][2][j];
                    }
                }
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        validSpace[i][2][j] = false;
                    }
                }
            }
        }
    }

    /**
     * rotate around x axis.
     *
     * @param isUpDirection rotate direction
     */
    public void rotateX(boolean isUpDirection) {
        boolean[][][] res = new boolean[3][3][3];
        boolean[][][] temporary = new boolean[3][3][3];
        // clone
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    temporary[i][j][k] = validSpace[i][j][k];
                }
            }
        }

        float[] oldPosition = new float[4];
        float[] newPosition = new float[4];

        if (isUpDirection) { // clockwise
            float[] MM = new float[16];
            Matrix.setIdentityM(MM, 0);
            Matrix.rotateM(MM, 0, 90f, 1, 0, 0);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        oldPosition[0] = i - 1;
                        oldPosition[1] = j - 1;
                        oldPosition[2] = k - 1;
                        oldPosition[3] = 1;
                        newPosition[0] = 0;
                        newPosition[1] = 0;
                        newPosition[2] = 0;
                        newPosition[3] = 0;
                        Matrix.multiplyMV(newPosition, 0, MM, 0, oldPosition, 0);
                        res[i][j][k] = temporary[Math.round(newPosition[0]) + 1][Math.round(newPosition[1]) + 1][Math.round(newPosition[2]) + 1];
                    }
                }
            }
        } else { // anti-clockwise
            float[] MM = new float[16];
            Matrix.setIdentityM(MM, 0);
            Matrix.rotateM(MM, 0, -90f, 1, 0, 0);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        oldPosition[0] = i - 1;
                        oldPosition[1] = j - 1;
                        oldPosition[2] = k - 1;
                        oldPosition[3] = 1;
                        newPosition[0] = 0;
                        newPosition[1] = 0;
                        newPosition[2] = 0;
                        newPosition[3] = 0;
                        Matrix.multiplyMV(newPosition, 0, MM, 0, oldPosition, 0);
                        res[i][j][k] = temporary[Math.round(newPosition[0]) + 1][Math.round(newPosition[1]) + 1][Math.round(newPosition[2]) + 1];
                    }
                }
            }
        }
        validSpace = res;
        move(Direction.Z, true);
        move(Direction.Z, true);
    }

    /**
     * rotate around y axis.
     *
     * @param isUpDirection rotate direction
     */
    public void rotateY(boolean isUpDirection) {
        boolean[][][] res = new boolean[3][3][3];
        boolean[][][] temporary = new boolean[3][3][3]; // clone
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    temporary[i][j][k] = validSpace[i][j][k];
                }
            }
        }

        float[] oldPosition = new float[4];
        float[] newPosition = new float[4];

        if (isUpDirection) { // clockwise
            float[] MM = new float[16];
            Matrix.setIdentityM(MM, 0);
            Matrix.rotateM(MM, 0, 90f, 0, 0, 1);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        oldPosition[0] = i - 1;
                        oldPosition[1] = j - 1;
                        oldPosition[2] = k - 1;
                        oldPosition[3] = 1;
                        newPosition[0] = 0;
                        newPosition[1] = 0;
                        newPosition[2] = 0;
                        newPosition[3] = 0;
                        Matrix.multiplyMV(newPosition, 0, MM, 0, oldPosition, 0);
                        res[i][j][k] = temporary[Math.round(newPosition[0]) + 1][Math.round(newPosition[1]) + 1][Math.round(newPosition[2]) + 1];
                    }
                }
            }
        } else { // anti-clockwise
            float[] MM = new float[16];
            Matrix.setIdentityM(MM, 0);
            Matrix.rotateM(MM, 0, -90f, 0, 0, 1);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        oldPosition[0] = i - 1;
                        oldPosition[1] = j - 1;
                        oldPosition[2] = k - 1;
                        oldPosition[3] = 1;
                        newPosition[0] = 0;
                        newPosition[1] = 0;
                        newPosition[2] = 0;
                        newPosition[3] = 0;
                        Matrix.multiplyMV(newPosition, 0, MM, 0, oldPosition, 0);
                        res[i][j][k] = temporary[Math.round(newPosition[0]) + 1][Math.round(newPosition[1]) + 1][Math.round(newPosition[2]) + 1];
                    }
                }
            }
        }
        validSpace = res;
        move(Direction.Z, true);
        move(Direction.Z, true);
    }

    public void fall() {
        height--;
    }

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
                        blockPosition = CubeUtil.combineArrays(blockPosition, CubeUtil.getCubePosition(BLOCK_LENGTH,
                                BLOCK_LENGTH * 2 * (i - 1),
                                BLOCK_LENGTH * 2 * (j - 1),
                                BLOCK_LENGTH * 2 * (k - 1)));
                        texturePosition = CubeUtil.combineArrays(texturePosition, CubeUtil.getCubeTexturePosition());
                        normalPosition = CubeUtil.combineArrays(normalPosition, CubeUtil.getCubeNormalPosition());
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
        // basic transformation :
        // Because of the transformation, the z axis become the y axis
        Matrix.rotateM(MM, 0, 20f, 1, 0, 0);
        Matrix.rotateM(MM, 0, 40f, 0, 1, 0);
        //height
        Matrix.translateM(MM, 0, 0, BLOCK_LENGTH * 2 * (height - 5), 0);

        Matrix.multiplyMM(MVPM, 0, projectionMatrix, 0, MM, 0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MVPM, 0);
        GLES20.glUniformMatrix4fv(modelViewMatrix, 1, false, MM, 0);
        GLES20.glUniformMatrix3fv(normalMatrix, 1, false, Utils.mat4ToMat3(MM), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36 * blockCount);
    }

    public boolean[][][] getValidSpace() {
        return validSpace;
    }
}
