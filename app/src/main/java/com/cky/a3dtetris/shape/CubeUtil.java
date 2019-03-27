package com.cky.a3dtetris.shape;

import android.util.Log;

import java.util.Arrays;

/**
 * Used to get coordinate of each shapes using CubeUtil.
 */

public class CubeUtil {

    public static float[] combineArrays(float[]... positions) {
        int length = 0;
        for (float[] position : positions) {
            length += position.length;
        }
        if (length == 0) {
            return null;
        }
        float[] res = new float[length];
        int index = 0;
        for (float[] position : positions) {
            if (position != null && position.length > 0) {
                for (float value : position) {
                    if (index < res.length) {
                        res[index] = value;
                        index++;
                    }
                }
            }
        }
        return res;
    }

    public static float[] getCubePosition(float l, float x, float y, float z) {
        return new float[] {

                // Front face
                -l + x, l + y, l + z,
                -l + x, -l + y, l + z,
                l + x, l + y, l + z,
                -l + x, -l + y, l + z,
                l + x, -l + y, l + z,
                l + x, l + y, l + z,

                // Right face
                l + x, l + y, l + z,
                l + x, -l + y, l + z,
                l + x, l + y, -l + z,
                l + x, -l + y, l + z,
                l + x, -l + y, -l + z,
                l + x, l + y, -l + z,

                // Back face
                l + x, l + y, -l + z,
                l + x, -l + y, -l + z,
                -l + x, l + y, -l + z,
                l + x, -l + y, -l + z,
                -l + x, -l + y, -l + z,
                -l + x, l + y, -l + z,

                // Left face
                -l + x, l + y, -l + z,
                -l + x, -l + y, -l + z,
                -l + x, l + y, l + z,
                -l + x, -l + y, -l + z,
                -l + x, -l + y, l + z,
                -l + x, l + y, l + z,

                // Top face
                -l + x, l + y, -l + z,
                -l + x, l + y, l + z,
                l + x, l + y, -l + z,
                -l + x, l + y, l + z,
                l + x, l + y, l + z,
                l + x, l + y, -l + z,

                // Bottom face
                l + x, -l + y, -l + z,
                l + x, -l + y, l + z,
                -l + x, -l + y, -l + z,
                l + x, -l + y, l + z,
                -l + x, -l + y, l + z,
                -l + x, -l + y, -l + z
        };
    }

    public static float[] getCubeTexturePosition() {
        return texturePosition;
    }

    public static float[] getCubeNormalPosition() {
        return normalPosition;
    }

    private static float[] texturePosition = new float[]{
            // Front face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Right face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Back face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            // Left face
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

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
            1.0f, 0.0f
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
