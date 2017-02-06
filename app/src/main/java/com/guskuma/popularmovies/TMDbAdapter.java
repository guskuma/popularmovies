package com.guskuma.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guskuma.tmdbapi.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Julio on 28/01/2017.
 */

public class TMDbAdapter extends RecyclerView.Adapter<TMDbAdapter.MovieItemViewHolder> {

    String TAG = TMDbAdapter.class.getSimpleName();

    private int mCurrentPage;
    private MovieItemClickListener mClickListener;
    ArrayList<Movie> mMovies = new ArrayList<>();

    public TMDbAdapter(MovieItemClickListener listener){
        mClickListener = listener;
    }

    public void addMovies(int pageNumber, List<Movie> moviesToAdd){
        if(pageNumber > mCurrentPage) {
            mCurrentPage = pageNumber;
            mMovies.addAll(moviesToAdd);
            notifyDataSetChanged();
            Log.d(TAG, String.format("%s movies addes (page %s)", moviesToAdd.size(), pageNumber));
        }
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_movie_poster) ImageView mMoviePosterImageView;
        @BindView(R.id.tv_movie_title)TextView mMovieTitleTextView;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickListener.onListItemClick(clickedPosition);
        }

        public void bind(Movie movie){
            Uri imageUri = new Uri.Builder().scheme("http").path("image.tmdb.org/t/p/").appendPath("w185").appendPath(movie.poster_path.replace("/", "")).build();
            mMovieTitleTextView.setText(movie.title);
            Picasso.with(itemView.getContext()).load(imageUri.toString()).into(mMoviePosterImageView);
        }

    }

    public interface MovieItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public int getItemCount() {
        return (mMovies == null) ? 0 : mMovies.size();
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.grid_item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieItemViewHolder viewHolder = new MovieItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        holder.bind(mMovies.get(position));
    }

    public int getmCurrentPage() {
        return mCurrentPage;
    }
}
