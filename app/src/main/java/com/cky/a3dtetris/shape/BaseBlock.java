package com.cky.a3dtetris.shape;

public abstract class BaseBlock {

    private int height = 10; // the game space is 3 x 3 x 10, the blocks locate at 10 firstly.
    private int color = 0xffff0000;
    private boolean[][][] x = new boolean[3][3][3]; // used to record which cube should be draw at this 3 x 3 x3 space.

    public abstract void translate(int offsetX, int offsetY);

    /**
     * rotate around x axis.
     * @param isUpDirection rotate direction
     */
    public abstract void rotateX(boolean isUpDirection);

    /**
     * rotate around y axis.
     * @param isUpDirection rotate direction
     */
    public abstract void rotateY(boolean isUpDirection);

    public abstract void draw();
}
