package com.azcltd.android.test.babenko.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.azcltd.android.test.babenko.CitiesApplication;
import com.azcltd.android.test.babenko.R;
import com.azcltd.android.test.babenko.constants.ApplicationConstants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 11/03/17.
 */

public class ImageManager {
    private static ImageManager sImageManager;
    private static final String TAG = "ImageManager";

    private List<Target> mTargets;

    public ImageManager() {
        mTargets = new ArrayList<Target>();
    }

    public static ImageManager getInstance() {
        if (sImageManager == null)
            sImageManager = new ImageManager();
        return sImageManager;
    }

    public void downloadImage(String imageName, ImageCallback callback) {
        File file = new File(
                Environment.getExternalStorageDirectory().getPath()
                        + "/" + ApplicationConstants.APPLICATION_FOLDER + "/" + imageName);
        if (file.exists()) {
            callback.imageDownloaded();
            return;
        }
        String imageUrl = CitiesApplication.getContext().getResources().getString(R.string.azcltd_api_url) + imageName;
        Target target = prepareImageTarget(imageName, callback);
        mTargets.add(target);
        Picasso.with(CitiesApplication.getContext()).load(imageUrl).into(target);
    }

    private Target prepareImageTarget(final String fileName, final ImageCallback callback) {
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File file = new File(
                                Environment.getExternalStorageDirectory().getPath()
                                        + "/" + ApplicationConstants.APPLICATION_FOLDER + "/" + fileName);
                        file.getParentFile().mkdirs();
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                            callback.imageDownloaded();
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.failedToLoadImage();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                callback.failedToLoadImage();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    public interface ImageCallback {
        void imageDownloaded();
        void failedToLoadImage();
    }
}
