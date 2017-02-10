package com.guskuma.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guskuma.tmdbapi.Movie;
import com.guskuma.tmdbapi.MovieFilterDescriptor;
import com.guskuma.tmdbapi.MovieResultSet;
import com.guskuma.tmdbapi.TMDbAdapter;
import com.guskuma.tmdbapi.TMDbService;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<MovieResultSet>, TMDbAdapter.MovieItemClickListener {
    String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_movies_list) RecyclerView mMoviesList;
    @BindView(R.id.pb_fetching)    ProgressBar mFetchProgress;
    @BindView(R.id.tv_empty_list)  TextView mEmptyMessage;

    @BindString(R.string.fetch_fail) String toastMessage;

    TMDbService mMovieService;
    TMDbAdapter mTMDbAdapter;
    EndlessRecyclerViewScrollListener mEndlessScrollListener;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Gson gson = new GsonBuilder().setLenient().create();
        mMovieService = new Retrofit.Builder()
                .baseUrl(TMDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TMDbService.class);

        mTMDbAdapter = new TMDbAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNumRows(), LinearLayoutManager.VERTICAL, false);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(false);
        mMoviesList.setAdapter(mTMDbAdapter);

        mEndlessScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                fetchMoviesList(page);
            }
        };

        mMoviesList.addOnScrollListener(mEndlessScrollListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mTMDbAdapter.getMovies().size() == 0)
            fetchMoviesList(1);
    }

    private int calculateNumRows(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 185);
        return noOfColumns;
    }

    @Override
    public void onListItemClick(Movie movie) {
        Intent i = new Intent(this, MovieDetailActivity.class);
        i.putExtra(Movie.EXTRA_NAME, movie);
        startActivity(i);
    }

    public void fetchMoviesList(int pageToLoad){

        Call<MovieResultSet> call = mMovieService.getMoviesList(MovieFilterDescriptor.POPULAR, "e8a6c51fc482352ed4caed9cb105552f", "en-US", pageToLoad);
        call.enqueue(this);
        mFetchProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(Call<MovieResultSet> call, Response<MovieResultSet> response) {
        if(response.isSuccessful()) {
            MovieResultSet movieResultSet = response.body();
            Log.d(TAG, String.format("%s movies fetched (page %s)", movieResultSet.results.size(), movieResultSet.page));
            mTMDbAdapter.addMovies(movieResultSet.page, movieResultSet.results);
            toggleViewsVisibility(true);
        } else {
            toggleViewsVisibility(false);
        }

        mFetchProgress.setVisibility(View.INVISIBLE);

    }

    private void toggleViewsVisibility(boolean success) {
        mEmptyMessage.setVisibility(success ? View.INVISIBLE : View.VISIBLE);
        mMoviesList.setVisibility(success ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<MovieResultSet> call, Throwable t) {
        if(mToast!=null){
            mToast.cancel();
        }

        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        Log.d(TAG, "Movie list fetch failed: " + t.getMessage());
        mToast.show();
        mFetchProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("moviesList", mTMDbAdapter.getMovies());
        outState.putInt("currentPage", mEndlessScrollListener.getCurrentPage());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        List<Movie> movies = savedInstanceState.getParcelableArrayList("moviesList");
        int currentPage = savedInstanceState.getInt("currentPage");
        mTMDbAdapter.addMovies(currentPage, movies);
        mEndlessScrollListener.setCurrentPage(currentPage);
    }
}
