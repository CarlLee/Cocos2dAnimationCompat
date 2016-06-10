package com.carl.cosos2d.animation;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.lv);

        AssetManager assets = getAssets();
        try {
            String[] assetsFiles = assets.list("");
            List<String> plists = new ArrayList<>();
            for (String file : assetsFiles) {
                Log.d(TAG, "file: " + file);
                if (file.endsWith("plist")) {
                    plists.add(file);
                }
            }

            if (lv != null) {
                mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        android.R.id.text1, plists);
                lv.setAdapter(mAdapter);
                lv.setOnItemClickListener(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = mAdapter.getItem(position);
//        Intent intent = new Intent(this, AnimationFrameActivity.class);
        Intent intent = new Intent(this, AnimationActivity.class);

        intent.putExtra(AnimationActivity.EXTRAS_ASSETS_FILE_NAME, item);
        startActivity(intent);
    }
}
