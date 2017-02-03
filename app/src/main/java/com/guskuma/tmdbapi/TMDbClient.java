package com.guskuma.tmdbapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Julio on 30/01/2017.
 */

public interface TMDbClient {

    //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
    @GET("3/movie/{category}")
    Call<MovieResultSet> getPopularMovies(@Path("category") @MovieFilterDescriptor.MovieFilterDef String category, @Query("apiKey") String apiKey, @Query("language")String language, @Query("page") int page);

}
