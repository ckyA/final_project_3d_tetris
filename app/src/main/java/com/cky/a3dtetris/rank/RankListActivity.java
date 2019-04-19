package com.cky.a3dtetris.rank;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cky.a3dtetris.R;

public class RankListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);
        ListView listView = findViewById(R.id.lv_rank_list);
        listView.setAdapter(new RankListAdapter(this));
    }
}
