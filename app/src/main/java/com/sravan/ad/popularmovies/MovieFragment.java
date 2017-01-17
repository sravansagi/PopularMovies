package com.sravan.ad.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sravan.ad.popularmovies.utilities.FetchSingleton;
import com.sravan.ad.popularmovies.utilities.MovieAdapter;
import com.sravan.ad.popularmovies.utilities.TMDBMovie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Sravan on 1/3/2017.
 */

public class MovieFragment extends Fragment {

    RequestQueue fetchRequestQueue;
    private MovieAdapter movieAdapter;
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();
    private String sortPreference;
    public static final String TAG = "MyTag";
    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchRequestQueue = FetchSingleton.getInstance(this.getActivity().getApplicationContext()).getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        movieAdapter = new MovieAdapter(getContext(),new ArrayList<TMDBMovie>());
        GridView movieGridView = (GridView) rootView.findViewById(R.id.gridview_moviefragment);
        movieGridView.setAdapter(movieAdapter);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TMDBMovie movie = movieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,movie);
                startActivity(intent);
            }
        });
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
        if(movieAdapter.getCount() == 0){
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
     * updateMovieGrid method uses Volley for fetching the movies and executes
     * the task by getting the sort preference selected by the user. fetches movies with
     * vote count more than 1000.
     */
    private void updateMovieGrid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPreference = preferences.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_popularity));
        final String API_PARAM = "api_key=825205bc0a62ded8dc369348761dcef1";
        String TMDB_DISCOVER_URL = getTMDBBaseUrl(sortPreference) + API_PARAM ;

        // The String resquest is created which will be added to the request queue to get processed;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, TMDB_DISCOVER_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<TMDBMovie> tmdbMovies = null;
                        try {
                            tmdbMovies = getMovieDataFromJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (tmdbMovies != null){
                            movieAdapter.clear();
                            movieAdapter.addAll(tmdbMovies);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        // The tag is used here to cancel the request when the fragment is stopped
        stringRequest.setTag(TAG);
        FetchSingleton.getInstance(this.getActivity()).addToRequestQueue(stringRequest);
    }

    private String getTMDBBaseUrl(String sortPreference){
        if(sortPreference.equals("popular")){
            return "https://api.themoviedb.org/3/movie/popular?";
        }
        else{
            return "https://api.themoviedb.org/3/movie/top_rated?";
        }
    }

    /**
     * In the OnStop method we are cancelling all the network requests are not still waiting for in the
     * queue for the Network Dispatcher to handle them. Cancel will be used when many network calls are made
     *
     */
    @Override
    public void onStop() {
        super.onStop();
        if(fetchRequestQueue != null){
            fetchRequestQueue.cancelAll(TAG);
        }
    }

    private ArrayList<TMDBMovie> getMovieDataFromJSON(String movieString) throws JSONException {
        final String TMDB_RESULTS = "results";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_ORIGINALTITLE = "original_title";
        final String TMDB_POSTERPATH = "poster_path";
        final String TMDB_VOTEAVERAGE = "vote_average";
        final String TMDB_POSTERBASEURL = "http://image.tmdb.org/t/p/w185";
        final String TMDB_RELEASEDATE = "release_date";


        JSONObject tmdbJSONResponse = new JSONObject(movieString);
        JSONArray tmdbResultsArray = tmdbJSONResponse.getJSONArray(TMDB_RESULTS);
        ArrayList<TMDBMovie> movieList = new ArrayList<TMDBMovie>();
        for (int i = 0; i < tmdbResultsArray.length() ; i++) {
            TMDBMovie movie = new TMDBMovie();
            movie.setOriginalTitle(tmdbResultsArray.getJSONObject(i).getString(TMDB_ORIGINALTITLE));
            movie.setOverview(tmdbResultsArray.getJSONObject(i).getString(TMDB_OVERVIEW));
            movie.setPosterPath(TMDB_POSTERBASEURL + "/" +tmdbResultsArray.getJSONObject(i).getString(TMDB_POSTERPATH));
            movie.setVoteAverage(tmdbResultsArray.getJSONObject(i).getString(TMDB_VOTEAVERAGE));
            movie.setReleaseDate(tmdbResultsArray.getJSONObject(i).getString(TMDB_RELEASEDATE));
            movieList.add(movie);
        }
        return movieList;
    }

}
