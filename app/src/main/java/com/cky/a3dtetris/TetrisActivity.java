package com.cky.a3dtetris;

import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.cky.a3dtetris.shape.BaseBlock;

public class TetrisActivity extends AppCompatActivity {

    private GameRenderer renderer;
    float touchX;
    float touchY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        GLSurfaceView gameView = findViewById(R.id.GLSurfaceView);
        gameView.setEGLContextClientVersion(2);

        gameView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        gameView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        gameView.setZOrderOnTop(true);

        Resources resources = this.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        renderer = new GameRenderer(TetrisActivity.this, displayMetrics.heightPixels, displayMetrics.widthPixels);
        gameView.setRenderer(renderer);
        gameView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        View.OnTouchListener listener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getX();
                    touchY = event.getY();s
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (Math.abs(event.getY() - touchY) < 100 && Math.abs(event.getX() - touchX) < 100) {
                        // Too short movement.
                        return true;
                    }

                    // check floor
                    int screenWidth = renderer.getScreenWidth();
                    int screenHeight = renderer.getScreenHeight();
                    int floorHeight = (int) (((float) screenWidth / 2f) + ((float) screenHeight / 2f));
                    if (touchY < floorHeight + 100 && touchY > floorHeight - 100) {
                        if (touchX > 200 && touchX < screenWidth - 200) {
                            if (event.getX() > touchX) {
                                renderer.getFloor().rotate(true);
                            } else {
                                renderer.getFloor().rotate(false);
                            }
                            return true;
                        }
                    }

                    // check movement operation
                    float centerLeft = ((float) renderer.getScreenWidth()) / 2f - ((float) renderer.getScreenWidth()) / 8f;
                    float centerRight = ((float) renderer.getScreenWidth()) / 2f + ((float) renderer.getScreenWidth()) / 8f;
                    if (touchX < centerRight && touchX > centerLeft) {
                        //  from 3/8 to 5/8
                        if (event.getY() > touchY) {
                            if (event.getX() > touchX) {
                                renderer.getBlockB().move(BaseBlock.Direction.Y, true);
                            } else {
                                renderer.getBlockB().move(BaseBlock.Direction.X, true);
                            }
                        } else {
                            if (event.getX() > touchX) {
                                renderer.getBlockB().move(BaseBlock.Direction.X, false);
                            } else {
                                renderer.getBlockB().move(BaseBlock.Direction.Y, false);
                            }
                        }
                        return true;
                    }

                    // check rotation operation
                    if (event.getX() < ((float) renderer.getScreenWidth()) * 3 / 8f && touchX < ((float) renderer.getScreenWidth()) * 3 / 8f) {
                        if (event.getY() > touchY) {
                            renderer.getBlockB().rotateX(false);
                        } else {
                            renderer.getBlockB().rotateX(true);
                        }
                    } else if (event.getX() > ((float) renderer.getScreenWidth()) * 5 / 8f && touchX > ((float) renderer.getScreenWidth()) * 5 / 8f) {
                        if (event.getY() > touchY) {
                            renderer.getBlockB().rotateY(false);
                        } else {
                            renderer.getBlockB().rotateY(true);
                        }
                    }
                }
                return true;
            }
        };
        gameView.setOnTouchListener(listener);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
