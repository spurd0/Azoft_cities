package com.azcltd.android.test.babenko;

import android.app.Application;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 08/03/17.
 */

public class CitiesApplication extends Application {

    private static CitiesApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static CitiesApplication getInstance() {
        return sInstance;
    }
}