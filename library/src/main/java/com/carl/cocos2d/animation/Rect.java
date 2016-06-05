package com.carl.cocos2d.animation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Carl on 2016/6/2.
 */
public class Rect {
    public Point topLeft;
    public Size size;

    public static Rect fromString(String rectString) {
        Pattern p = Pattern.compile("(\\{\\d+, *\\d+\\})");
        Matcher m = p.matcher(rectString);
        Rect rect = null;
        if(m.find()) {
            rect = new Rect();
            String pointStr = m.group(1);
            if (pointStr != null) {
                rect.topLeft = Point.fromString(pointStr);
            }
        }

        if(m.find() && rect != null) {
            String sizeStr = m.group(1);
            if(sizeStr != null) {
                rect.size = Size.fromString(sizeStr);
            }
        }

        return rect;
    }
}
