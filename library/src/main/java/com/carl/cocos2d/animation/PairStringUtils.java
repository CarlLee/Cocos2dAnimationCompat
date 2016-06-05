package com.carl.cocos2d.animation;

/**
 * Utility class to parse "{x, y}" strings
 * Created by Carl on 2016/6/3.
 */
public abstract class PairStringUtils {
    public static String[] parse(String pairString) {
        if (pairString == null || pairString.length() < 3) return null;
        pairString = pairString.substring(1, pairString.length() - 1);
        return pairString.split(",");
    }
}
