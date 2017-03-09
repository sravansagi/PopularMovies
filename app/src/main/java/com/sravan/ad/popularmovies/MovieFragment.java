package com.sravan.ad.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sravan.ad.popularmovies.utilities.FetchMovieTask;
import com.sravan.ad.popularmovies.utilities.MovieRecycleAdapter;
import com.sravan.ad.popularmovies.data.TMDBMovie;

import java.util.ArrayList;

/**
 * Created by Sravan on 1/3/2017.
 */

public class MovieFragment extends Fragment implements MovieRecycleAdapter.Callbacks{


    //private MovieAdapter movieAdapter;
    MovieRecycleAdapter movieRecycleAdapter;
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();
    private String sortPreference = "";

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        movieRecycleAdapter = new MovieRecycleAdapter(getContext(),new ArrayList<TMDBMovie>(), this);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview_moviefragment);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieRecycleAdapter);
        return rootView;
    }

    /**
     * OnResume method is called when fragment is visible to the user and is actively running.
     * OnResume method is used to update the GridView by calling UpdateMovieGrid method
     * During the first callback, since the movieAdapter contains no items(The movie adapter is set to
     * empty ArrayList in the onCreateView method) UpdateMovieGrid method will be called. When onResume is called
     * after coming back from setting activity then the selected sharepreference is compared with the sort order
     * which was used when the grid view is currently populated. If there is a difference in selected sort order
     * in the settingsactivity and the populated gridview sort order then only the UpdatMovieGrid is called. If both
     * values are same, it is not required to update the GridView as it is already updated
     */
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preferencesString = preference.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_popularity));
        if(movieRecycleAdapter.getItemCount() == 0 || preferencesString.equalsIgnoreCase(getString(R.string.pref_sortby_favourites))){
            updateMovieGrid();
        }else{
            if (!preferencesString.equalsIgnoreCase(sortPreference)){
                updateMovieGrid();
            }
        }
    }

    /**
     * onStart()method is called when the fragment is visible to the user
     * When the control comes back from the Settings activity onStart callback method
     * is called.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * updateMovieGrid method creates an object for async task for fetching the movies and executes
     * the task by getting the sort preference selected by the user. FetchMovieTask fetches movies with
     * vote count more than 1000. If we call updateMovieGrid method in the onCreateView after adapter is
     * set then the loading time is reduced.
     */
    private void updateMovieGrid() {
        FetchMovieTask movieTask = new FetchMovieTask(movieRecycleAdapter, getContext());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPreference = preferences.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_popularity));
        movieTask.execute(sortPreference);
    }

    @Override
    public void open(TMDBMovie movie) {
        Intent intent = new Intent(getActivity(),DetailActivity.class)
                .putExtra(Intent.EXTRA_TEXT,movie);
        startActivity(intent);
    }
}
