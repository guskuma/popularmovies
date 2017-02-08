package com.guskuma.tmdbapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by juak on 07/02/17.
 */

public class DetailedMovie implements Parcelable {

    public static final String EXTRA_NAME = DetailedMovie.class.getSimpleName();

    public boolean adult;
    public String backdrop_path;
    public MovieCollection belongs_to_collection;
    public int budget;
    public List<Genre> genres = null;
    public String homepage;
    public int id;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;
    public List<Producer> production_companies = null;
    public List<Country> production_countries = null;
    public String release_date;
    public int revenue;
    public int runtime;
    public List<Language> spoken_languages = null;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    public double vote_average;
    public int vote_count;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdrop_path);
        dest.writeParcelable(this.belongs_to_collection, flags);
        dest.writeInt(this.budget);
        dest.writeTypedList(this.genres);
        dest.writeString(this.homepage);
        dest.writeInt(this.id);
        dest.writeString(this.imdb_id);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeTypedList(this.production_companies);
        dest.writeTypedList(this.production_countries);
        dest.writeString(this.release_date);
        dest.writeInt(this.revenue);
        dest.writeInt(this.runtime);
        dest.writeTypedList(this.spoken_languages);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.vote_average);
        dest.writeInt(this.vote_count);
    }

    public DetailedMovie() {
    }

    protected DetailedMovie(Parcel in) {
        this.adult = in.readByte() != 0;
        this.backdrop_path = in.readString();
        this.belongs_to_collection = in.readParcelable(MovieCollection.class.getClassLoader());
        this.budget = in.readInt();
        this.genres = in.createTypedArrayList(Genre.CREATOR);
        this.homepage = in.readString();
        this.id = in.readInt();
        this.imdb_id = in.readString();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.poster_path = in.readString();
        this.production_companies = in.createTypedArrayList(Producer.CREATOR);
        this.production_countries = in.createTypedArrayList(Country.CREATOR);
        this.release_date = in.readString();
        this.revenue = in.readInt();
        this.runtime = in.readInt();
        this.spoken_languages = in.createTypedArrayList(Language.CREATOR);
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.vote_average = in.readDouble();
        this.vote_count = in.readInt();
    }

    public static final Parcelable.Creator<DetailedMovie> CREATOR = new Parcelable.Creator<DetailedMovie>() {
        @Override
        public DetailedMovie createFromParcel(Parcel source) {
            return new DetailedMovie(source);
        }

        @Override
        public DetailedMovie[] newArray(int size) {
            return new DetailedMovie[size];
        }
    };



     /*
       {
            "adult": false,
            "backdrop_path": "/lubzBMQLLmG88CLQ4F3TxZr2Q7N.jpg",
            "belongs_to_collection": {
                "id": 427084,
                "name": "The Secret Life of Pets Collection",
                "poster_path": "/aDNbXvuRiuYxk8qCwXNQQ7UEHau.jpg",
                "backdrop_path": null
            },
            "budget": 75000000,
            "genres": [
                {
                    "id": 12,
                    "name": "Adventure"
                },
                {
                    "id": 16,
                    "name": "Animation"
                },
                {
                    "id": 35,
                    "name": "Comedy"
                },
                {
                    "id": 10751,
                    "name": "Family"
                }
            ],
            "homepage": "http://www.thesecretlifeofpets.com/",
            "id": 328111,
            "imdb_id": "tt2709768",
            "original_language": "en",
            "original_title": "The Secret Life of Pets",
            "overview": "The quiet life of a terrier named Max is upended when his owner takes in Duke, a stray whom Max instantly dislikes.",
            "popularity": 124.271374,
            "poster_path": "/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg",
            "production_companies": [
                {
                    "name": "Universal Pictures",
                    "id": 33
                },
                {
                    "name": "Dentsu",
                    "id": 6452
                },
                {
                    "name": "Illumination Entertainment",
                    "id": 6704
                }
            ],
            "production_countries": [
                {
                    "iso_3166_1": "US",
                    "name": "United States of America"
                }
            ],
            "release_date": "2016-06-18",
            "revenue": 874333497,
            "runtime": 87,
            "spoken_languages": [
                {
                    "iso_639_1": "en",
                    "name": "English"
                }
            ],
            "status": "Released",
            "tagline": "Think this is what they do all day?",
            "title": "The Secret Life of Pets",
            "video": false,
            "vote_average": 5.8,
            "vote_count": 2069
        }
     */
}
