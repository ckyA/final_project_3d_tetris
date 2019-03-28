package com.cky.a3dtetris;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.cky.a3dtetris.shape.BaseBlock;
import com.cky.a3dtetris.shape.BlockType;
import com.cky.a3dtetris.shape.Floor;

import java.util.Random;

public class GameManager {

    private Handler handler;
    private volatile Floor floor;
    private volatile BaseBlock fallingBlock;
    private int speed = 1000; // falling speed (Unit: mm)
    private boolean isPause = false;

    private volatile OnBlockChangeListener onBlockChangeListener;

    public interface OnBlockChangeListener {
        /**
         * when the falling block fix on the floor.
         */
        void onBlockChange();
    }

    private GameManager(Handler handler) {
        this.handler = handler;

    }

    public static GameManager create(@NonNull Handler handler) {
        return new GameManager(handler);
    }

    public void start() {
        if (floor == null || onBlockChangeListener == null || fallingBlock == null) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    start();
//                }
//            }, 500);
            return;
        }
        isPause = false;
        fall();
    }

    private Runnable fallRunnable = new Runnable() {
        @Override
        public void run() {
            if (canBlockFall()) {
                fallingBlock.fall();
            } else {
                floor.fixBlock(fallingBlock);
                if (onBlockChangeListener != null) {
                    onBlockChangeListener.onBlockChange();
                }
            }
            fall();
        }
    };

    private void fall() {
        if (isPause) {
            return;
        }
        handler.postDelayed(fallRunnable, speed);
    }

    /**
     * stop falling
     */
    public void pause() {
        isPause = true;
    }

    public boolean isPause() {
        return isPause;
    }

    /**
     * release the resource
     */
    public void destroy() {
        handler.removeCallbacksAndMessages(null);
        onBlockChangeListener = null;
    }

    public BlockType createRandomBlockType() {
        Random random = new Random();
        BlockType blockType;
        switch (random.nextInt(6)) {// 0 ~ 5
            case 0:
                blockType = BlockType.A;
                break;
            case 1:
                blockType = BlockType.B;
                break;
            case 2:
                blockType = BlockType.C;
                break;
            case 3:
                blockType = BlockType.D;
                break;
            case 4:
                blockType = BlockType.E;
                break;
            case 5:
                blockType = BlockType.F;
                break;
            default:
                blockType = BlockType.A;
                break;
        }
        return blockType;
    }

    public boolean canBlockFall() {
        BlockType[][][] blockList = floor.getBlockList();
        boolean[][][] validSpace = fallingBlock.getValidSpace();
        int height = fallingBlock.getHeight();

        if (isPlaneEmpty(validSpace, 0)) {
            if (isPlaneEmpty(validSpace, 1)) {
                return detectFallCollision(2, height, validSpace, blockList);
            } else {
                return detectFallCollision(1, height, validSpace, blockList);
            }
        } else {
            return detectFallCollision(0, height, validSpace, blockList);
        }
    }

    /**
     * 3 X 3 X 3 space, XY plane
     */
    private boolean isPlaneEmpty(boolean[][][] validSpace, int which) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (validSpace[i][which][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean detectFallCollision(int floors, int height, boolean[][][] validSpace, BlockType[][][] blockList) {
        if (height + floors - 2 <= 0) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (validSpace[i][floors][j] && blockList[i][height - 3 + floors][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void setFallingBlock(BaseBlock fallingBlock) {
        this.fallingBlock = fallingBlock;
    }

    public void setOnBlockChangeListener(OnBlockChangeListener onBlockChangeListener) {
        this.onBlockChangeListener = onBlockChangeListener;
    }
}
