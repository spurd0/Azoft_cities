package com.azcltd.android.test.babenko.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.azcltd.android.test.babenko.BuildConfig;
import com.azcltd.android.test.babenko.R;
import com.azcltd.android.test.babenko.data.Cities;
import com.azcltd.android.test.babenko.managers.CitiesManager;

public class CitiesActivity extends AppCompatActivity {
    private static final String TAG = "CitiesActivity";
    private ListView mCitiesLv;
    private TextView mConnectionErrorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_layout);
        initViews();
        requestCities();
    }

    private void initViews() {
        mCitiesLv = (ListView) findViewById(R.id.citiesLv);
        mConnectionErrorTv = (TextView) findViewById(R.id.connectionErrorTv);
    }

    private void requestCities() {
        CitiesManager.getInstance().requestCities(new CitiesManager.CitiesRequestCallback() {
            @Override
            public void gotCities(Cities cities) {
                if (cities == null) {
                    showServerErrorDialog(getString(R.string.error_dialog_null_response));
                    return;
                }
                if (BuildConfig.DEBUG) Log.d(TAG, cities.toString());
                /// TODO: 08/03/17 threadpool for loading images
            }

            @Override
            public void responseError(Throwable t) {
                if (BuildConfig.DEBUG) Log.e(TAG, t.toString());
                showServerErrorDialog(t.getMessage());
            }
        });
    }

    private void showServerErrorDialog(String content) {
        new MaterialDialog.Builder(CitiesActivity.this)
                .title(R.string.error_dialog_title)
                .content(content)
                .positiveText(R.string.error_dialog_try_again)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestCities();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.d(TAG, "onNegative");
                    }
                })
                .cancelable(false)
                .show();
    }
}
