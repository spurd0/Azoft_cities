package com.azcltd.android.test.babenko.data;

/**
 * Created by Roman Babenko (babe-roman@yandex.ru) on 07/03/17.
 */

public class City {
    public int id;
    public String name;
    public String description;
    public String image_url;
    public String country;
    public Location location;

    @Override
    public String toString() {
        return "Name: " + name;
    }
}
