package com.guskuma.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guskuma.tmdbapi.MovieFilterDescriptor;
import com.guskuma.tmdbapi.MovieResultSet;
import com.guskuma.tmdbapi.TMDbService;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<MovieResultSet>, TMDbAdapter.MovieItemClickListener {

    public static final String BASE_URL = "http://api.themoviedb.org/";
    String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_movies_list) RecyclerView mMoviesList;
    @BindView(R.id.pb_fetching)    ProgressBar mFetchProgress;
    @BindView(R.id.tv_empty_list)  TextView mEmptyMessage;

    @BindString(R.string.fetch_fail) String toastMessage;

    int mLastPageLoaded = 0;
    TMDbService mMovieService;
    TMDbAdapter mTMDbAdapter;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Gson gson = new GsonBuilder().setLenient().create();
        mMovieService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TMDbService.class);

        fetchMoviesList(1);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(false);
        mMoviesList.setAdapter(mTMDbAdapter);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    public void fetchMoviesList(int pageToLoad){
        if(mTMDbAdapter == null){
            mTMDbAdapter = new TMDbAdapter(this);
        }

        Call<MovieResultSet> call = mMovieService.getMoviesList(MovieFilterDescriptor.POPULAR, "e8a6c51fc482352ed4caed9cb105552f", "en-US", pageToLoad);
        call.enqueue(this);
        mFetchProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(Call<MovieResultSet> call, Response<MovieResultSet> response) {
        if(response.isSuccessful()) {
            MovieResultSet movieResultSet = response.body();
            mLastPageLoaded = movieResultSet.page;
            mTMDbAdapter.addMovies(mLastPageLoaded, movieResultSet.results);

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
}
