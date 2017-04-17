package com.azcltd.android.test.babenko;

import android.app.Application;
import android.content.Context;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 08/03/17.
 */

public class CitiesApplication extends Application {

    private static CitiesApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Context getApplication(){
        return sApplication;
    }
}