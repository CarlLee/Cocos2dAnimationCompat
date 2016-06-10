package com.carl.cosos2d.animation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.carl.cocos2d.animation.AssetsFileOpener;
import com.carl.cocos2d.animation.SpriteSheet;
import com.carl.cocos2d.animation.SpriteSheetAnimationAdapter;
import com.carl.cocos2d.animation.SpriteSheetParser;

/**
 * An activity to display animation
 * Created by Carl on 2016/6/7.
 */
public class AnimationActivity extends Activity {

    public static final String EXTRAS_ASSETS_FILE_NAME = "assets.filename";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String filename = extras.getString(EXTRAS_ASSETS_FILE_NAME, null);
        if (filename == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_animation);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        AssetsFileOpener fileOpener = new AssetsFileOpener(getApplicationContext());
        SpriteSheetParser p = new SpriteSheetParser(fileOpener);
        boolean success = p.parse(filename);
        if (success && iv != null) {
            SpriteSheet result = p.getResult();
            SpriteSheetAnimationAdapter animAdapter = new SpriteSheetAnimationAdapter(getResources(),
                    result, fileOpener);
            AnimationDrawable animationDrawable = animAdapter.loadAnimation();
            if(animationDrawable == null){
                finish();
                Toast.makeText(this, "Unable to create animation", Toast.LENGTH_SHORT).show();
                return;
            }
            animationDrawable.setOneShot(false);
            iv.setImageDrawable(animationDrawable);
            animationDrawable.start();
        } else {
            finish();
        }
    }
}
