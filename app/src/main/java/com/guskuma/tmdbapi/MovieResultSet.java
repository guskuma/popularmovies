package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Julio Guskuma on 03/02/17.
 */

public class MovieResultSet implements Parcelable {

    public static final String EXTRA_NAME = MovieResultSet.class.getSimpleName();

    public int page;
    public List<Movie> results;
    public DateInterval dates;
    public int total_pages;
    public int total_results;

    public MovieResultSet() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
        dest.writeParcelable(this.dates, flags);
        dest.writeInt(this.total_pages);
        dest.writeInt(this.total_results);
    }

    protected MovieResultSet(Parcel in) {
        this.page = in.readInt();
        this.results = in.createTypedArrayList(Movie.CREATOR);
        this.dates = in.readParcelable(DateInterval.class.getClassLoader());
        this.total_pages = in.readInt();
        this.total_results = in.readInt();
    }

    public static final Parcelable.Creator<MovieResultSet> CREATOR = new Parcelable.Creator<MovieResultSet>() {
        @Override
        public MovieResultSet createFromParcel(Parcel source) {
            return new MovieResultSet(source);
        }

        @Override
        public MovieResultSet[] newArray(int size) {
            return new MovieResultSet[size];
        }
    };
}
