package com.carl.cocos2d.animation;

import java.util.List;

/**
 * Containing data of sprite animation
 * Created by Carl on 2016/6/2.
 */
public class SpriteSheet {
    private List<Sprite> frames;
    private MetaData metaData;

    public List<Sprite> getFrames() {
        return frames;
    }

    public void setFrames(List<Sprite> frames) {
        this.frames = frames;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
