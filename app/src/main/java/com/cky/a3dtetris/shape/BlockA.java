package com.cky.a3dtetris.shape;

public class BlockA extends BaseBlock {

    private static CenterPoint centerPoint = new CenterPoint(1,1,1);

    public BlockA(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix, centerPoint);
            r = 1;
            g = 0;
            b = 0;
            validSpace[1][1][0] = true;
            validSpace[1][1][1] = true;
            validSpace[1][1][2] = true;
        }

    @Override
    public void translate(int offsetX, int offsetY, int offsetZ) {

    }

    @Override
    public void rotateX(boolean isUpDirection) {
        rotateAroundX(isUpDirection);
    }

    @Override
    public void rotateY(boolean isUpDirection) {
        rotateAroundY(isUpDirection);
    }
}
