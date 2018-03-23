package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julio Guskuma on 07/02/17.
 */

public class Producer implements Parcelable {

    public static final String EXTRA_NAME = Producer.class.getSimpleName();

    public String name;
    public int id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public Producer() {
    }

    protected Producer(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Producer> CREATOR = new Parcelable.Creator<Producer>() {
        @Override
        public Producer createFromParcel(Parcel source) {
            return new Producer(source);
        }

        @Override
        public Producer[] newArray(int size) {
            return new Producer[size];
        }
    };
}
