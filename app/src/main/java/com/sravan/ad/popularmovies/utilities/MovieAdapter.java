package com.sravan.ad.popularmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sravan.ad.popularmovies.R;
import java.util.ArrayList;

/**
 * Created by HP on 1/8/2017.
 */

public class MovieAdapter extends ArrayAdapter<TMDBMovie> {

    public final static String LOG_CAT = MovieAdapter.class.getSimpleName();

    Context context;
    ArrayList<TMDBMovie> movieList ;

    public MovieAdapter(Context context, ArrayList<TMDBMovie> movieList) {
        super(context,0,movieList);
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.grid_item_movie,parent,false);
            /*rootView = layoutInflater.inflate(R.layout.grid_item_movie,parent,false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.grid_item_movie_imageview);
            Picasso.with(context).load(movieList.get(position).getPosterPath()).into(imageView);*/
        }
        Picasso.with(context).load(movieList.get(position).getPosterPath()).into((ImageView) convertView);
        return convertView;
    }
}
