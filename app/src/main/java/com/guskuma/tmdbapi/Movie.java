package com.guskuma.tmdbapi;

/**
 * Created by juak on 03/02/17.
 */

public class Movie {

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


}
