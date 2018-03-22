package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by juak on 21/03/18.
 */

public class ReviewResultSet implements Parcelable {

    public static final String EXTRA_NAME = ReviewResultSet.class.getSimpleName();

    public int id;
    public int page;
    public List<Review> results;
    public int total_pages;
    public int total_results;

    public ReviewResultSet(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
        dest.writeInt(this.total_pages);
        dest.writeInt(this.total_results);
    }

    protected ReviewResultSet(Parcel in) {
        this.id = in.readInt();
        this.page = in.readInt();
        this.results = in.createTypedArrayList(Review.CREATOR);
        this.total_pages = in.readInt();
        this.total_results = in.readInt();
    }

    public static final Creator<ReviewResultSet> CREATOR = new Creator<ReviewResultSet>() {
        @Override
        public ReviewResultSet createFromParcel(Parcel source) {
            return new ReviewResultSet(source);
        }

        @Override
        public ReviewResultSet[] newArray(int size) {
            return new ReviewResultSet[size];
        }
    };

    @Override
    public String toString() {
        return "ReviewResultSet{" +
                "id=" + id +
                ", page=" + page +
                ", results=" + results +
                ", total_pages=" + total_pages +
                ", total_results=" + total_results +
                '}';
    }
}
