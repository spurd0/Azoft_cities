package com.azcltd.android.test.babenko.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.azcltd.android.test.babenko.managers.ImageManager;
import com.azcltd.android.test.babenko.utils.UtilsHelper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CitiesActivity extends AppCompatActivity {
    private static final String TAG = "CitiesActivity";
    private static final int STORAGE_PERMISSION_CODE = 1;
    private ListView mCitiesLv;
    private TextView mConnectionErrorTv;
    private ArrayList<City> mCityList;
    private CitiesAdapter mCitiesAdapter;
    private MaterialDialog mServerErrorDialog;
    private MaterialDialog mPermissionsDialog;
    private MaterialDialog mNoPermissionsDialog;
    private MaterialDialog mDownloadingCitiesDialog;
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

    private void handleListState(boolean empty) {
        mCitiesLv.setVisibility(empty ? View.INVISIBLE : View.VISIBLE);
        mConnectionErrorTv.setVisibility(empty ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCityList == null)
            if (!UtilsHelper.checkStoragePermissions(this) && !mPermissionsAsked
                    && mNoPermissionsDialog == null)
                showPermissionsDialog();
            else if (mPermissionsAsked)
                return;
            else requestCities();
    }

    private void showPermissionsDialog() {
        if (this.isFinishing()) //todo move to runnable & run in onresume if activity is finishing
            return;
        if (mPermissionsDialog != null)
            return;
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
        if (this.isFinishing()) //todo move to runnable & run in onresume if activity is finishing
            return;
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
        if (mCityList != null)
            return;
        mDownloadingCitiesDialog = new MaterialDialog.Builder(this)
                .title(R.string.loading_dialog_title)
                .content(R.string.loading_dialog_content)
                .cancelable(false)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mDownloadingCitiesDialog = null;
                    }
                })
                .progress(true, 0)
                .show();
        CitiesManager.getInstance().requestCities(new CitiesManager.CitiesRequestCallback() {
            @Override
            public void gotCities(Cities cities) {
                boolean listIsEmpty = cities == null || cities.cities.isEmpty();
                if (listIsEmpty) {
                    handleListState(listIsEmpty);
                    return;
                }
                //if (BuildConfig.DEBUG) Log.d(TAG, cities.toString());
                receivedCitiesList(cities.cities);
            }

            @Override
            public void responseError(Throwable t) {
                if (BuildConfig.DEBUG) Log.e(TAG, t.toString());
                mDownloadingCitiesDialog.dismiss();
                showServerErrorDialog(t.getMessage());
            }
        });
    }

    private void downloadImages(boolean permitted) {
        ArrayList<String> imagesToDownload = new ArrayList<>();
        for (final City city : mCityList)
            if (!city.image_url.isEmpty())
                imagesToDownload.add(city.image_url);
        final AtomicInteger count = new AtomicInteger(imagesToDownload.size());

        for (String image_url : imagesToDownload)
            ImageManager.getInstance().downloadImage(image_url, permitted, new ImageManager.ImageCallback() {
                @Override
                public void imageDownloaded(final String fileName) {
                    handleImageLoad(count);
                }

                @Override
                public void failedToLoadImage(String fileName) {
                    if (BuildConfig.DEBUG)
                        Log.e(TAG, "Failed to load image " + fileName);
                    handleImageLoad(count);
                }
            });
    }

    private void handleImageLoad(AtomicInteger count) {
        if (count.decrementAndGet() == 0)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDownloadingCitiesDialog.dismiss();
                    initCitiesListView();
                }
            });
    }

    private void showServerErrorDialog(String content) {
        if (this.isFinishing()) //todo move to runnable & run in onresume if activity is finishing
            return;
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
                        mServerErrorDialog.dismiss();
                    }
                })
                .cancelable(false)
                .show();
    }

    private void receivedCitiesList(ArrayList<City> cities) {
        mCityList = cities;
        downloadImages(UtilsHelper.checkStoragePermissions(this));
    }

    private void initCitiesListView() {
        mCitiesAdapter = new CitiesAdapter(this, mCityList);
        mCitiesLv.setAdapter(mCitiesAdapter);
        mCitiesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City clickedCity = mCitiesAdapter.getItem(position);
                Intent intent = new Intent(CitiesActivity.this, CityDetailsActivity.class);
                intent.putExtra(CityDetailsActivity.DESCRIPTION_KEY, clickedCity.description);
                intent.putExtra(CityDetailsActivity.IMAGE_PATH_KEY, clickedCity.image_url);
                CitiesActivity.this.startActivity(intent);
            }
        });
        handleListState(false);
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
        if (mDownloadingCitiesDialog != null)
            mDownloadingCitiesDialog.dismiss();
    }
}
