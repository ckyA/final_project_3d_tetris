package com.cky.a3dtetris;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Admin on 2019/2/14.
 */

public class Utils {

    public static float[] mat4ToMat3(float[] input) {
        if (input.length == 16) {
            float[] out = new float[9];
            out[0] = input[0];
            out[1] = input[1];
            out[2] = input[2];
            out[3] = input[4];
            out[4] = input[5];
            out[5] = input[6];
            out[6] = input[8];
            out[7] = input[9];
            out[8] = input[10];
            return out;
        }
        return null;
    }

    public static FloatBuffer getFBVertices(float[] vertices) {
        // 创建顶点坐标数据缓冲
        // vertices.length*4是因为一个float占四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());             //设置字节顺序
        FloatBuffer vertexBuf = vbb.asFloatBuffer();    //转换为Float型缓冲
        vertexBuf.put(vertices);                        //向缓冲区中放入顶点坐标数据
        vertexBuf.position(0);                          //设置缓冲区起始位置

        return vertexBuf;
    }
}
