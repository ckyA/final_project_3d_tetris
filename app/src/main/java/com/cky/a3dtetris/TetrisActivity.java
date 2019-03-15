package com.cky.a3dtetris;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

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

        gameView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX = event.getX();
                    touchY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

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

                    if (event.getX() < ((float) renderer.getScreenWidth()) / 2f) {
                        if (event.getY() > touchY) {
                            renderer.getBlockB().rotateY(true);
                        } else {
                            renderer.getBlockB().rotateY(false);
                        }
                    } else {
                        if (event.getY() > touchY) {
                            renderer.getBlockB().rotateX(true);
                        } else {
                            renderer.getBlockB().rotateX(false);
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
