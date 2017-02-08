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
    public String backdropPath;
    public MovieCollection belongsToCollection;
    public int budget;
    public List<Genre> genres = null;
    public String homepage;
    public int id;
    public String imdbId;
    public String originalLanguage;
    public String originalTitle;
    public String overview;
    public double popularity;
    public String posterPath;
    public List<Producer> productionCompanies = null;
    public List<Country> productionCountries = null;
    public String releaseDate;
    public int revenue;
    public int runtime;
    public List<Language> spokenLanguages = null;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    public double voteAverage;
    public int voteCount;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdropPath);
        dest.writeParcelable(this.belongsToCollection, flags);
        dest.writeInt(this.budget);
        dest.writeTypedList(this.genres);
        dest.writeString(this.homepage);
        dest.writeInt(this.id);
        dest.writeString(this.imdbId);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeTypedList(this.productionCompanies);
        dest.writeTypedList(this.productionCountries);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.revenue);
        dest.writeInt(this.runtime);
        dest.writeTypedList(this.spokenLanguages);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.voteAverage);
        dest.writeInt(this.voteCount);
    }

    public DetailedMovie() {
    }

    protected DetailedMovie(Parcel in) {
        this.adult = in.readByte() != 0;
        this.backdropPath = in.readString();
        this.belongsToCollection = in.readParcelable(MovieCollection.class.getClassLoader());
        this.budget = in.readInt();
        this.genres = in.createTypedArrayList(Genre.CREATOR);
        this.homepage = in.readString();
        this.id = in.readInt();
        this.imdbId = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.posterPath = in.readString();
        this.productionCompanies = in.createTypedArrayList(Producer.CREATOR);
        this.productionCountries = in.createTypedArrayList(Country.CREATOR);
        this.releaseDate = in.readString();
        this.revenue = in.readInt();
        this.runtime = in.readInt();
        this.spokenLanguages = in.createTypedArrayList(Language.CREATOR);
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readDouble();
        this.voteCount = in.readInt();
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
