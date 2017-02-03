package com.guskuma.tmdbapi;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by juak on 03/02/17.
 */

public class MovieFilterDescriptor {
    public static final String LATEST = "latest";
    public static final String NOW_PLAYING = "now_playing";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    public final String movieFilter;

    @Retention(RetentionPolicy.SOURCE)

    // Enumerate valid values for this interface
    @StringDef({ LATEST, NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING })

    // Create an interface for validating int types
    public @interface MovieFilterDef { }

    // Mark the argument as restricted to these enumerated types
    public MovieFilterDescriptor(@MovieFilterDef String movieFilter) {
        this.movieFilter = movieFilter;
    }
}
