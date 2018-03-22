package com.guskuma.tmdbapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Julio on 30/01/2017.
 */

public interface TMDbService {

    public static final String BASE_URL = "http://api.themoviedb.org/";

    //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
    @GET("3/movie/{category}")
    Call<MovieResultSet> getMoviesList(@Path("category") @MovieFilterDescriptor.MovieFilterDef String category, @Query("api_key") String apiKey, @Query("language")String language, @Query("page") int page);

    //https://api.themoviedb.org/3/movie/328111?api_key=<<api_key>>&language=en-U
    @GET("3/movie/{movie_id}")
    Call<DetailedMovie> getMovieDetail(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("language")String language);

    //https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>&language=en-US
    @GET("3/movie/{movie_id}/videos")
    Call<VideoResultSet> getMovieVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("language")String language);

    //https://api.themoviedb.org/3/movie/{movie_id}/reviews?api_key=<<api_key>>&language=en-US&page=1
    @GET("3/movie/{movie_id}/reviews")
    Call<ReviewResultSet> getMovieReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("language")String language, @Query("page") int page);

}