package com.carl.cocos2d.animation;

/**
 * Created by Carl on 2016/6/2.
 */
public class MetaData {
    private String version;
    private int format;
    private Size size;
    private String name;
    private boolean premultipliedAlpha;
    private Target target;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPremultipliedAlpha() {
        return premultipliedAlpha;
    }

    public void setPremultipliedAlpha(boolean premultipliedAlpha) {
        this.premultipliedAlpha = premultipliedAlpha;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
