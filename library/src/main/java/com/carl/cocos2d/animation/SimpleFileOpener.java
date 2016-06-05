package com.carl.cocos2d.animation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Carl on 2016/6/2.
 */
public class SimpleFileOpener implements FileOpener {
    @Override
    public InputStream open(String path) throws IOException {
        return new FileInputStream(path);
    }
}
