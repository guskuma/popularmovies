package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by juak on 20/03/18.
 */

public class VideoResultSet implements Parcelable {

    public VideoResultSet(){}

    public int id;
    public List<Video> results;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.results);
    }

    protected VideoResultSet(Parcel in) {
        this.id = in.readInt();
        this.results = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<VideoResultSet> CREATOR = new Creator<VideoResultSet>() {
        @Override
        public VideoResultSet createFromParcel(Parcel source) {
            return new VideoResultSet(source);
        }

        @Override
        public VideoResultSet[] newArray(int size) {
            return new VideoResultSet[size];
        }
    };

    @Override
    public String toString() {
        return "VideoResultSet{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }
}
