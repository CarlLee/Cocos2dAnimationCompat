package com.carl.cosos2d.animation;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.carl.cocos2d.animation.AssetsFileOpener;
import com.carl.cocos2d.animation.SpriteSheet;
import com.carl.cocos2d.animation.SpriteSheetAnimationAdapter;
import com.carl.cocos2d.animation.SpriteSheetParser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.iv);

        AssetsFileOpener fileOpener = new AssetsFileOpener(getApplicationContext());
        SpriteSheetParser p = new SpriteSheetParser(fileOpener);
        boolean success = p.parse("plant_1_4_n_2_1_anim.plist");
        if (success && iv != null) {
            SpriteSheet result = p.getResult();
            SpriteSheetAnimationAdapter animAdapter = new SpriteSheetAnimationAdapter(getResources(),
                    result, fileOpener);
            AnimationDrawable animationDrawable = animAdapter.loadAnimation();
            animationDrawable.setOneShot(false);
            iv.setImageDrawable(animationDrawable);
            animationDrawable.start();
        }
    }
}
