package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julio Guskuma on 07/02/17.
 */

public class Genre implements Parcelable {

    public static final String EXTRA_NAME = Genre.class.getSimpleName();

    public int id;
    public String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Genre() {
    }

    protected Genre(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
