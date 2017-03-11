package com.azcltd.android.test.babenko.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import java.io.File;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 11/03/17.
 */

public class UtilsHelper {
    public static boolean checkIfFileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    private static void requestPermission(Activity activity, String[] permission, int code) {
        ActivityCompat.requestPermissions(
                activity,
                permission,
                code);
    }

    public static boolean checkStoragePermissions(Activity activity) {
        return checkPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private static boolean checkPermission(Context context, String[] permissions) {
        boolean granted = true;
        for (String perm : permissions)
            granted = ActivityCompat.checkSelfPermission(context, perm)
                    == PackageManager.PERMISSION_GRANTED;
        return granted;
    }

    public static void requestStoragePermissions(Activity activity, int code) {
        requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, code);
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

}
