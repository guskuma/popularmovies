package com.guskuma.popularmovies;

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
            Log.d(TAG, String.format("%s movies addes (page %s)", moviesToAdd.size(), pageNumber));
        }
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMoviePosterImageView;
        TextView mMovieTitleTextView;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            mMovieTitleTextView = (TextView)itemView.findViewById(R.id.tv_movie_title) ;
            mMoviePosterImageView =  (ImageView) itemView.findViewById(R.id.iv_movie_poster) ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickListener.onListItemClick(clickedPosition);
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.grid_item_movie, parent, false);
        MovieItemViewHolder viewHolder = new MovieItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Uri imageUri = new Uri.Builder().scheme("http").path("image.tmdb.org/t/p/").appendPath("w154").appendPath(movie.poster_path).build();

        holder.mMovieTitleTextView.setText(movie.title);
        Picasso.with(holder.itemView.getContext()).load(imageUri.toString()).into(holder.mMoviePosterImageView);
    }

    public int getmCurrentPage() {
        return mCurrentPage;
    }
}
