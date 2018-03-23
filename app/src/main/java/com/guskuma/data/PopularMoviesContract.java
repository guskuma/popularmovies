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

        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String POSTER_IMAGE = "poster_image";
        public static final String RATING = "rating";
    }

}
