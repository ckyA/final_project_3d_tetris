package com.cky.a3dtetris.shape;

public class BlockA extends BaseBlock {

    public BlockA(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
        r = 1;
        g = 0;
        b = 0;
        validSpace[1][1][0] = true;
        validSpace[1][1][1] = true;
        validSpace[1][1][2] = true;
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
