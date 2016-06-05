package com.carl.cocos2d.animation;

/**
 * Created by Carl on 2016/6/3.
 */
public class Target {
    private String name;
    private String textureFileName;
    private String textureFileExtension;
    private String coordinatesFileName;
    private String coordinatesFileExtension;
    private boolean premultipliedAlpha;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }

    public String getTextureFileExtension() {
        return textureFileExtension;
    }

    public void setTextureFileExtension(String textureFileExtension) {
        this.textureFileExtension = textureFileExtension;
    }

    public String getCoordinatesFileExtension() {
        return coordinatesFileExtension;
    }

    public void setCoordinatesFileExtension(String coordinatesFileExtension) {
        this.coordinatesFileExtension = coordinatesFileExtension;
    }

    public String getCoordinatesFileName() {
        return coordinatesFileName;
    }

    public void setCoordinatesFileName(String coordinatesFileName) {
        this.coordinatesFileName = coordinatesFileName;
    }

    public boolean isPremultipliedAlpha() {
        return premultipliedAlpha;
    }

    public void setPremultipliedAlpha(boolean premultipliedAlpha) {
        this.premultipliedAlpha = premultipliedAlpha;
    }
}
