package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by juak on 07/02/17.
 */

public class Language implements Parcelable {

    public static final String EXTRA_NAME = Language.class.getSimpleName();

    public String iso6391;
    public String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iso6391);
        dest.writeString(this.name);
    }

    public Language() {
    }

    protected Language(Parcel in) {
        this.iso6391 = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel source) {
            return new Language(source);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };
}
