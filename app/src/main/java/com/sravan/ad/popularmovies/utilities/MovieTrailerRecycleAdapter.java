package com.sravan.ad.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sravan.ad.popularmovies.R;
import com.sravan.ad.popularmovies.data.TMDBMovieTrailer;

import java.util.ArrayList;

/**
 * Created by HP on 3/9/2017.
 */

public class MovieTrailerRecycleAdapter extends RecyclerView.Adapter<MovieTrailerRecycleAdapter.ViewHolder> {

    Context context;
    public ArrayList<TMDBMovieTrailer> movieTrailers;
    private TrailorCallback trailorCallback;

    public interface TrailorCallback {
        void open(TMDBMovieTrailer trailer);
    }


    public MovieTrailerRecycleAdapter(Context context, ArrayList<TMDBMovieTrailer> movieTrailers, TrailorCallback trailorCallback) {
        this.context = context;
        this.movieTrailers = movieTrailers;
        this.trailorCallback = trailorCallback;
    }

    @Override
    public MovieTrailerRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MovieTrailerRecycleAdapter.ViewHolder holder, int position) {
        TMDBMovieTrailer tmdbMovieTrailer = movieTrailers.get(position);
        String youTubelink = String.format(context.getString(R.string.youtube_trailer_thumbnail_url), tmdbMovieTrailer.getKey());
        Picasso.with(context)
                .load(youTubelink)
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.error_image)
                .into((ImageView) holder.trailerImage);
        holder.trailerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trailorCallback.open(movieTrailers.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieTrailers.size();
    }

    public ArrayList<TMDBMovieTrailer> getTrailers() {
        return movieTrailers;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView trailerImage;
        public ViewHolder(View itemView) {
            super(itemView);
            trailerImage = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
        }
    }

}
