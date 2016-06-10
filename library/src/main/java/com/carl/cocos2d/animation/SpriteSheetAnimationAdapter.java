package com.carl.cocos2d.animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An adapter for {@link SpriteSheet} to make animations
 * Created by Carl on 2016/6/5.
 */
public class SpriteSheetAnimationAdapter {

    private static final String TAG = "SpriteSheetAnimAdapter";
    private static final int DEFAULT_FRAME_RATE = 24;
    private final SpriteSheet mSpriteSheet;
    private final FileOpener mFileOpener;
    private final Resources mResources;
    private int mFrameRate;
    private TreeMap<String, Bitmap> mFrameTextures;
    private AnimationDrawable mAnimationDrawable;

    public SpriteSheetAnimationAdapter(Resources resources, SpriteSheet spriteSheet,
                                       FileOpener fileOpener) {
        this(resources, spriteSheet, fileOpener, DEFAULT_FRAME_RATE);
    }

    public SpriteSheetAnimationAdapter(Resources resources, SpriteSheet spriteSheet,
                                       FileOpener fileOpener, int frameRate) {
        this.mResources = resources;
        this.mSpriteSheet = spriteSheet;
        this.mFileOpener = fileOpener;
        this.mFrameRate = frameRate;
    }

    public int getFrameRate() {
        return mFrameRate;
    }

    public void setFrameRate(int frameRate) {
        this.mFrameRate = frameRate;
    }

    public Collection<Bitmap> getFrames() {
        return mFrameTextures.values();
    }

    public AnimationDrawable loadAnimation() {
        if (mAnimationDrawable != null) {
            return mAnimationDrawable;
        }

        MetaData metaData = mSpriteSheet.getMetaData();
        List<Sprite> frames = mSpriteSheet.getFrames();
        FileOpener fileOpener = mFileOpener;

        if (metaData == null || fileOpener == null) {
            return null;
        }

        if (mFrameTextures != null) {
            freeTextures(mFrameTextures);
        }

        mFrameTextures = makeTreeMap();

        Target target = metaData.getTarget();
        String textureFileName = target.getTextureFileName() + target.getTextureFileExtension();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap texture = null;
        try {
            texture = BitmapFactory.decodeStream(fileOpener.open(textureFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (texture == null) {
            return null;
        }

        for (Sprite frame : frames) {
            String name = frame.getName();
            Point spriteOffset = frame.getSpriteOffset();
            Size spriteSize = frame.getSpriteSize();
            Size spriteSourceSize = frame.getSpriteSourceSize();
            Rect textureRect = frame.getTextureRect();
            Point textureOrigin = textureRect.topLeft;
            Size textureSize = textureRect.size;
            Rect colorRect = frame.getSpriteColorRect();
            Point colorOrigin = colorRect.topLeft;
            Size colorSize = colorRect.size;

            boolean rotated = frame.isTextureRotated();
            boolean trimmed = frame.isSpriteTrimmed();

            android.graphics.Rect srcRect =
                    new android.graphics.Rect(textureOrigin.x, textureOrigin.y,
                            textureOrigin.x + textureSize.w, textureOrigin.y + textureSize.h);
            android.graphics.Rect dstRect;

            Bitmap frameTexture = Bitmap.createBitmap(spriteSourceSize.w,
                        spriteSourceSize.h,Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(frameTexture);
            dstRect = new android.graphics.Rect(colorOrigin.x,
                    colorOrigin.y, colorOrigin.x + colorSize.w, colorOrigin.y + colorSize.h);
            c.save();
            if (rotated) {
                int originLeft = dstRect.left;
                int originTop = dstRect.top;
                int originRight = dstRect.right;
                int originBottom = dstRect.bottom;
                dstRect.left = -originBottom;
                dstRect.top = originLeft;
                dstRect.right = -originTop;
                dstRect.bottom = originRight;
                c.rotate(-90F);
            }
            c.drawBitmap(texture, srcRect, dstRect, paint);
            c.restore();
            //for debug
//            Paint p = new Paint();
//            p.setColor(Color.RED);
//            p.setStrokeWidth(1.0f);
//            p.setStyle(Paint.Style.STROKE);
//            c.drawRect(0, 0, spriteOffset.x, spriteOffset.y, p);
//            c.drawRect(0, 0, colorOrigin.x, colorOrigin.y, p);
//            c.drawRect(0, 0, frameTexture.getWidth() -1, frameTexture.getHeight()-1, p);
            mFrameTextures.put(name, frameTexture);
        }

        mAnimationDrawable = new AnimationDrawable();
        for (Map.Entry<String, Bitmap> entry : mFrameTextures.entrySet()) {
            BitmapDrawable bd = new BitmapDrawable(mResources, entry.getValue());
            mAnimationDrawable.addFrame(bd, 1000 / mFrameRate);
        }

        return mAnimationDrawable;
    }

    private void freeTextures(TreeMap<String, Bitmap> frameTextures) {
        for (Map.Entry<String, Bitmap> entry : frameTextures.entrySet()) {
            Bitmap bitmap = entry.getValue();
            bitmap.recycle();
        }
        frameTextures.clear();
    }

    private TreeMap<String, Bitmap> makeTreeMap() {
        return new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                // Fixes natural order mistakes where "abc10" is less than "abc2"
//                Log.d(TAG, String.format("lhs %s, rhs %s, lhs.length() - rhs.length(): %d",
//                        lhs, rhs, lhs.length() - rhs.length()));
                if (lhs.length() != rhs.length()) {
                    return lhs.length() - rhs.length();
                } else {
                    return lhs.compareTo(rhs);
                }
            }
        });
    }
}
