package com.guskuma.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guskuma.tmdbapi.Movie;
import com.guskuma.tmdbapi.Review;
import com.guskuma.tmdbapi.ReviewResultSet;
import com.guskuma.tmdbapi.TMDbService;
import com.guskuma.tmdbapi.Video;
import com.guskuma.tmdbapi.VideoResultSet;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @BindString(R.string.tmdb_api_key) String TMDB_API_KEY;
    @BindString(R.string.youtube_api_key) String YOUTUBE_API_KEY;
    private static final int RECOVERY_DIALOG_REQUEST = 16156;

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
    @BindView(R.id.tv_trailers) TextView mTrailerLabel;
    @BindView(R.id.ll_trailers) LinearLayout mTrailersList;
    @BindView(R.id.tv_reviews) TextView mReviewLabel;
    @BindView(R.id.ll_reviews) LinearLayout mReviewsList;
    @BindView(R.id.iv_favorite) ImageView mFavorite;

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
        mMovieOriginalTitle.setText(mMovie.original_title);
        mMovieRating.setText(mMovie.vote_average + " of 10 (" + mMovie.vote_count + " votes)");
        mMovieReleaseDate.setText(mMovie.release_date);

        if(isMovieFavorited()){
            mFavorite.setBackground(getResources().getDrawable(R.drawable.ic_star_24dp));
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent),
                    PorterDuff.Mode.SRC_ATOP);
            mFavorite.setColorFilter(porterDuffColorFilter);
        }

        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Gson gson = new GsonBuilder().setLenient().create();
        mMovieService = new Retrofit.Builder()
                .baseUrl(TMDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TMDbService.class);

        getMovieImages();
        getMovieVideosThumbnails();
        getMovieReviews();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean isMovieFavorited(){
        return true;
    }

    private void getMovieVideosThumbnails(){
        Call<VideoResultSet> call = mMovieService.getMovieVideos(mMovie.id, TMDB_API_KEY, "en-US");
        call.enqueue(new Callback<VideoResultSet>() {

            int mTrailerCount = 0;

            @Override
            public void onResponse(Call<VideoResultSet> call, Response<VideoResultSet> response) {
                Iterator<Video> it = response.body().results.iterator();

                while(it.hasNext() && mTrailerCount < 5){
                    Video v = it.next();
                    if(v.site.equals("YouTube") && v.type.equals("Trailer")) {

                        int layoutIdForListItem = R.layout.item_movie_trailer;
                        LayoutInflater inflater = LayoutInflater.from(MovieDetailActivity.this);
                        boolean shouldAttachToParentImmediately = true;

                        View view = inflater.inflate(layoutIdForListItem, mTrailersList, false);
                        ImageView thumbnail = (ImageView)view.findViewById(R.id.iv_movie_trailer_thumbnail);
                        thumbnail.setContentDescription(v.name);
                        if(mTrailerCount > 0) {
                            int px = getPXfromDP();
                            Space space = getSpace(px, ViewGroup.LayoutParams.MATCH_PARENT);
                            mTrailersList.addView(space);
                        }

                        Uri thumbnailUri = new Uri.Builder()
                                .scheme("http")
                                .authority("img.youtube.com")
                                .appendPath("vi")
                                .appendPath(v.key)
                                .appendPath("0.jpg")
                                .build();

                        Picasso.with(MovieDetailActivity.this).load(thumbnailUri.toString()).into(thumbnail, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {}

                            @Override
                            public void onError() {}
                        });

                        ImageView youtubePlayImage = (ImageView)view.findViewById(R.id.iv_youtube_play);
                        youtubePlayImage.setTag(v.key);
                        youtubePlayImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+v.getTag().toString())));
                            }
                        });

                        mTrailersList.addView(view);
                        mTrailerCount++;
                    }

                    if(mTrailerCount > 0){
                        mTrailerLabel.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<VideoResultSet> call, Throwable t) { }

        });
    }

    private int getPXfromDP() {
        float dp = getResources().getDimension(R.dimen.between_text_vertical);
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics() );
    }

    @NonNull
    private Space getSpace(int width, int height) {
        Space space = new Space(MovieDetailActivity.this);
        space.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        return space;
    }

    private void getMovieReviews(){
        Call<ReviewResultSet> call = mMovieService.getMovieReviews(mMovie.id, TMDB_API_KEY, "en-US", 1);
        call.enqueue(new Callback<ReviewResultSet>() {

            int reviewsCount = 0;

            @Override
            public void onResponse(Call<ReviewResultSet> call, Response<ReviewResultSet> response) {
                Iterator<Review> it = response.body().results.iterator();
                while(it.hasNext() && reviewsCount < 3){
                    Review v = it.next();

                    int layoutIdForListItem = R.layout.item_movie_review;
                    LayoutInflater inflater = LayoutInflater.from(MovieDetailActivity.this);
                    View view = inflater.inflate(layoutIdForListItem, mReviewsList, false);

                    TextView tvReviewContent = (TextView)view.findViewById(R.id.tv_review_content);
                    tvReviewContent.setText("\"" + v.content.replace("\r\n", "") + "\"");

                    TextView tvReviewAuthor = (TextView)view.findViewById(R.id.tv_review_author);
                    tvReviewAuthor.setText("-" + v.author);

                    if(reviewsCount > 0) {
                        int px = getPXfromDP();
                        Space space = getSpace(ViewGroup.LayoutParams.MATCH_PARENT, px);
                        mReviewsList.addView(space);
                    }

                    mReviewsList.addView(view);
                    reviewsCount++;
                }

                if(response.body().total_results > 0){
                    mReviewLabel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ReviewResultSet> call, Throwable t) {}
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
