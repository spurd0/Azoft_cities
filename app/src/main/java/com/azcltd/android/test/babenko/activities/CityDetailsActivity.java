package com.azcltd.android.test.babenko.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 12/03/17.
 */

public class CityDetailsActivity extends AppCompatActivity {
    private static final String TAG = "CityDetailsActivity";
    public static final String DESCRIPTION_KEY = "com.azcltd.android.test.babenko.activities.CityDetailsActivity.DESCRIPTION_KEY";
    public static final String IMAGE_PATH_KEY = "com.azcltd.android.test.babenko.activities.CityDetailsActivity.IMAGE_PATH_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String description = intent.getExtras().getString(DESCRIPTION_KEY);
        String imagePath = intent.getExtras().getString(IMAGE_PATH_KEY);
        Log.d(TAG, "imagepath " + imagePath);
    }
}
