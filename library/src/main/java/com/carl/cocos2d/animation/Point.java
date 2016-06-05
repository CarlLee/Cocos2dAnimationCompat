package com.carl.cocos2d.animation;

/**
 * Created by Carl on 2016/6/2.
 */
public class Point {
    public int x;
    public int y;

    public static Point fromString(String pairString) {
        String[] pair = PairStringUtils.parse(pairString);
        if (pair != null) {
            Point p = new Point();
            p.x = Integer.parseInt(pair[0].trim());
            p.y = Integer.parseInt(pair[1].trim());
            return p;
        } else {
            return null;
        }
    }
}
