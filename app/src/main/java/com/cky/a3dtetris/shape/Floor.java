package com.cky.a3dtetris.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.cky.a3dtetris.GameManager;
import com.cky.a3dtetris.Utils;

import static com.cky.a3dtetris.shape.BaseBlock.BLOCK_LENGTH;

public class Floor {

    private int normalMatrix;
    private int modelViewMatrix;
    private int uMatrixLocation;
    private float[] projectionMatrix;

    private BlockType[][][] blockList = new BlockType[3][BaseBlock.MAX_HEIGHT][3]; // If null, don`t draw.

    private float rotationAngle = 0;
    private float degreeNeedRotation = 0; // used to implement an animation
    private float blockRotationAngle;


    public Floor(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        this.normalMatrix = normalMatrix;
        this.modelViewMatrix = modelViewMatrix;
        this.uMatrixLocation = uMatrixLocation;
        this.projectionMatrix = projectionMatrix;
    }

    /**
     * Draw the floor. Call this method after calling Floor.drawFixedBlocks()
     */
    public void draw(int vPosition, int aTextureCoordinatesLocation, int vNormalPosition, int uColor, int texture, int uTextureUnitLocation) {
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
                Utils.getFBVertices(getFloorPosition(BLOCK_LENGTH)));
        GLES20.glEnableVertexAttribArray(vPosition);

        GLES20.glUniform4f(uColor, 1f, 1f, 1f, 0f);

        // set current texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(uTextureUnitLocation, 1);

        //draw

        float[] MVPM = new float[16];
        float[] MM = new float[16];

        Matrix.setIdentityM(MM, 0);
        // basic transformation :
        // Because of the transformation, the z axis become the y axis
        Matrix.rotateM(MM, 0, 20f, 1, 0, 0);
        Matrix.rotateM(MM, 0, 40f, 0, 1, 0);

        //checkRotationAnimation();
        Matrix.rotateM(MM, 0, rotationAngle, 0, 1, 0);

        Matrix.translateM(MM, 0, 0, BLOCK_LENGTH * 2 * -4.6f, 0);

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
        blockRotationAngle = 0;
        if (positive) {
            degreeNeedRotation += 90;
        } else {
            degreeNeedRotation -= 90;
        }
    }

    private BlockType[][][] temporaryList = null;

    public void checkRotationAnimation() {
        if (degreeNeedRotation != 0) {

            if (temporaryList == null) {
                temporaryList = rotateBlockListAroundZ(degreeNeedRotation > 0);
                if (GameManager.getGameManager() != null && !GameManager.getGameManager().detectCollision(GameManager.getGameManager().getFallingBlock().getHeight(),
                        GameManager.getGameManager().getFallingBlock().getValidSpace(), temporaryList)) {
                    // cancel rotation
                    degreeNeedRotation = 0;
                    temporaryList = null;
                    blockRotationAngle = 0;
                }
            }

            if (degreeNeedRotation > 0) {
                rotationAngle += 10f;
                degreeNeedRotation -= 10f;
                blockRotationAngle += 10f;
                if (degreeNeedRotation == 0) {
                    //rotateBlockListAroundZ(true);
                    blockRotationAngle = 0;
                    blockList = temporaryList;
                    temporaryList = null;
                }
            } else {
                rotationAngle -= 10f;
                degreeNeedRotation += 10f;
                blockRotationAngle -= 10f;
                if (degreeNeedRotation == 0) {
                    //rotateBlockListAroundZ(false);
                    blockRotationAngle = 0;
                    blockList = temporaryList;
                    temporaryList = null;
                }
            }
        }
    }

    public BlockType[][][] rotateBlockListAroundZ(boolean positive) {
        BlockType[][][] res = new BlockType[3][BaseBlock.MAX_HEIGHT][3];
        BlockType[][][] temporary = new BlockType[3][BaseBlock.MAX_HEIGHT][3];
        // clone
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < BaseBlock.MAX_HEIGHT; j++) {
                for (int k = 0; k < 3; k++) {
                    temporary[i][j][k] = blockList[i][j][k];
                }
            }
        }

        float[] MM = new float[16];
        Matrix.setIdentityM(MM, 0);
        Matrix.rotateM(MM, 0, positive ? -90 : 90, 0, 1, 0);

        float[] oldPosition = new float[4];
        float[] newPosition = new float[4];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < BaseBlock.MAX_HEIGHT; j++) {
                for (int k = 0; k < 3; k++) {
                    oldPosition[0] = i - 1;
                    oldPosition[1] = j;
                    oldPosition[2] = k - 1;
                    oldPosition[3] = 1;
                    newPosition[0] = 0;
                    newPosition[1] = 0;
                    newPosition[2] = 0;
                    newPosition[3] = 0;
                    Matrix.multiplyMV(newPosition, 0, MM, 0, oldPosition, 0);
                    res[i][j][k] = temporary[Math.round(newPosition[0]) + 1][Math.round(newPosition[1])][Math.round(newPosition[2]) + 1];
                }
            }
        }
