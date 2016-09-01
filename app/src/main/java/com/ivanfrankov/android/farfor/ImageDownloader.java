package com.ivanfrankov.android.farfor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ImageDownloader<Token> extends HandlerThread {

    private static final String TAG = "ImageDownloader";
    private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    private Handler mHandler;
    Handler mResponseHadler;;
    private static final int MESSAGE_DOWNLOAD = 0;
    Listener<Token> mListener;

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    Token token = (Token)msg.obj;
                    handleRequest(token);
                }
            }
        };
    }

    public ImageDownloader(Handler handler) {
        super(TAG);
        mResponseHadler = handler;
    }

    public void newMessage(Token token, String url) {
        requestMap.put(token, url);
        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token)
                .sendToTarget();
    }

    private void handleRequest(final Token token) {
        final String url = requestMap.get(token);

        final String imageKey = String.valueOf(url);
        final Bitmap bitmap = LruCacheForImage.get().getBitmapFromMemCache(imageKey);

        if (url == null) return;
        try {
            if (bitmap == null) {
                byte[] bitmapBytes = new FlickFetch().getUrlByte(url);
                final Bitmap bitmapNew = decodeSampledBitmapFromByte(bitmapBytes, 512, 300);
                LruCacheForImage.get().addBitmapToMemoryCache(imageKey, bitmapNew);
                mResponseHadler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (requestMap.get(token) != url) return;
                        requestMap.remove(token);
                        mListener.ImageDownloaded(token, bitmapNew);
                    }
                });
            } else {
                mResponseHadler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (requestMap.get(token) != url) return;
                        requestMap.remove(token);
                        mListener.ImageDownloaded(token, bitmap);
                    }
                });

            }

            //byte[] bitmapBytes = new FlickFetch().getUrlByte(url);
            //final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);



        } catch (IOException ioE) {
            Log.e(TAG, "Error downloading image", ioE);
        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromByte(byte[] bitmapBytes, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    public interface Listener<Token> {
        void ImageDownloaded(Token token, Bitmap bitmap);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }
}
