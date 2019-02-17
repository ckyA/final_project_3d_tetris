package com.cky.a3dtetris.shape;

/**
 * Created by Admin on 2019/2/16.
 */

public class BlockB extends BaseBlock {

    public BlockB(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
        r = (4f * 16f + 4f) / 256f;//44EFB9
        g = (14f * 16f + 15f) / 256f;
        b = (14f * 16f + 9f) / 256f;
        validSpace[0][0][0] = true;
        validSpace[0][0][1] = true;
        validSpace[0][1][0] = true;
        validSpace[1][0][0] = true;
    }

    @Override
    public void translate(int offsetX, int offsetY) {

    }

    @Override
    public void rotateX(boolean isUpDirection) {

    }

    @Override
    public void rotateY(boolean isUpDirection) {

    }
}
