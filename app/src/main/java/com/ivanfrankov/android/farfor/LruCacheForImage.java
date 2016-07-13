package com.ivanfrankov.android.farfor;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class LruCacheForImage {

     private LruCache<String, Bitmap> mMemoryCache;
     private static LruCacheForImage sCacheForImage;

    private LruCacheForImage() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static LruCacheForImage get() {
        if (sCacheForImage == null)
            sCacheForImage = new LruCacheForImage();
        return sCacheForImage;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
