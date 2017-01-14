package com.sravan.ad.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        if (intent!= null &&
            intent.getStringExtra("MOVIE_TITLE").length() > 0){
            title.setText(intent.getStringExtra("MOVIE_TITLE"));
            Picasso.with(getContext()).load(intent.getStringExtra("MOVIE_POSTERPATH")).into(poster);
            overview.setText(intent.getStringExtra("MOVIE_OVERVIEW"));
            movieRating.setText(intent.getStringExtra("MOVIE_VOTE") +"/10");
            movieReleaseDate.setText(intent.getStringExtra("MOVIE_RELEASEDATE"));
        }
        return rootView;
    }

}
