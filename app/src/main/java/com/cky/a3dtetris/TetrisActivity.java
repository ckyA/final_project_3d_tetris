package com.cky.a3dtetris;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cky.a3dtetris.shape.BaseBlock;

public class TetrisActivity extends AppCompatActivity {

    private GameManager manager;
    private GameRenderer renderer;
    private ImageView pauseButton;
    private TextView scoreView;
    float touchX;
    float touchY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        pauseButton = findViewById(R.id.iv_pause);
        scoreView = findViewById(R.id.tv_score);

        GLSurfaceView gameView = findViewById(R.id.GLSurfaceView);
        gameView.setEGLContextClientVersion(2);

        gameView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        gameView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        gameView.setZOrderOnTop(true);

        Handler handler = new Handler();
        manager = GameManager.create(handler);
        manager.setOnScoreListener(new GameManager.OnScoreListener() {
            @Override
            public void onScore(final int score) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (scoreView != null) {
                            scoreView.setText(String.valueOf(score));
                        }
                    }
                });
            }
        });

        renderer = new GameRenderer(TetrisActivity.this, Utils.getScreenHeight(this), Utils.getScreenWidth(this), manager);
        gameView.setRenderer(renderer);
        gameView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        View.OnTouchListener listener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getX();
                    touchY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (Math.abs(event.getY() - touchY) < 30 && Math.abs(event.getX() - touchX) < 30) {
                        // Too short movement.
                        return true;
                    }

                    // check floor
                    int screenWidth = renderer.getScreenWidth();
                    int screenHeight = renderer.getScreenHeight();
                    int interval = (int) ((float) screenWidth / 3f);
                    int floorHeight = (int) (((float) screenWidth * 0.92f / 2f) + ((float) screenHeight / 2f));
                    if (event.getY() > floorHeight - 150 && event.getY() < floorHeight + 150
                            && touchY > floorHeight - 150 && touchY < floorHeight + 150) {
                        if (touchX > interval && touchX < screenWidth - interval) {
                            if (event.getX() > touchX) {
                                renderer.getFloor().rotate(true);
                            } else {
                                renderer.getFloor().rotate(false);
                            }
                            return true;
                        }
                    } else if (event.getY() > floorHeight + 150 && touchY > floorHeight + 150) {

                        // rotate the block in z axis
                        if (touchX > interval && touchX < screenWidth - interval) {
                            if (event.getX() > touchX) {
                                renderer.getFallingBlock().rotateZ(false);
                            } else {
                                renderer.getFallingBlock().rotateZ(true);
                            }
                            return true;
                        }
                    }

                    // check movement operation
                    float centerLeft = ((float) renderer.getScreenWidth()) / 3f;
                    float centerRight = ((float) renderer.getScreenWidth()) * 2 / 3f;
                    if (touchX < centerRight && touchX > centerLeft) {
                        if (event.getY() > touchY) {
                            if (event.getX() > touchX) {
                                renderer.getFallingBlock().move(BaseBlock.Direction.Y, true);
                            } else {
                                renderer.getFallingBlock().move(BaseBlock.Direction.X, true);
                            }
                        } else {
                            if (event.getX() > touchX) {
                                renderer.getFallingBlock().move(BaseBlock.Direction.X, false);
                            } else {
                                renderer.getFallingBlock().move(BaseBlock.Direction.Y, false);
                            }
                        }
                        return true;
                    }

                    // check rotation operation
                    if (event.getX() < ((float) renderer.getScreenWidth()) / 3f && touchX < ((float) renderer.getScreenWidth()) / 3f) {
                        if (event.getY() > touchY) {
                            renderer.getFallingBlock().rotateY(false);
                        } else {
                            renderer.getFallingBlock().rotateY(true);
                        }
                    } else if (event.getX() > ((float) renderer.getScreenWidth()) * 2f / 3f && touchX > ((float) renderer.getScreenWidth()) * 2f / 3f) {
                        if (event.getY() > touchY) {
                            renderer.getFallingBlock().rotateX(false);
                        } else {
                            renderer.getFallingBlock().rotateX(true);
                        }
                    }
                }
                return true;
            }
        };
        gameView.setOnTouchListener(listener);
    }

    public void stopOrStart(View view) {
        if (manager.isPause()) {
            manager.start();
            if (pauseButton != null) {
                pauseButton.setImageResource(R.drawable.ic_pause);
            }
        } else {
            manager.pause();
            if (pauseButton != null) {
                pauseButton.setImageResource(R.drawable.ic_play);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopOrStart(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.destroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
