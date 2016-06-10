package com.carl.cosos2d.animation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.carl.cocos2d.animation.AssetsFileOpener;
import com.carl.cocos2d.animation.SpriteSheet;
import com.carl.cocos2d.animation.SpriteSheetAnimationAdapter;
import com.carl.cocos2d.animation.SpriteSheetParser;

import java.util.Collection;

/**
 * Created by Carl on 2016/6/10.
 */
public class AnimationFrameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String filename = extras.getString(AnimationActivity.EXTRAS_ASSETS_FILE_NAME, null);
        if (filename == null) {
            finish();
            return;
        }
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        sv.addView(parent);
        setContentView(sv);

        AssetsFileOpener fileOpener = new AssetsFileOpener(getApplicationContext());
        SpriteSheetParser p = new SpriteSheetParser(fileOpener);
        boolean success = p.parse(filename);
        if (success) {
            SpriteSheet result = p.getResult();

            SpriteSheetAnimationAdapter animAdapter = new SpriteSheetAnimationAdapter(getResources(),
                    result, fileOpener);
            animAdapter.loadAnimation();
            Collection<Bitmap> frames = animAdapter.getFrames();
            int count = 0;
            for (Bitmap b : frames) {
                ImageView iv = new ImageView(this);
                TextView tv = new TextView(this);
                tv.setText(String.format("frame %d", count++));
                iv.setBackgroundColor(Color.BLACK);
                BitmapDrawable bd = new BitmapDrawable(getResources(), b);
                iv.setImageDrawable(bd);
                iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                iv.setAdjustViewBounds(true);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                parent.addView(tv);
                parent.addView(iv);
            }
        }
    }
}
