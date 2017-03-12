package com.azcltd.android.test.babenko.managers;

import android.util.Log;

import com.azcltd.android.test.babenko.BuildConfig;
import com.azcltd.android.test.babenko.CitiesApplication;
import com.azcltd.android.test.babenko.R;
import com.azcltd.android.test.babenko.data.Cities;
import com.azcltd.android.test.babenko.interfaces.AzcltdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 07/03/17.
 */

public class CitiesManager {
    private static CitiesManager sCitiesManager;
    private static final String TAG = "CitiesManager";

    public static CitiesManager getInstance() {
        if (sCitiesManager == null)
            sCitiesManager = new CitiesManager();
        return sCitiesManager;
    }

    public void requestCities(final CitiesRequestCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CitiesApplication.getContext().getString(R.string.azcltd_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AzcltdService requestService = retrofit.create(AzcltdService.class);

        Call<Cities> call = requestService.getCities();
        call.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                if (BuildConfig.DEBUG) Log.d(TAG, "got cities");
                callback.gotCities(response.body());
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                if (BuildConfig.DEBUG) Log.e(TAG, "Error while requesting cities: " + t);
                callback.responseError(t);
            }
        });
    }

    public interface CitiesRequestCallback {
        void gotCities(Cities cities);
        void responseError(Throwable t);
    }
}
