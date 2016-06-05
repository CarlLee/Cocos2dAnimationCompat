package com.carl.cocos2d.animation;


/**
 * Information about a sprite
 * Created by Carl on 2016/6/2.
 */
public class Sprite implements Cloneable{
    private String name;
    private Rect spriteColorRect;
    private Point spriteOffset;
    private Size spriteSize;
    private Size spriteSourceSize;
    private boolean spriteTrimmed;
    private Rect textureRect;
    private boolean textureRotated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rect getSpriteColorRect() {
        return spriteColorRect;
    }

    public void setSpriteColorRect(Rect spriteColorRect) {
        this.spriteColorRect = spriteColorRect;
    }

    public Point getSpriteOffset() {
        return spriteOffset;
    }

    public void setSpriteOffset(Point spriteOffset) {
        this.spriteOffset = spriteOffset;
    }

    public Size getSpriteSize() {
        return spriteSize;
    }

    public void setSpriteSize(Size spriteSize) {
        this.spriteSize = spriteSize;
    }

    public Size getSpriteSourceSize() {
        return spriteSourceSize;
    }

    public void setSpriteSourceSize(Size spriteSourceSize) {
        this.spriteSourceSize = spriteSourceSize;
    }

    public boolean isSpriteTrimmed() {
        return spriteTrimmed;
    }

    public void setSpriteTrimmed(boolean spriteTrimmed) {
        this.spriteTrimmed = spriteTrimmed;
    }

    public Rect getTextureRect() {
        return textureRect;
    }

    public void setTextureRect(Rect textureRect) {
        this.textureRect = textureRect;
    }

    public boolean isTextureRotated() {
        return textureRotated;
    }

    public void setTextureRotated(boolean textureRotated) {
        this.textureRotated = textureRotated;
    }

    public Sprite makeCopy() {
        try {
            return (Sprite) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
