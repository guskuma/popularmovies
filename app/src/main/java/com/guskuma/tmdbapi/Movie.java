package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by juak on 03/02/17.
 */

public class Movie implements Parcelable {

    /* REST
    {
        "poster_path": "/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg",
        "adult": false,
        "overview": "The quiet life of a terrier named Max is upended when his owner takes in Duke, a stray whom Max instantly dislikes.",
        "release_date": "2016-06-18",
        "genre_ids": [
            12,
            16,
            35,
            10751
        ],
        "id": 328111,
        "original_title": "The Secret Life of Pets",
        "original_language": "en",
        "title": "The Secret Life of Pets",
        "backdrop_path": "/lubzBMQLLmG88CLQ4F3TxZr2Q7N.jpg",
        "popularity": 149.543296,
        "vote_count": 1983,
        "video": false,
        "vote_average": 5.8
    }
    */



    public Movie() {}

    public static final String EXTRA_NAME = Movie.class.getSimpleName();

    public String poster_path;
    public boolean adult;
    public String overview;
    public String date;
    public Integer[] genre_id;
    public int id;
    public String original_title;
    public String original_language;
    public String title;
    public String backdrop_path;
    public double popularity;
    public int vote_count;
    public boolean video;
    public double vote_average;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.date);
        dest.writeArray(this.genre_id);
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.original_language);
        dest.writeString(this.title);
        dest.writeString(this.backdrop_path);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.vote_count);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.vote_average);
    }

    protected Movie(Parcel in) {
        this.poster_path = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.date = in.readString();
        this.genre_id = (Integer[]) in.readArray(Integer[].class.getClassLoader());
        this.id = in.readInt();
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.title = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = in.readDouble();
        this.vote_count = in.readInt();
        this.video = in.readByte() != 0;
        this.vote_average = in.readDouble();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
