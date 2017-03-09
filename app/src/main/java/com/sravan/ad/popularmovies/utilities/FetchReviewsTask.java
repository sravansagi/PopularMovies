package com.sravan.ad.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sravan.ad.popularmovies.BuildConfig;
import com.sravan.ad.popularmovies.data.TMDBMovieReview;

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
 * Created by HP on 3/9/2017.
 */

public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<TMDBMovieReview>> {

    public static final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    Context context;
    MovieReviewRecycleAdapter movieReviewRecycleAdapter;

    public FetchReviewsTask(Context context, MovieReviewRecycleAdapter movieReviewRecycleAdapter) {
        this.context = context;
        this.movieReviewRecycleAdapter = movieReviewRecycleAdapter;
    }

    @Override
    protected ArrayList<TMDBMovieReview> doInBackground(String... params) {
        {

            final String API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieTrailerString = null;

            try {
                String movieId = params[0];
                final String TMDB_TRAILER_URL = "https://api.themoviedb.org/3/movie/"+movieId+"/reviews?";
                final String API_QUERY_PARAM = "api_key";
                Uri movieTrailerFetchQuery = Uri.parse(TMDB_TRAILER_URL).buildUpon()
                        .appendQueryParameter(API_QUERY_PARAM, API_KEY)
                        .build();
                URL movieTrailerUrl = new URL(movieTrailerFetchQuery.toString());
                urlConnection = (HttpURLConnection) movieTrailerUrl.openConnection();
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
                movieTrailerString = buffer.toString();
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
                return getMovieReviewDataFromJSON(movieTrailerString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error while processing JSON",e);
            }
            return null;
        }
    }

    private ArrayList<TMDBMovieReview> getMovieReviewDataFromJSON(String movieTrailerString) throws JSONException {
        JSONObject tmdbResponce = new JSONObject(movieTrailerString);
        JSONArray resultsArray = tmdbResponce.getJSONArray("results");
        ArrayList<TMDBMovieReview> movieReviews = new ArrayList<TMDBMovieReview>();
        for (int i = 0; i < resultsArray.length(); i++) {
            TMDBMovieReview movieReview = new TMDBMovieReview();
            movieReview.setId(resultsArray.getJSONObject(i).getString("id"));
            movieReview.setAuthor(resultsArray.getJSONObject(i).getString("author"));
            movieReview.setContent(resultsArray.getJSONObject(i).getString("content"));
            movieReview.setUrl(resultsArray.getJSONObject(i).getString("url"));
            movieReviews.add(movieReview);
        }
        return movieReviews;
    }

    @Override
    protected void onPostExecute(ArrayList<TMDBMovieReview> tmdbMovieReviews) {
        if (tmdbMovieReviews != null){
            movieReviewRecycleAdapter.movieReviewArrayList.clear();
            movieReviewRecycleAdapter.movieReviewArrayList.addAll(tmdbMovieReviews);
            movieReviewRecycleAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(context, "Unable to retrieve reviews. Please check network connection", Toast.LENGTH_SHORT).show();
        }
    }
}
