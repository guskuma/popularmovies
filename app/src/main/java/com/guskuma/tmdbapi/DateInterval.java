package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julio Guskuma on 03/02/17.
 */

public class DateInterval implements Parcelable {

    public String mininum;
    public String maximum;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mininum);
        dest.writeString(this.maximum);
    }

    public DateInterval() {
    }

    protected DateInterval(Parcel in) {
        this.mininum = in.readString();
        this.maximum = in.readString();
    }

    public static final Parcelable.Creator<DateInterval> CREATOR = new Parcelable.Creator<DateInterval>() {
        @Override
        public DateInterval createFromParcel(Parcel source) {
            return new DateInterval(source);
        }

        @Override
        public DateInterval[] newArray(int size) {
            return new DateInterval[size];
        }
    };
}
