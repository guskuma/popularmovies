package com.guskuma.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guskuma.data.PopularMoviesContract;
import com.guskuma.tmdbapi.Movie;
import com.guskuma.tmdbapi.MovieFilterDescriptor;
import com.guskuma.tmdbapi.MovieResultSet;
import com.guskuma.tmdbapi.TMDbAdapter;
import com.guskuma.tmdbapi.TMDbService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<MovieResultSet>,
        TMDbAdapter.MovieItemClickListener, LoaderManager.LoaderCallbacks<MovieResultSet> {

    String TAG = MainActivity.class.getSimpleName();

    @BindString(R.string.tmdb_api_key) String TMDB_API_KEY;
    @BindView(R.id.rv_movies_list) RecyclerView mMoviesList;
    @BindView(R.id.pb_fetching)    ProgressBar mFetchProgress;
    @BindView(R.id.no_connection_view)  View mEmptyScreen;
    @BindView(R.id.bt_try_again) Button mTryAgainButton;
    @BindView(R.id.tv_empty_list) TextView mEmptyList;
    @BindString(R.string.fetch_fail) String toastMessage;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_menu) LinearLayout mDrawerMenu;
    @BindView(R.id.drawer_list) ListView mDrawerList;
    @BindArray(R.array.navigation_drawer_list)  String[] mDrawerOptions;
    @BindArray(R.array.navigation_drawer_list_values)  String[] mDrawerOptionsValue;
    @BindView(R.id.my_toolbar) Toolbar mToolbar;

    private static final int FAVORITE_MOVIES_LOADER = 22;
    int mImageWidth = 185;

    String mMovieCategory;
    ActionBarDrawerToggle mDrawerToggle;
    TMDbService mMovieService;
    TMDbAdapter mTMDbAdapter;
    EndlessRecyclerViewScrollListener mEndlessScrollListener;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_menu_item, mDrawerOptions));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerMenu);
                changeMovieCategory(mDrawerOptionsValue[position], true);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_closed  /* "close drawer" description */
        ) { };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        Gson gson = new GsonBuilder().setLenient().create();
        mMovieService = new Retrofit.Builder()
                .baseUrl(TMDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TMDbService.class);

        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNumItensInRows(0), LinearLayoutManager.VERTICAL, false);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(false);

        mTMDbAdapter = new TMDbAdapter(this, mImageWidth);
        mMoviesList.setAdapter(mTMDbAdapter);

        mEndlessScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                fetchMoviesList(page);
            }
        };

        mMoviesList.addOnScrollListener(mEndlessScrollListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mMovieCategory = MovieFilterDescriptor.POPULAR;

    }

    private void changeMovieCategory(@NonNull String newCategory, boolean reset) {

        getSupportActionBar().setTitle(MovieFilterDescriptor.getDescription(newCategory));

        if(mMovieCategory == null || !mMovieCategory.equals(newCategory)) {
            mMovieCategory = newCategory;

            if(reset) {
                mTMDbAdapter.reset();
                resetScrollListener();
                fetchMoviesList(1);
            }
        }
    }

    private void resetScrollListener() {
        mEndlessScrollListener.resetState();
        mMoviesList.scrollToPosition(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getResources().getString(R.string.favorite_item_value).equals(mMovieCategory)){
            mTMDbAdapter.reset();
            fetchMoviesList(1);
        } else {
            // Fetches only if there´s no movies loaded
            if(mTMDbAdapter.getMovies().size() == 0) {
                changeMovieCategory(mMovieCategory, true);
                fetchMoviesList(1);
            } else {
                changeMovieCategory(mMovieCategory, false);
            }
        }
    }

    private int calculateNumItensInRows(int border){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / (mImageWidth + border));

        if(noOfColumns <= 1){
            mImageWidth = mImageWidth - 10;
            return calculateNumItensInRows(20);
        }

        return noOfColumns;
    }

    @Override
    public void onListItemClick(Movie movie) {
        Log.d(TAG, String.format("Movie clicked: %s", movie.toString()));
        Intent i = new Intent(this, MovieDetailActivity.class);
        i.putExtra(Movie.EXTRA_NAME, movie);
        startActivity(i);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void fetchMoviesList(int pageToLoad){
        if(getResources().getString(R.string.favorite_item_value).equals(mMovieCategory)){
            if(pageToLoad == 1) {
                Loader<MovieResultSet> favoriteMoviesLoader = getSupportLoaderManager().getLoader(FAVORITE_MOVIES_LOADER);
                if (favoriteMoviesLoader == null) {
                    getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this).forceLoad();
                } else {
                    getSupportLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER, null, this).forceLoad();
                }
            }
        } else {
            if (isOnline()) {
                Call<MovieResultSet> call = mMovieService.getMoviesList(MovieFilterDescriptor.valueOf(mMovieCategory), TMDB_API_KEY, "en-US", pageToLoad);
                call.enqueue(this);
                mFetchProgress.setVisibility(View.VISIBLE);
            } else {
                toggleViewsVisibility(false, false);
            }
        }
    }

    @Override
    public void onResponse(Call<MovieResultSet> call, Response<MovieResultSet> response) {
        if(!response.isSuccessful()) {
            toggleViewsVisibility(false, true);
            return;
        }

        if(response.body().results != null && response.body().results.size() > 0) {
            MovieResultSet movieResultSet = response.body();
            Log.d(TAG, String.format("%s movies fetched (page %s)", movieResultSet.results.size(), movieResultSet.page));
            mTMDbAdapter.addMovies(movieResultSet.page, movieResultSet.results);
            toggleViewsVisibility(true, false);
        } else {
            toggleViewsVisibility(true, true);
        }
        mFetchProgress.setVisibility(View.INVISIBLE);
    }

    private void toggleViewsVisibility(boolean success, boolean empty) {
        mEmptyScreen.setVisibility(success && !empty ? View.INVISIBLE : View.VISIBLE);
        mMoviesList.setVisibility(success ? View.VISIBLE : View.INVISIBLE);

        if(success) {
            if(empty) {
                mTryAgainButton.setVisibility(View.GONE);
                mEmptyList.setText(R.string.empty_list);
            }
        } else {
            mTryAgainButton.setVisibility(View.VISIBLE);
            mEmptyList.setText(R.string.empty_list_no_connection);
        }
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
        outState.putString("movieCategory", mMovieCategory);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        List<Movie> movies = savedInstanceState.getParcelableArrayList("moviesList");
        int currentPage = savedInstanceState.getInt("currentPage");
        String movieCategory = savedInstanceState.getString("movieCategory", MovieFilterDescriptor.POPULAR);
        mTMDbAdapter.addMovies(currentPage, movies);
        mEndlessScrollListener.setCurrentPage(currentPage);
        mMovieCategory = movieCategory;
    }

    @OnClick(R.id.bt_try_again)
    public void tryAgain(Button button){
        fetchMoviesList(1);
    }

    private static class FavoriteLoader extends AsyncTaskLoader<MovieResultSet> {

        String TAG = FavoriteLoader.class.getSimpleName();

        public FavoriteLoader(Context context){
            super(context);
        }

        @Override
        public MovieResultSet loadInBackground() {
            MovieResultSet movieResultSet = new MovieResultSet();
            movieResultSet.results = new ArrayList<>();
            try {
                Cursor cursor = getContext().getContentResolver().query(PopularMoviesContract.MovieEntry.CONTENT_URI,
                        null, null, null,
                        PopularMoviesContract.MovieEntry.DATE_ADDED + " DESC");
                while(cursor.moveToNext()){
                    String id = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MovieEntry.TMDB_ID));
                    String title = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MovieEntry.TITLE));
                    String overview = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MovieEntry.OVERVIEW));
                    String releaseDate = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MovieEntry.RELEASE_DATE));
                    String backdropImage = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MovieEntry.BACKDROP_PATH));
                    String posterImage = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MovieEntry.POSTER_PATH));
                    String rating = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.MovieEntry.RATING));
                    Movie movie = new Movie(id, title, overview, releaseDate, backdropImage, posterImage, rating);
                    movieResultSet.results.add(movie);
                }
            }catch (Exception e){
                Log.w(TAG, "Failed to asynchronously fetch data from ContentProvider");
            }

            movieResultSet.total_pages = 1;
            movieResultSet.total_results = movieResultSet.results.size();
            movieResultSet.page = 1;

            return movieResultSet;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
        }
    }

    @Override
    public Loader<MovieResultSet> onCreateLoader(int id, Bundle args) {
        mFetchProgress.setVisibility(View.VISIBLE);
        return new FavoriteLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<MovieResultSet> loader, MovieResultSet movieResultSet) {
        if(movieResultSet.results != null && movieResultSet.results.size() > 0) {
            Log.d(TAG, String.format("%s movies fetched (page %s)", movieResultSet.results.size(), movieResultSet.page));
            mTMDbAdapter.addMovies(movieResultSet.page, movieResultSet.results);
            toggleViewsVisibility(true, false);
        } else {
            toggleViewsVisibility(true, true);
        }
        mFetchProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<MovieResultSet> loader) {
// Oba!
    }
}
