package com.cky.a3dtetris.shape;

/**
 * Created by Admin on 2019/2/13.
 */

public interface BaseShape {
    /**
     * draw a shape in the openGL stage using the offset value.
     */
    void draw(int offsetX, int offsetY, int offsetZ);
}
