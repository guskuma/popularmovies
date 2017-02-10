package com.guskuma.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guskuma.tmdbapi.DetailedMovie;
import com.guskuma.tmdbapi.Movie;
import com.guskuma.tmdbapi.TMDbService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @BindView(R.id.tv_movie_title) TextView mMovieTitle;
    @BindView(R.id.tv_movie_original_title) TextView mMovieOriginalTitle;
    @BindView(R.id.tv_movie_rating) TextView mMovieRating;
    @BindView(R.id.tv_movie_release_date) TextView mMovieReleaseDate;
    @BindView(R.id.tv_movie_overview) TextView mMovieOverview;
    @BindView(R.id.iv_movie_poster) ImageView mMoviePoster;
    @BindView(R.id.iv_movie_backdrop) ImageView mMovieBackdrop;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.sv_movie_detail) NestedScrollView mMovieDetailNestedScrollView;
    Movie mMovie;
    TMDbService mMovieService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        mMovie = getIntent().getExtras().getParcelable(Movie.EXTRA_NAME);

        mMovieOverview.setText(mMovie.overview);
        mMovieTitle.setText(mMovie.title);
        mMovieOriginalTitle.setText(Html.fromHtml("<b>Original title:</b><br>" + mMovie.original_title));
        mMovieRating.setText(Html.fromHtml("<b>Rating:</b><br>" + mMovie.vote_average + " of 10 (" + mMovie.vote_count + " votes)"));
        mMovieReleaseDate.setText(Html.fromHtml("<b>Release date:</b><br>" + mMovie.release_date));

        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        getMovieImages();
        getMovieDetails();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getMovieDetails() {
        Gson gson = new GsonBuilder().setLenient().create();
        mMovieService = new Retrofit.Builder()
                .baseUrl(TMDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TMDbService.class);

        Call<DetailedMovie> call = mMovieService.getMovieDetail(mMovie.id, "e8a6c51fc482352ed4caed9cb105552f", "en-US");
        call.enqueue(new Callback<DetailedMovie>() {
            @Override
            public void onResponse(Call<DetailedMovie> call, Response<DetailedMovie> response) {
                if(response.isSuccessful()) {
                    DetailedMovie detailedMovie = response.body();

                } else {

                }
            }

            @Override
            public void onFailure(Call<DetailedMovie> call, Throwable t) {

            }
        });
    }

    private void getMovieImages() {
        Uri backdropUri = new Uri.Builder()
                .scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w1280")
                .appendPath(mMovie.backdrop_path.replace("/", ""))
                .build();

        Picasso.with(this).load(backdropUri.toString()).into(mMovieBackdrop, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) mMovieBackdrop.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override
            public void onError() {

            }
        });

        Uri posterUri = new Uri.Builder()
                .scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(mMovie.poster_path.replace("/", ""))
                .build();

        Picasso.with(this).load(posterUri.toString()).into(mMoviePoster);
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        int backgroundLight = getResources().getColor(R.color.colorBackgroundLight);
        mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        mCollapsingToolbarLayout.setBackgroundColor(palette.getDarkMutedColor(primaryDark));
        mMovieTitle.setTextColor(palette.getDarkMutedColor(primaryDark));
        mMovieDetailNestedScrollView.setBackgroundColor(palette.getLightMutedColor(backgroundLight));
        supportStartPostponedEnterTransition();
    }
}
