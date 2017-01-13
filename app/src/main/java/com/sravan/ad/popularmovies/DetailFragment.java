package com.sravan.ad.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

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
        if (intent!= null &&
            intent.getStringExtra("MOVIE_TITLE").length() > 0){
            TextView title = (TextView) rootView.findViewById(R.id.detail_movie_title);
            title.setText(intent.getStringExtra("MOVIE_TITLE"));
            ImageView poster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
            Picasso.with(getContext()).load(intent.getStringExtra("MOVIE_POSTERPATH")).into(poster);
            TextView overview = (TextView) rootView.findViewById(R.id.details_movie_overview);
            overview.setText(intent.getStringExtra("MOVIE_OVERVIEW"));
            TextView movieRating = (TextView) rootView.findViewById(R.id.detail_movie_rating);
            movieRating.setText(intent.getStringExtra("MOVIE_VOTE") +"/10");
            TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.detail_movie_release_date);
            movieReleaseDate.setText(intent.getStringExtra("MOVIE_RELEASEDATE"));
        }
        return rootView;
    }

}
