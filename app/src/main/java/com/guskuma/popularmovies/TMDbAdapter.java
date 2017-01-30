package com.guskuma.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

/**
 * Created by Julio on 28/01/2017.
 */

public class TMDbAdapter extends RecyclerView.Adapter<TMDbAdapter.MovieItemViewHolder> {

    ArrayList<String> mMovies = new ArrayList<>();

    public TMDbAdapter(String[] movies){
        mMovies.addAll(Arrays.asList(movies));
    }

    public void addMovies(String[] moviesToAdd){
        mMovies.addAll(Arrays.asList(moviesToAdd));
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mMoviePosterImageView;
        TextView mMovieTitleTextView;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            mMovieTitleTextView = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }
    }

    @Override
    public int getItemCount() {
        return (mMovies == null) ? 0 : mMovies.size();
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.grid_item_movie, parent, false);
        MovieItemViewHolder viewHolder = new MovieItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {

    }
}
