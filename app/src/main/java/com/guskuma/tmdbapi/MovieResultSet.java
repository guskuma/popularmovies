package com.guskuma.tmdbapi;

import java.util.List;

/**
 * Created by juak on 03/02/17.
 */

public class MovieResultSet {

    public int page;
    public List<Movie> results;
    public DateInterval dates;
    public int total_pages;
    public int total_results;

}
