package com.azcltd.android.test.babenko.data;

import java.util.ArrayList;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 07/03/17.
 */

public class Cities {
    public ArrayList<City> cities;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (City city : cities)
            builder.append(city.toString()).append("\n");
        return builder.toString();
    }
}
