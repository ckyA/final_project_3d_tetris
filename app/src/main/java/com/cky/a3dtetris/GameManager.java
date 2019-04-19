package com.cky.a3dtetris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cky.a3dtetris.rank.RankItem;
import com.cky.a3dtetris.rank.RankManager;
import com.cky.a3dtetris.shape.BaseBlock;
import com.cky.a3dtetris.shape.BlockType;
import com.cky.a3dtetris.shape.Floor;

import java.util.Random;

public class GameManager {

    private final TetrisActivity activity;
    private Handler handler;
    private volatile Floor floor;
    private volatile BaseBlock fallingBlock;

    private int speed = MAX_SPEED; // falling speed (Unit: mm)
    private static final int MIN_SPEED = 800;
    private static final int MAX_SPEED = 1500;

    private boolean isPause = true;
    private volatile boolean isQuickly = false;
    private int score = 0;

    private static GameManager gameManager;

    private volatile OnBlockChangeListener onBlockChangeListener;

    public interface OnBlockChangeListener {
        /**
         * when the falling block fix on the floor.
         */
        void onBlockChange();
    }

    private OnScoreListener onScoreListener;

    public interface OnScoreListener {
        void onScore(int score);
    }

    private GameManager(Handler handler, TetrisActivity activity) {
        this.handler = handler;
        this.activity = activity;
    }

    public static GameManager create(@NonNull Handler handler, TetrisActivity activity) {
        gameManager = new GameManager(handler, activity);
        return gameManager;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public void start() {
        if (floor == null || onBlockChangeListener == null || fallingBlock == null) {
            return;
        }
        isPause = false;
        if (onScoreListener != null) {
            onScoreListener.onScore(score);
        }
        fall();
    }

    private Runnable fallRunnable = new Runnable() {
        @Override
        public void run() {
            if (!floor.isRotating()) {
                if (canBlockFall()) {
                    fallingBlock.fall();
                } else {
                    floor.fixBlock(fallingBlock);
                    checkScore();
                    if (isQuickly) {
                        speed *= 50;
                        isQuickly = false;
                    }

                    if (onBlockChangeListener != null) {
                        onBlockChangeListener.onBlockChange();
                    }
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
        handler.removeCallbacksAndMessages(null);
    }

    private void gameOver() {
        pause();
        showGameOverDialog();
    }

    private void showGameOverDialog() {
        if (activity == null) {
            return;
        }
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_game_over, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setView(view)
                .create();

        final EditText editText = view.findViewById(R.id.edit_name);
        view.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                RankManager.getManager().getRankList().add(new RankItem(name, score));
                RankManager.getManager().saveList();

                editText.setText(null);
                editText.setHint(R.string.save_successful);
                editText.setEnabled(false);
                v.setClickable(false);
            }
        });

        view.findViewById(R.id.tv_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                activity.finish();
            }
        });


        alertDialog.show();

//        new AlertDialog.Builder(activity)
//                .setMessage(R.string.game_over_message)
//                .setTitle(R.string.game_over)
//                .setCancelable(false)
//                .setNegativeButton(R.string.restart, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        restart();
//                    }
//                })
//                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (activity != null) {
//                            activity.finish();
//                        }
//                    }
//                })
//                .create()
//                .show();
    }

    public void showPauseDialog() {
        if (activity == null) {
            return;
        }
        new AlertDialog.Builder(activity)
                .setTitle(R.string.pause)
                .setCancelable(false)
                .setNeutralButton(R.string.continue_, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        start();
                    }
                })
                .setNegativeButton(R.string.restart, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restart();
                    }
                })
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .create()
                .show();
    }

    private void restart() {
        handler.removeCallbacksAndMessages(null);
        floor.restart();
        if (onBlockChangeListener != null) {
            onBlockChangeListener.onBlockChange();
        }
        // todo rank list
        score = 0;
        if (onScoreListener != null) {
            onScoreListener.onScore(score);
        }
        start();
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
        gameManager = null;
    }

    public void fallQuickly() {
        if (isQuickly) {
            return;
        }
        isQuickly = true;
        handler.removeCallbacksAndMessages(null);
        speed = speed / 50;
        fall();
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

    public boolean detectCollision(int height, boolean[][][] validSpace, BlockType[][][] blockList) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (height - 2 + j >= 0 && validSpace[i][j][k] && blockList[i][height - 2 + j][k] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void setFallingBlock(BaseBlock block) {

        if (GameManager.getGameManager() != null && block != null &&
                !GameManager.getGameManager().detectCollision(block.getHeight(), block.getValidSpace(), GameManager.getGameManager().getFloor().getBlockList())) {
            gameOver();
        }

        fallingBlock = block;
        fallingBlock.setOnBlockFallingListener(new BaseBlock.OnBlockFallingListener() {
            @Override
            public void onFallingFinished() {
                if (!canBlockFall()) {
                    floor.fixBlock(fallingBlock);
                    checkScore();
                    if (isQuickly) {
                        speed *= 50;
                        isQuickly = false;
                    }

                    if (onBlockChangeListener != null) {
                        onBlockChangeListener.onBlockChange();
                    }
                }
            }
        });
    }

    /**
     * If there are 9 blocks (full) in one XY plane, delete the block and get 100 score.
     */
    private void checkScore() {
        BlockType[][][] blockList = floor.getBlockList();
        for (int height = 0; height < BaseBlock.MAX_HEIGHT; height++) {
            // z
            boolean isFull = true;
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    //xy
                    if (blockList[j][height][k] == null) {
                        isFull = false;
                    }
                }
            }
            if (isFull) { // Score
                deletePlane(blockList, height);
                score += 100;
                if (onScoreListener != null) {
                    onScoreListener.onScore(score);
                }
                height--;
            }
        }
    }

    public void checkLevel() {
        speed = MAX_SPEED;
        int score = this.score;
        while (score >= 500) {
            speed -= 100;
            score -= 500;
            if (speed <= MIN_SPEED) {
                speed = MIN_SPEED;
                return;
            }
        }
    }

    private void deletePlane(BlockType[][][] blockList, int height) {
        // clone
        BlockType[][][] temp = new BlockType[3][BaseBlock.MAX_HEIGHT][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < BaseBlock.MAX_HEIGHT; j++) {
                for (int k = 0; k < 3; k++) {
                    temp[i][j][k] = blockList[i][j][k];
                }
            }
        }
        // delete
        for (int j = height; j < BaseBlock.MAX_HEIGHT; j++) {
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 3; k++) {
                    if (j + 1 < BaseBlock.MAX_HEIGHT) {
                        blockList[i][j][k] = temp[i][j + 1][k];
                    } else if (j == BaseBlock.MAX_HEIGHT - 1) {
                        blockList[i][j][k] = null;
                    }
                }
            }
        }
    }

    public Floor getFloor() {
        return floor;
    }

    public BaseBlock getFallingBlock() {
        return fallingBlock;
    }

    public void setOnBlockChangeListener(OnBlockChangeListener onBlockChangeListener) {
        this.onBlockChangeListener = onBlockChangeListener;
    }

    public void setOnScoreListener(OnScoreListener onScoreListener) {
        this.onScoreListener = onScoreListener;
    }
}
