package com.cky.a3dtetris.shape;

/**
 * Created by Admin on 2019/2/16.
 */

public class BlockB extends BaseBlock {

    public BlockB(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
        r = 0.3f;
        g = 0.4f;
        b = 0.1f;
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
