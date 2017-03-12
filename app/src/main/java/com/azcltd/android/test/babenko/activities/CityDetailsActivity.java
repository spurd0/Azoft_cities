package com.azcltd.android.test.babenko.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.azcltd.android.test.babenko.R;
import com.azcltd.android.test.babenko.constants.ApplicationConstants;
import com.azcltd.android.test.babenko.utils.UtilsHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 12/03/17.
 */

public class CityDetailsActivity extends AppCompatActivity {
    private static final String TAG = "CityDetailsActivity";
    public static final String DESCRIPTION_KEY = "com.azcltd.android.test.babenko.activities.CityDetailsActivity.DESCRIPTION_KEY";
    public static final String IMAGE_PATH_KEY = "com.azcltd.android.test.babenko.activities.CityDetailsActivity.IMAGE_PATH_KEY";

    private ImageView mCityImage;
    private TextView mCityDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_details_layout);
        initViews();
        handleIntent();
    }

    private void initViews() {
        mCityImage = (ImageView) findViewById(R.id.city_details_image);
        mCityDescription = (TextView) findViewById(R.id.city_details_description);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String description = intent.getExtras().getString(DESCRIPTION_KEY);
        final String imagePath = intent.getExtras().getString(IMAGE_PATH_KEY);
        mCityDescription.setText(description.isEmpty() ? getString(R.string.empty_description) : description);
        mCityImage.post(new Runnable() {
            @Override
            public void run() {
                int size = mCityImage.getWidth();

                if (UtilsHelper.checkStoragePermissions(CityDetailsActivity.this)) {
                    File imageFile = new File(
                            Environment.getExternalStorageDirectory().getPath()
                                    + "/" + ApplicationConstants.APPLICATION_FOLDER + "/" + imagePath);
                    if (!imageFile.exists()) {
                        loadImageFromUrl(imagePath, size);
                        return;
                    }
                    Picasso.with(CityDetailsActivity.this).load(imageFile)
                            .resize(size, size)
                            .centerInside()
                            .error(R.drawable.question_mark)
                            .into(mCityImage);
                } else {
                    loadImageFromUrl(imagePath, size);
                }
            }
        });
    }

    private void loadImageFromUrl(String imageName, int size) {
        String imageUrl = CityDetailsActivity.this.getResources().getString(R.string.azcltd_api_url)
                + "/" + imageName;

        Picasso.with(CityDetailsActivity.this).load(imageUrl)
                .resize(size, size)
                .centerInside()
                .error(R.drawable.question_mark)
                .into(mCityImage);
    }
}
