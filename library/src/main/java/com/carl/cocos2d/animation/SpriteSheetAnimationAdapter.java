package com.carl.cocos2d.animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

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
    private final FileOpener mFileOpener;
    private int mFrameRate;
    private TreeMap<String, Drawable> mFrameDrawables;
    private AnimationDrawable mAnimationDrawable;
    private Bitmap mTexture;
    private int mBitmapDensity = SpriteDrawable.DENSITY_UNKNOWN;

    public SpriteSheetAnimationAdapter(FileOpener fileOpener) {
        this(fileOpener, DEFAULT_FRAME_RATE);
    }

    public SpriteSheetAnimationAdapter(FileOpener fileOpener, int frameRate) {
        this.mFileOpener = fileOpener;
        this.mFrameRate = frameRate;
    }

    public int getBitmapDensity() {
        return mBitmapDensity;
    }

    public void setBitmapDensity(int bitmapDensity) {
        mBitmapDensity = bitmapDensity;
    }

    public int getFrameRate() {
        return mFrameRate;
    }

    public void setFrameRate(int frameRate) {
        this.mFrameRate = frameRate;
    }

    public Collection<Drawable> getFrames() {
        return mFrameDrawables.values();
    }

    /**
     * Release resources allocated by this adapter, free up memory. The adapter can be reused after
     * being released
     */
    public void release() {
        mFrameDrawables.clear();
        mTexture.recycle();
        mTexture = null;
        mAnimationDrawable = null;
    }

    /**
     * Load a series of {@link SpriteSheet}s into one single AnimationDrawable
     * @param spriteSheets sprite sheets that make up the animation
     * @param resources resources of the context, used to retrieve screen density
     * @return the {@link AnimationDrawable}
     */
    public AnimationDrawable loadAnimation(Collection<SpriteSheet> spriteSheets,
                                           Resources resources) {
        for (SpriteSheet spriteSheet : spriteSheets) {
            loadAnimation(spriteSheet, resources);
        }
        return mAnimationDrawable;
    }

    /**
     * Load one {@link SpriteSheet} into one single AnimationDrawable,
     * multiple calls to this
     * @param spriteSheet sprite sheet that makes up the animation
     * @param resources resources of the context, used to retrieve screen density
     * @return the {@link AnimationDrawable}
     */
    public AnimationDrawable loadAnimation(SpriteSheet spriteSheet, Resources resources) {
        MetaData metaData = spriteSheet.getMetaData();
        List<Sprite> frames = spriteSheet.getFrames();
        FileOpener fileOpener = mFileOpener;

        if (metaData == null || fileOpener == null) {
            return null;
        }

        if (mFrameDrawables == null) {
            mFrameDrawables = makeTreeMap();
        }

        Target target = metaData.getTarget();
        String textureFileName = target.getTextureFileName() + target.getTextureFileExtension();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        try {
            mTexture = BitmapFactory.decodeStream(fileOpener.open(textureFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mTexture == null) {
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
            android.graphics.Rect dstRect = new android.graphics.Rect(colorOrigin.x,
                    colorOrigin.y, colorOrigin.x + colorSize.w, colorOrigin.y + colorSize.h);

            SpriteDrawable drawable = new SpriteDrawable(mTexture, srcRect, dstRect,
                    spriteSourceSize.h, spriteSourceSize.w, rotated,
                    resources.getDisplayMetrics(), mBitmapDensity);
            mFrameDrawables.put(name, drawable);
        }

        mAnimationDrawable = new AnimationDrawable();
        for (Map.Entry<String, Drawable> entry : mFrameDrawables.entrySet()) {
            mAnimationDrawable.addFrame(entry.getValue(), 1000 / mFrameRate);
        }

        return mAnimationDrawable;
    }

    private TreeMap<String, Drawable> makeTreeMap() {
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
