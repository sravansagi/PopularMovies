package com.sravan.ad.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.sravan.ad.popularmovies.utilities.FetchMovieTask;
import com.sravan.ad.popularmovies.utilities.MovieAdapter;
import com.sravan.ad.popularmovies.utilities.MovieRecycleAdapter;
import com.sravan.ad.popularmovies.utilities.TMDBMovie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sravan on 1/3/2017.
 */

public class MovieFragment extends Fragment implements MovieRecycleAdapter.Callbacks{


    //private MovieAdapter movieAdapter;
    MovieRecycleAdapter movieRecycleAdapter;
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();
    private String sortPreference;

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
        if(movieRecycleAdapter.getItemCount() == 0){
            updateMovieGrid();
        }else{
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String preferencesString = preferences.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_popularity));
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
     * vote count more than 1000.
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
