package com.azcltd.android.test.babenko.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.azcltd.android.test.babenko.BuildConfig;
import com.azcltd.android.test.babenko.R;
import com.azcltd.android.test.babenko.data.Cities;
import com.azcltd.android.test.babenko.managers.CitiesManager;

public class CitiesActivity extends AppCompatActivity {
    private static final String TAG = "CitiesActivity";
    private ListView mCitiesLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_layout);
        initViews();
        requestCities();
    }

    private void initViews() {
        mCitiesLV = (ListView) findViewById(R.id.citiesLv);
    }

    private void requestCities() {
        CitiesManager.getInstance(this).requestCities(new CitiesManager.CitiesRequestCallback() {
            @Override
            public void gotCities(Cities cities) {
                if (BuildConfig.DEBUG) Log.d(TAG, cities.toString());
            }

            @Override
            public void responseError(Throwable t) {
                if (BuildConfig.DEBUG) Log.e(TAG, t.toString());
            }
        });
    }
}
