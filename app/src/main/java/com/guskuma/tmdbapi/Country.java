package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julio Guskuma on 07/02/17.
 */

public class Country implements Parcelable {

    public static final String EXTRA_NAME = Country.class.getSimpleName();

    public String iso_639_1;
    public String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iso_639_1);
        dest.writeString(this.name);
    }

    public Country() {
    }

    protected Country(Parcel in) {
        this.iso_639_1 = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
