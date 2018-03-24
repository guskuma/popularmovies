package com.guskuma.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Julio Guskuma on 23/03/18.
 */

public class PopularMoviesContract {

    public static final String AUTHORITY = "com.guskuma.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";

        public static final String TMDB_ID = "tmdb_id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String POSTER_PATH = "poster_path";
        public static final String RATING = "rating";
        public static final String DATE_ADDED = "date_added";
    }

}
