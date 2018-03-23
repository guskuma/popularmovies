package com.guskuma.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Julio Guskuma on 23/03/18.
 */

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popularmovies.db";
    public static final int DATABASE_VERSION = 1;

    public PopularMoviesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + PopularMoviesContract.MovieEntry.TABLE_NAME + " (" +
                PopularMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.MovieEntry.TITLE + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.OVERVIEW + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.RATING + " TEXT NOT NULL, " +
                PopularMoviesContract.MovieEntry.POSTER_IMAGE + " BLOB);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
