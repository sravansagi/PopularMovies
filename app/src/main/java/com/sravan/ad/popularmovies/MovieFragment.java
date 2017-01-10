package com.sravan.ad.popularmovies;

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
import android.widget.GridView;
import android.widget.Toast;
import com.sravan.ad.popularmovies.utilities.MovieAdapter;
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

public class MovieFragment extends Fragment {


    private MovieAdapter movieAdapter;
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
        movieAdapter = new MovieAdapter(getContext(),new ArrayList<TMDBMovie>());
        GridView movieGridView = (GridView) rootView.findViewById(R.id.gridview_moviefragment);
        movieGridView.setAdapter(movieAdapter);
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
        Log.i(LOG_TAG,"The Fragment has been resumed");
        super.onResume();
        if(movieAdapter.getCount() == 0){
            Log.i(LOG_TAG,"movieAdapter 0 check");
            updateMovieGrid();
        }else{
            Log.i(LOG_TAG,"Inside else");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String preferencesString = preferences.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_popularity));
            Log.i(LOG_TAG,"Preference values" + sortPreference + ":" + preferencesString);
            if (!preferencesString.equalsIgnoreCase(sortPreference)){
                Log.i(LOG_TAG,"The UpdateGrid is called");
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
        Log.i(LOG_TAG,"mAdapter Count" + movieAdapter.getCount());
        Log.i(LOG_TAG,"The Fragment has been started");
        super.onStart();
    }

    private void updateMovieGrid() {
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPreference = preferences.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_popularity));
        movieTask.execute(sortPreference);
    }

    public class FetchMovieTask extends AsyncTask<String,Void,ArrayList<TMDBMovie>>{

        public final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected ArrayList<TMDBMovie> doInBackground(String... params) {

            if (params.length == 0){
                return null;
            }
            final String API_KEY = "825205bc0a62ded8dc369348761dcef1";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieString = null;

            try {
                final String TMDB_DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_QUERY_PARAM = "sort_by";
                String SortByQueryValue = params[0];
                final String API_QUERY_PARAM = "api_key";
                Uri movieFetchQuery = Uri.parse(TMDB_DISCOVER_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_QUERY_PARAM,SortByQueryValue)
                        .appendQueryParameter(API_QUERY_PARAM, API_KEY)
                        .build();
                Log.i(LOG_TAG,"The URL" + movieFetchQuery.toString());
                URL movieUrl = new URL(movieFetchQuery.toString());
                urlConnection = (HttpURLConnection) movieUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null){
                    //No Data for processing
                    return null;
                }
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine())!=null){
                    buffer.append(line);
                }
                if (buffer.length() == 0){
                    return null;
                }
                movieString = buffer.toString();
            } catch (java.io.IOException e) {
                Log.e(LOG_TAG,"I/O Error", e);
                return null;
            }
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if  (reader!=null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream",e);
                    }
                }
            }
            try {
                return getMovieDataFromJSON(movieString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error while processing JSON",e);
            }
            // If this return is places in the above catch block on start methods is called twice
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TMDBMovie> tmdbMovies) {
            if (tmdbMovies != null){
                movieAdapter.clear();
                movieAdapter.addAll(tmdbMovies);
            }
            else{
                Toast.makeText(getContext(),"Error while connecting to the Internet",Toast.LENGTH_SHORT).show();
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
            // To be deleted
            /*for (TMDBMovie mov: movieList
                 ) {
                Log.i(LOG_TAG, "The movie is " + mov.getOriginalTitle() + " : " + mov.getOverview() );
            }*/
            return movieList;
        }
    }
}
