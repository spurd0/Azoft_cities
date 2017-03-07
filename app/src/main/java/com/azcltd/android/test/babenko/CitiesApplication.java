package com.azcltd.android.test.babenko;

import android.app.Application;
import android.content.Context;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 08/03/17.
 */

public class CitiesApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}