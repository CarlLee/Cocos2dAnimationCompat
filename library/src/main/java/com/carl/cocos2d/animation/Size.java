package com.carl.cocos2d.animation;

/**
 * Created by Carl on 2016/6/2.
 */
public class Size {
    public int w;
    public int h;
    public static Size fromString(String pairString) {
        String[] pair = PairStringUtils.parse(pairString);
        if (pair != null) {
            Size p = new Size();
            p.w = Integer.parseInt(pair[0].trim());
            p.h = Integer.parseInt(pair[1].trim());
            return p;
        } else {
            return null;
        }
    }
}
