package com.cky.a3dtetris.shape;

public class BlockFactory {

    public static BaseBlock createBlock(BlockType type, int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
        switch (type) {
            case A:
                return new BlockA(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            case B:
                return new BlockB(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            case C:
                return new BlockC(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            case D:
                return new BlockD(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            case E:
                return new BlockE(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            case F:
                return new BlockF(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            default:
                return null;
        }
    }

    static class BlockA extends BaseBlock {

        BlockA(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
            super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            r = 1;
            g = 0;
            b = 0;
            validSpace[1][0][1] = true;
            validSpace[1][1][1] = true;
            validSpace[1][2][1] = true;
        }
    }

    static class BlockB extends BaseBlock {

        BlockB(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
            super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            r = (4f * 16f + 4f) / 255f;
            g = (14f * 16f + 15f) / 255f;
            b = (14f * 16f + 9f) / 255f;
            validSpace[0][1][0] = true;
            validSpace[0][1][1] = true;
            validSpace[0][2][0] = true;
            validSpace[1][1][0] = true;
        }

    }

    static class BlockC extends BaseBlock {

        BlockC(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
            super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            r = 0;
            g = (255f) / 255f;
            b = 0;
            validSpace[1][2][0] = true;
            validSpace[1][2][1] = true;
            validSpace[0][2][1] = true;
            validSpace[0][2][2] = true;
        }

    }

    static class BlockD extends BaseBlock {

        BlockD(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
            super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            r = (255f) / 255f;
            g = (255f) / 255f;
            b = 0;
            validSpace[0][2][0] = true;
            validSpace[0][2][1] = true;
            validSpace[0][2][2] = true;
            validSpace[1][2][2] = true;
        }

    }

    static class BlockE extends BaseBlock {

        BlockE(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
            super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            r = (255F) / 255f;
            g = (68F) / 255f;
            b = (96F) / 255f;
            validSpace[0][1][1] = true;
            validSpace[1][1][1] = true;
            validSpace[1][1][0] = true;
            validSpace[1][2][0] = true;
        }

    }

    static class BlockF extends BaseBlock {

        BlockF(int normalMatrix, int modelViewMatrix, int uMatrixLocation, float[] projectionMatrix) {
            super(normalMatrix, modelViewMatrix, uMatrixLocation, projectionMatrix);
            r = 1;//44EFB9
            g = 185f / 255f;
            b = 73f / 255f;
            validSpace[0][1][1] = true;
            validSpace[0][1][0] = true;
            validSpace[1][1][0] = true;
            validSpace[1][2][0] = true;
        }

    }
}
