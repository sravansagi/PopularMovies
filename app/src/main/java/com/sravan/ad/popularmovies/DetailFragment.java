package com.sravan.ad.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sravan.ad.popularmovies.utilities.TMDBMovie;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    @BindView(R.id.detail_movie_title) TextView title;
    @BindView(R.id.detail_movie_poster) ImageView poster;
    @BindView(R.id.details_movie_overview) TextView overview;
    @BindView(R.id.detail_movie_rating)  TextView movieRating;
    @BindView(R.id.detail_movie_release_date) TextView movieReleaseDate;

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public DetailFragment() {
    }

    /**
     * This call back method is used to check if intent is passes to this fragment
     * and display the movie details based on that.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);
        Intent intent = getActivity().getIntent();
        ButterKnife.bind(this,rootView);
        if (intent!= null){
            TMDBMovie movie= (TMDBMovie) intent.getParcelableExtra(Intent.EXTRA_TEXT);
            if (movie == null){
                Toast.makeText(getContext(),"Unable to display the movie details", Toast.LENGTH_SHORT).show();
            }
            else{
                title.setText(movie.getOriginalTitle());
                Picasso.with(getContext()).load(movie.getPosterPath()).into(poster);
                overview.setText(movie.getOverview());
                movieRating.setText(movie.getVoteAverage() +"/10");
                movieReleaseDate.setText(movie.getReleaseDate());
            }
        }
        else{
            Toast.makeText(getContext(),"Unable to display the movie details", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

}
