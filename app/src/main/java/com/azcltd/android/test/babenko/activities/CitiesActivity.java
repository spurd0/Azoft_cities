package com.azcltd.android.test.babenko.activities;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import com.azcltd.android.test.babenko.adapters.CitiesAdapter;
import com.azcltd.android.test.babenko.data.Cities;
import com.azcltd.android.test.babenko.data.City;
import com.azcltd.android.test.babenko.managers.CitiesManager;
import com.azcltd.android.test.babenko.utils.UtilsHelper;

import java.util.ArrayList;

public class CitiesActivity extends AppCompatActivity {
    private static final String TAG = "CitiesActivity";
    private static final int STORAGE_PERMISSION_CODE = 1;
    private ListView mCitiesLv;
    private TextView mConnectionErrorTv;
    private ArrayList<City> mCitiesList;
    private MaterialDialog mServerErrorDialog;
    private MaterialDialog mPermissionsDialog;
    private MaterialDialog mNoPermissionsDialog;
    private boolean mPermissionsAsked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_layout);
        initViews();

    }

    private void initViews() {
        mCitiesLv = (ListView) findViewById(R.id.citiesLv);
        mConnectionErrorTv = (TextView) findViewById(R.id.connectionErrorTv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UtilsHelper.checkStoragePermissions(this)) {
            if (mCitiesList == null) requestCities();
        } else if (!mPermissionsAsked) showPermissionsDialog();
    }

    private void showPermissionsDialog() {
        mPermissionsDialog = new MaterialDialog.Builder(CitiesActivity.this)
                .title(R.string.permissions_dialog_title)
                .content(R.string.permissions_dialog_content)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mPermissionsDialog = null;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UtilsHelper.requestStoragePermissions(CitiesActivity.this, STORAGE_PERMISSION_CODE);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestCities();
                        mPermissionsDialog.dismiss();
                    }
                })
                .cancelable(false)
                .show();
    }


    private void showNoPermissionsDialog() {
        mNoPermissionsDialog = new MaterialDialog.Builder(CitiesActivity.this)
                .title(R.string.permissions_dialog_title)
                .content(R.string.permissions_error_dialog_content)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mNoPermissionsDialog = null;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UtilsHelper.startInstalledAppDetailsActivity(CitiesActivity.this);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestCities();
                        mNoPermissionsDialog.dismiss();
                    }
                })
                .cancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = grantResults.length != 0;
        for (int result : grantResults)
            if (result != PackageManager.PERMISSION_GRANTED)
                granted = false;
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (granted)
                    requestCities();
                else showNoPermissionsDialog();
                mPermissionsAsked = true;
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                initCitiesList(cities.cities);
            }

            @Override
            public void responseError(Throwable t) {
                if (BuildConfig.DEBUG) Log.e(TAG, t.toString());
                showServerErrorDialog(t.getMessage());
            }
        });
    }

    private void showServerErrorDialog(String content) {
        mServerErrorDialog = new MaterialDialog.Builder(CitiesActivity.this)
                .title(R.string.error_dialog_title)
                .content(content)
                .positiveText(R.string.error_dialog_try_again)
                .negativeText(android.R.string.cancel)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mServerErrorDialog = null;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestCities();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO: 08/03/17 show error tv
                        Log.d(TAG, "onNegative");
                        mServerErrorDialog.dismiss();
                    }
                })
                .cancelable(false)
                .show();
    }

    private void initCitiesList(ArrayList<City> cities) {
        mCitiesList = cities;
        CitiesAdapter adapter = new CitiesAdapter(this, mCitiesList);
        mCitiesLv.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPermissionsAsked = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServerErrorDialog != null)
            mServerErrorDialog.dismiss();
        if (mPermissionsDialog != null)
            mPermissionsDialog.dismiss();
        if (mNoPermissionsDialog != null)
            mNoPermissionsDialog.dismiss();
    }
}
