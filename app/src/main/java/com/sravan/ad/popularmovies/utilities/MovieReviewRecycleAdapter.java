package com.sravan.ad.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sravan.ad.popularmovies.R;
import com.sravan.ad.popularmovies.data.TMDBMovieReview;

import java.util.ArrayList;

/**
 * Created by HP on 3/9/2017.
 */

public class MovieReviewRecycleAdapter extends RecyclerView.Adapter<MovieReviewRecycleAdapter.ViewHolder> {

    Context context;
    public ArrayList<TMDBMovieReview> movieReviewArrayList;
    private ReviewCallback reviewCallback;

    public interface ReviewCallback {
        void open(TMDBMovieReview review);
    }

    public MovieReviewRecycleAdapter(Context context, ArrayList<TMDBMovieReview> movieReviewArrayList,
                                     ReviewCallback reviewCallback) {
        this.context = context;
        this.movieReviewArrayList = movieReviewArrayList;
        this.reviewCallback = reviewCallback;
    }

    @Override
    public MovieReviewRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MovieReviewRecycleAdapter.ViewHolder holder, int position) {
        TMDBMovieReview tmdbMovieReview = movieReviewArrayList.get(position);
        holder.authorTextView.setText(tmdbMovieReview.getAuthor() + "'s review");
        holder.contentTextView.setText(tmdbMovieReview.getContent());
        holder.contentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewCallback.open(movieReviewArrayList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieReviewArrayList.size();
    }

    public ArrayList<TMDBMovieReview> getReviews() {
        return movieReviewArrayList;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView authorTextView;
        public final TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.review_author);
            contentTextView = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
