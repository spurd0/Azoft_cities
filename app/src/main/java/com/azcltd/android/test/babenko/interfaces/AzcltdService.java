package com.azcltd.android.test.babenko.interfaces;

import com.azcltd.android.test.babenko.data.Cities;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 08/03/17.
 */

public interface AzcltdService {
    @GET("/cities.json")
    Call<Cities> getCities();
}
