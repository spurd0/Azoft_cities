package com.azcltd.android.test.babenko.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.azcltd.android.test.babenko.R;

public class CitiesActivity extends AppCompatActivity {
    private ListView mCitiesLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_layout);
        initViews();
    }

    private void initViews() {
        mCitiesLV = (ListView) findViewById(R.id.citiesLv);
    }
}
