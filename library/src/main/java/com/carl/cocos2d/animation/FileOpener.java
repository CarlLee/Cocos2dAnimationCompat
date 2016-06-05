package com.carl.cocos2d.animation;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Carl on 2016/6/2.
 */
public interface FileOpener {
    InputStream open(String path) throws IOException;
}