//        blockRotationAngle = 0;
//        blockList = res;

        return res;
    }

    public BlockType[][][] getBlockList() {
        return blockList;
    }

    public void fixBlock(BaseBlock block) {
        int blockHeight = block.getHeight();
        if (blockHeight <= 9 && blockHeight >= 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        if (blockHeight >= 0) {
                            if (block.validSpace[i][j][k] && blockList[i][blockHeight - 2 + j][k] == null) {
                                blockList[i][blockHeight - 2 + j][k] = block.getType();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * set space coordinates and color for one type of blocks.
     * return the number of the blocks.
     */
    public void drawBlocksByType(BlockType type, int vPosition, int aTextureCoordinatesLocation, int vNormalPosition, int uColor) {
        float[] blockPosition = new float[0];
        float[] texturePosition = new float[0];
        float[] normalPosition = new float[0];
        int blockCount = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < BaseBlock.MAX_HEIGHT; j++) {
                for (int k = 0; k < 3; k++) {
                    if (blockList[i][j][k] == type) {
                        blockCount++;
                        blockPosition = CubeUtil.combineArrays(blockPosition, CubeUtil.getCubePosition(BLOCK_LENGTH,
                                BLOCK_LENGTH * 2 * (i - 1),
                                BLOCK_LENGTH * 2 * (j - 4),
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

        BlockFactory.setBlockColor(type, uColor);

        drawBlock(blockCount);
    }

    /**
     * Draw blocks fixed on this floor. This method must be called before calling Floor.draw()
     */
    public void drawFixedBlocks(int vPosition, int aTextureCoordinatesLocation, int vNormalPosition, int uColor) {
        checkRotationAnimation();
        drawBlocksByType(BlockType.A, vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
        drawBlocksByType(BlockType.B, vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
        drawBlocksByType(BlockType.C, vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
        drawBlocksByType(BlockType.D, vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
        drawBlocksByType(BlockType.E, vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
        drawBlocksByType(BlockType.F, vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
    }

    private void drawBlock(int blockCount) {
        float[] MVPM = new float[16];
        float[] MM = new float[16];

        Matrix.setIdentityM(MM, 0);
        // basic transformation :
        // Because of the transformation, the z axis become the y axis
        Matrix.rotateM(MM, 0, 20f, 1, 0, 0);
        Matrix.rotateM(MM, 0, 40f, 0, 1, 0);

        Matrix.rotateM(MM, 0, degreeNeedRotation == 0 ? 0 : blockRotationAngle, 0, 1, 0);

        Matrix.multiplyMM(MVPM, 0, projectionMatrix, 0, MM, 0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MVPM, 0);
        GLES20.glUniformMatrix4fv(modelViewMatrix, 1, false, MM, 0);
        GLES20.glUniformMatrix3fv(normalMatrix, 1, false, Utils.mat4ToMat3(MM), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36 * blockCount);
    }

    public float getRotationAngle() {
        return rotationAngle;
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
