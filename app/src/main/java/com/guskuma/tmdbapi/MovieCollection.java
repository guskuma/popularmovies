package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by juak on 07/02/17.
 */

public class MovieCollection implements Parcelable {

    public static final String EXTRA_NAME = MovieCollection.class.getSimpleName();

    public int id;
    public String name;
    public String poster_path;
    public String backdrop_path;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
    }

    public MovieCollection() {
    }

    protected MovieCollection(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
    }

    public static final Parcelable.Creator<MovieCollection> CREATOR = new Parcelable.Creator<MovieCollection>() {
        @Override
        public MovieCollection createFromParcel(Parcel source) {
            return new MovieCollection(source);
        }

        @Override
        public MovieCollection[] newArray(int size) {
            return new MovieCollection[size];
        }
    };
}
