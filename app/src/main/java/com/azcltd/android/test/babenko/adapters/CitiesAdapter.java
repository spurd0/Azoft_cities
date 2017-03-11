package com.azcltd.android.test.babenko.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.azcltd.android.test.babenko.R;
import com.azcltd.android.test.babenko.data.City;
import com.squareup.picasso.Picasso;

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

    private static class ViewHolder {
        ImageView cityImageView;
        TextView cityNameTextView;
    }

    @Nullable
    @Override
    public City getItem(int position) {
        return mCitiesList.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null || convertView.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.city_adapter_element, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cityNameTextView = (TextView) convertView.findViewById(R.id.cityElementName);
            viewHolder.cityImageView = (ImageView) convertView.findViewById(R.id.cityElementImage);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.cityNameTextView.setText(getItem(position).name);

        viewHolder.cityImageView.post(new Runnable() {
            @Override
            public void run() {
                String imageUrl = mContext.getResources().getString(R.string.azcltd_api_url)
                        + "/" + getItem(position).image_url;

                int size = viewHolder.cityImageView.getWidth() > viewHolder.cityImageView.getHeight() ?
                        viewHolder.cityImageView.getHeight() : viewHolder.cityImageView.getWidth();

                Picasso.with(mContext).load(imageUrl)
                        .resize(size, size)
                        .centerInside()
                        .error(R.drawable.question_mark)
                        .into(viewHolder.cityImageView); // TODO: 11/03/17 handle image error
            }
        });
        return convertView;
    }
}
