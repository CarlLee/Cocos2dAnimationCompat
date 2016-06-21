package com.carl.cocos2d.animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

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
    private static final int IN_SAMPLE_SIZE_NONE = -1;
    private final FileOpener mFileOpener;
    private int mFrameRate;
    private TreeMap<String, Drawable> mFrameDrawables;
    private AnimationDrawable mAnimationDrawable;
    private Bitmap mTexture;
    private int mBitmapDensity = SpriteDrawable.DENSITY_UNKNOWN;
    private int mRequestedWidth = -1;
    private int mRequestedHeight = -1;
    private int mInSampleSize = IN_SAMPLE_SIZE_NONE;

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

    public void setRequestedSize(int width, int height) {
        this.mRequestedWidth = width;
        this.mRequestedHeight = height;
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
     *
     * @param spriteSheets sprite sheets that make up the animation
     * @param resources    resources of the context, used to retrieve screen density
     * @return the {@link AnimationDrawable}
     */
    public AnimationDrawable loadAnimation(Collection<SpriteSheet> spriteSheets,
                                           Resources resources) {
        if (mRequestedHeight > 0 && mRequestedWidth > 0) {
            FileOpener fileOpener = mFileOpener;
            if (fileOpener == null) {
                return null;
            }
            int lowestInSampleSize = IN_SAMPLE_SIZE_NONE;
            for (SpriteSheet spriteSheet : spriteSheets) {
                int inSampleSize = determineInSampleSize(spriteSheet, fileOpener);
                if (lowestInSampleSize > 0) {
                    if (inSampleSize < lowestInSampleSize) {
                        lowestInSampleSize = inSampleSize;
                    }
                } else {
                    lowestInSampleSize = inSampleSize;
                }
            }

            mInSampleSize = lowestInSampleSize;
        }
        for (SpriteSheet spriteSheet : spriteSheets) {
            doLoadAnimation(spriteSheet, resources);
        }
        return mAnimationDrawable;
    }

    /**
     * Load one {@link SpriteSheet} into one single AnimationDrawable,
     * multiple calls to this
     *
     * @param spriteSheet sprite sheet that makes up the animation
     * @param resources   resources of the context, used to retrieve screen density
     * @return the {@link AnimationDrawable}
     */
    public AnimationDrawable loadAnimation(SpriteSheet spriteSheet, Resources resources) {
        if (mFileOpener == null) {
            return null;
        }
        if (mRequestedHeight > 0 && mRequestedWidth > 0) {
            mInSampleSize = determineInSampleSize(spriteSheet, mFileOpener);
        }
        return doLoadAnimation(spriteSheet, resources);
    }

    private AnimationDrawable doLoadAnimation(SpriteSheet spriteSheet, Resources resources) {
        MetaData metaData = spriteSheet.getMetaData();
        List<Sprite> frames = spriteSheet.getFrames();
        FileOpener fileOpener = mFileOpener;
        float srcRectScaleFactor = 1.0f;

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
            BitmapFactory.Options options = null;
            if (mInSampleSize != IN_SAMPLE_SIZE_NONE) {
                options = new BitmapFactory.Options();
                options.inSampleSize = mInSampleSize;
                srcRectScaleFactor = mInSampleSize;
                Log.d(TAG, "inSampleSize: " + srcRectScaleFactor);
            }
            mTexture = BitmapFactory.decodeStream(fileOpener.open(textureFileName),
                    null, options);
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

            srcRect.left = (int) (srcRect.left / srcRectScaleFactor + 0.5F);
            srcRect.right = (int) (srcRect.right / srcRectScaleFactor + 0.5F);
            srcRect.top = (int) (srcRect.top / srcRectScaleFactor + 0.5F);
            srcRect.bottom = (int) (srcRect.bottom / srcRectScaleFactor + 0.5F);

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

    private int determineInSampleSize(SpriteSheet spriteSheet, FileOpener fileOpener) {

        MetaData metaData = spriteSheet.getMetaData();

        if (metaData == null) {
            return IN_SAMPLE_SIZE_NONE;
        }
        Target target = metaData.getTarget();
        String textureFileName = target.getTextureFileName() + target.getTextureFileExtension();
        try {
            BitmapFactory.Options options = BitmapUtils.buildBitmapFactoryOptions(fileOpener.open(textureFileName),
                    mRequestedWidth, mRequestedHeight);
            return options.inSampleSize;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IN_SAMPLE_SIZE_NONE;
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
