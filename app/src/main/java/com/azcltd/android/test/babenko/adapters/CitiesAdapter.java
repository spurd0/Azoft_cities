package com.azcltd.android.test.babenko.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.azcltd.android.test.babenko.R;
import com.azcltd.android.test.babenko.data.Cities;
import com.azcltd.android.test.babenko.data.City;

import java.util.List;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 09/03/17.
 */

public class CitiesAdapter extends ArrayAdapter<City> {
    private Context mContext;
    private List<City> mCitiesList;

    public CitiesAdapter(@NonNull Context context, @NonNull List<City> objects) {
        super(context, R.layout.city_adapter_element, objects);
        mContext = context;
        mCitiesList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cityElementView = inflater.inflate(R.layout.city_adapter_element, parent, false);
        TextView cityNameTextView = (TextView) cityElementView.findViewById(R.id.cityElementName);
        ImageView cityImageView = (ImageView) cityElementView.findViewById(R.id.cityElementImage);
        cityNameTextView.setText(mCitiesList.get(position).name);
        //cityImageView.setImageAlpha();
        return cityElementView;
    }
}
