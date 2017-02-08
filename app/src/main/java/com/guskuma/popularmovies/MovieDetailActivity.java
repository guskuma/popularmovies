package com.guskuma.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guskuma.tmdbapi.DetailedMovie;
import com.guskuma.tmdbapi.Movie;
import com.guskuma.tmdbapi.MovieResultSet;
import com.guskuma.tmdbapi.TMDbService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity implements Callback<DetailedMovie> {

    @BindView(R.id.tv_movie_overview) TextView mMovieOverview;
    @BindView(R.id.iv_movie_backdrop) ImageView mMovieBackdrop;
    TMDbService mMovieService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        Gson gson = new GsonBuilder().setLenient().create();
        mMovieService = new Retrofit.Builder()
                .baseUrl(TMDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TMDbService.class);

        Movie movie = getIntent().getExtras().getParcelable(Movie.EXTRA_NAME);

        Call<DetailedMovie> call = mMovieService.getMovieDetail(movie.id, "e8a6c51fc482352ed4caed9cb105552f", "en-US");
        call.enqueue(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
        if(response.isSuccessful()) {
            DetailedMovie detailedMovie = response.body();
            mMovieOverview.setText(detailedMovie.overview);
            this.setTitle(detailedMovie.title);

            Uri imageUri = new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w300")
                    .appendPath(detailedMovie.backdropPath.replace("/", ""))
                    .build();

            Picasso.with(this).load(imageUri.toString()).into(mMovieBackdrop);

        } else {

        }


    }

    @Override
    public void onFailure(Call<DetailedMovie> call, Throwable t) {

    }
}
