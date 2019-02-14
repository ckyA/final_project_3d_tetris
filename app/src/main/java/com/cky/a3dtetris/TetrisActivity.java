package com.cky.a3dtetris;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class TetrisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        GLSurfaceView gameView = findViewById(R.id.GLSurfaceView);
        gameView.setEGLContextClientVersion(2);
        Resources resources = this.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        GameRenderer renderer = new GameRenderer(TetrisActivity.this, displayMetrics.heightPixels, displayMetrics.widthPixels);
        gameView.setRenderer(renderer);
        gameView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
}
