package com.sravan.ad.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by HP on 1/15/2017.
 */

/**
 * The FetchMovieTask has been moved from inner class of the MovieFragment class to
 * a new class. Thie makes the code more cleaner. To update the UI based on the Async task the
 * reguired adapter reference and the context have been passed to the FetchMovieTask constructor
 * 
 */
public class FetchMovieTask extends AsyncTask<String,Void,ArrayList<TMDBMovie>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    MovieRecycleAdapter movieAdapter;
    Context context;

    public FetchMovieTask(MovieRecycleAdapter movieAdapter, Context context) {
        this.movieAdapter = movieAdapter;
        this.context = context;
    }

    @Override
    protected ArrayList<TMDBMovie> doInBackground(String... params) {

        if (params.length == 0){
            return null;
        }
        final String API_KEY = "Please provide the API key here";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieString = null;

        try {
            final String TMDB_DISCOVER_URL = getTMDBBaseUrl(params[0]);
            final String API_QUERY_PARAM = "api_key";
            Uri movieFetchQuery = Uri.parse(TMDB_DISCOVER_URL).buildUpon()
                    .appendQueryParameter(API_QUERY_PARAM, API_KEY)
                    .build();
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
            movieAdapter.updateMovies(tmdbMovies);
            /*movieAdapter.movieList.addAll(tmdbMovies);
            movieAdapter.notifyDataSetChanged();*/
        }
        else{
            Toast.makeText(context,"Error while connecting to the Internet",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method parses the JSON data received from tmdb API
     * @param movieString
     * @return
     * @throws JSONException
     */

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
    /**
     * This method return the TMDB Url based on the sort preference selected
     *
     * @param sortPreference
     * @return The TMDB URL based in the sortPreference value. If sortPreference is popular then
     * the TMDB URL for the popular movies end point is returned. Similarly if the sortPreference is top_rated
     * the top rated movies end point is returned.
     */
    private String getTMDBBaseUrl(String sortPreference){
        if(sortPreference.equals("popular")){
            return "https://api.themoviedb.org/3/movie/popular?";
        }
        else{
            return "https://api.themoviedb.org/3/movie/top_rated?";
        }
    }
}

