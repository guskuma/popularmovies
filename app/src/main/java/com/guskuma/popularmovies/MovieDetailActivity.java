package com.guskuma.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
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

    @BindView(R.id.tv_movie_title) TextView mMovieTitle;
    @BindView(R.id.tv_movie_overview) TextView mMovieOverview;
    @BindView(R.id.iv_movie_backdrop) ImageView mMovieBackdrop;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
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

        mCollapsingToolbarLayout.setTitle(mMovie.title);
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -mCollapsingToolbarLayout.getHeight() + mToolbar.getHeight()) {
                    mMovieTitle.setVisibility(View.GONE);
                } else {
                    mMovieTitle.setVisibility(View.VISIBLE);
                }
            }
        });

        getMovieBackdrop();
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

    private void getMovieBackdrop() {
        Uri imageUri = new Uri.Builder()
                .scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w1280")
                .appendPath(mMovie.backdrop_path.replace("/", ""))
                .build();

        Picasso.with(this).load(imageUri.toString()).into(mMovieBackdrop, new com.squareup.picasso.Callback() {
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
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        mCollapsingToolbarLayout.setBackgroundColor(palette.getDarkMutedColor(primaryDark));
        supportStartPostponedEnterTransition();
    }
}
