package com.sravan.ad.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sravan.ad.popularmovies.R;
import com.sravan.ad.popularmovies.data.TMDBMovie;

import java.util.ArrayList;

/**
 * Created by HP on 3/8/2017.
 */


/**
 * MovieRecycleAdapter is a Recycler adapter for the TMDB movies. The Recycler adapter has many advantages
 * when compared to the grid or the list view. In terms of performance if we use View Holders in the list view
 * the performance is comparable to the recycler views. The challenge with recycler view there are not methods
 * for adding and clearing the contents. So this is handled by updateMovies method which removes the current set of
 * movies and adds the list of movies fetched. It also notifies the data change by calling notifyDataSetChanged.
 *
 * One more challenge with the recycler view is the onclick listener implementation. The on click listener is implemented
 * using a callback interfact. The same call back interfaces are used to fragment to fragment communications with in the app
 * The step for writing a callback interface:
 * 1) Create a call back interface in side the adapter with the required method
 * 2) Call the method in the adapter with required parameters
 * 3) Implement the method in the activity/fragment with the required implementation
 */
public class MovieRecycleAdapter extends RecyclerView.Adapter<MovieRecycleAdapter.ViewHolder> {

    Context context;
    private ArrayList<TMDBMovie> movieList ;
    private Callbacks mCallbacks;


    public MovieRecycleAdapter(Context context, ArrayList<TMDBMovie> movieList, Callbacks callbacks) {
        this.context = context;
        this.movieList = movieList;
        this.mCallbacks = callbacks;
    }

    public interface Callbacks {
        void open(TMDBMovie movie);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Picasso.with(context)
                .load(movieList.get(position).getPosterPath())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.error_image)
                .into((ImageView) holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.open(movieList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /*public TMDBMovie getMovieAtPosition(int position){
        if(position > movieList.size()){
            return movieList.get(position);
        }
        else{
            return null;
        }
    }*/

    public void updateMovies(ArrayList<TMDBMovie> movies){
        movieList.clear();
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.grid_item_movie_imageview);
        }
    }

}
