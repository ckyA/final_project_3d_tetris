package com.cky.a3dtetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cky.a3dtetris.rank.RankManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RankManager.create(getExternalCacheDir() + "/" + RankManager.FILE_RANK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RankManager.destroy();
    }

    public void start(View view) {
        startActivity(new Intent(this, TetrisActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void rank(View view) {
        // todo
    }

    public void settings(View view) {

    }
}
