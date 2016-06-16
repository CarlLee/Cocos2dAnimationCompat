package com.carl.cocos2d.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Drawable that draws a sprite on a canvas, reuse texture to preserve memory
 * Created by Carl on 2016/6/12.
 */
public class SpriteDrawable extends Drawable {
    public static final int DENSITY_UNKNOWN = -1;
    private final Paint mPaint;
    private final Bitmap mTexture;
    private final int mWidth;
    private final int mHeight;
    private boolean mRotate;
    private Rect mSrcRect;
    private Rect mDstRect;
    private int mAlpha = 255;

    public SpriteDrawable(Bitmap texture, Rect srcRect, Rect dstRect, int height, int width,
                          boolean rotate, DisplayMetrics displayMetrics) {
        this(texture, srcRect, dstRect, height, width, rotate, displayMetrics, DENSITY_UNKNOWN);
    }

    public SpriteDrawable(Bitmap texture, Rect srcRect, Rect dstRect, int height, int width,
                          boolean rotate, DisplayMetrics displayMetrics, int textureDensity) {
        this.mTexture = texture;
        this.mSrcRect = srcRect;
        this.mDstRect = dstRect;
        this.mRotate = rotate;
        this.mPaint = new Paint();
        mPaint.setAntiAlias(true);

        this.mWidth = scaleFromDensity(width, textureDensity, displayMetrics.densityDpi);
        this.mHeight = scaleFromDensity(height, textureDensity, displayMetrics.densityDpi);
        scaleRect(mSrcRect, textureDensity, displayMetrics.densityDpi);
        scaleRect(mDstRect, textureDensity, displayMetrics.densityDpi);

        if (rotate) {
            int originLeft = mDstRect.left;
            int originTop = mDstRect.top;
            int originRight = mDstRect.right;
            int originBottom = mDstRect.bottom;
            mDstRect.left = -originBottom;
            mDstRect.top = originLeft;
            mDstRect.right = -originTop;
            mDstRect.bottom = originRight;
        }
    }

    public Bitmap getTexture() {
        return mTexture;
    }

    public boolean isRotate() {
        return mRotate;
    }

    public void setRotate(boolean rotate) {
        mRotate = rotate;
    }

    public Rect getSrcRect() {
        return mSrcRect;
    }

    public void setSrcRect(Rect srcRect) {
        mSrcRect = srcRect;
    }

    public Rect getDstRect() {
        return mDstRect;
    }

    public void setDstRect(Rect dstRect) {
        mDstRect = dstRect;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setAlpha(mAlpha);
        if (mRotate) {
            canvas.save();
            canvas.rotate(-90F);
        }
        canvas.drawBitmap(mTexture, mSrcRect, mDstRect, mPaint);
        if (mRotate) {
            canvas.restore();
        }
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return mAlpha;
    }

    private void scaleRect(Rect rect, int textureDensity, int densityDpi) {
        rect.left = scaleFromDensity(rect.left, textureDensity, densityDpi);
        rect.top = scaleFromDensity(rect.top, textureDensity, densityDpi);
        rect.right = scaleFromDensity(rect.right, textureDensity, densityDpi);
        rect.bottom = scaleFromDensity(rect.bottom, textureDensity, densityDpi);
    }

    /**
     * Scale the size in pixel according to source and target density, allowing bitmap to be
     * displayed to desired height/width
     *
     * @param size     the size to scale
     * @param sdensity source density
     * @param tdensity target density
     * @return scaled size, rounded up
     */
    private int scaleFromDensity(int size, int sdensity, int tdensity) {
        if (sdensity == DENSITY_UNKNOWN || sdensity == tdensity) {
            return size;
        }

        // Scale by tdensity / sdensity, rounding up.
        return ((size * tdensity) + (sdensity >> 1)) / sdensity;
    }
}
