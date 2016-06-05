package com.carl.cocos2d.animation;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Carl on 2016/6/2.
 */
public class AssetsFileOpener implements FileOpener{

    private final Context mContext;

    public AssetsFileOpener(Context context) {
        this.mContext = context;
    }

    @Override
    public InputStream open(String path) throws IOException {
        AssetManager assets = mContext.getAssets();
        return assets.open(path);
    }
}
