package com.sravan.ad.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.sravan.ad.popularmovies.data.MovieContract;
import com.sravan.ad.popularmovies.data.TMDBMovie;
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
    @BindView(R.id.favorites_button) Button favouriteButton;
    TMDBMovie mTMDBmovie = null;

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
            mTMDBmovie= (TMDBMovie) intent.getParcelableExtra(Intent.EXTRA_TEXT);
            if (mTMDBmovie == null){
                Toast.makeText(getContext(),"Unable to display the movie details", Toast.LENGTH_SHORT).show();
            }
            else{
                title.setText(mTMDBmovie.getOriginalTitle());
                Picasso.with(getContext()).load(mTMDBmovie.getPosterPath()).into(poster);
                overview.setText(mTMDBmovie.getOverview());
                movieRating.setText(mTMDBmovie.getVoteAverage() +"/10");
                movieReleaseDate.setText(mTMDBmovie.getReleaseDate());
                favouriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleFavourite(mTMDBmovie);
                        //Log.i(LOG_TAG, "Favourite Buttion Clicked");

                        /*Cursor cursor = getActivity().getContentResolver().query(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                                MovieContract.FavoriteMovieEntry.MOVIE_COLUMNS,
                                null,
                                null,
                                null);
                        if (cursor.moveToNext()){
                            Log.e(LOG_TAG, cursor.getString(1));
                        }*/
                    }
                });
                if(savedInstanceState == null) {
                    updateFavourites(mTMDBmovie.getMovieId());
                }
            }
        }
        else{
            Toast.makeText(getContext(),"Unable to display the movie details", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void toggleFavourite(TMDBMovie movie) {
        new AsyncTask<TMDBMovie, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavourites(mTMDBmovie.getMovieId());
            }

            @Override
            protected Void doInBackground(TMDBMovie... params) {
                TMDBMovie movie = params[0];
                boolean favouriteMovie = isFavourite(movie.getMovieId());
                if (!favouriteMovie){
                    addMovieToFavourite(movie);
                }
                else{
                    deleteMovieFromFavourite(movie);
                }
                return null;
            }

        }.execute(movie);
    }

    private int deleteMovieFromFavourite(TMDBMovie movie) {
        return getContext().getContentResolver().delete(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{movie.getMovieId()});
    }

    private void addMovieToFavourite(TMDBMovie movie) {
        Uri uri = getContext().getContentResolver().insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, movie.getContentValue());

    }

    private void updateFavourites(String movieId) {
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                return isFavourite(params[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean){
                    favouriteButton.setText("Remove From Favourites");
                    favouriteButton.setBackgroundColor(Color.RED);
                } else{
                    favouriteButton.setText("Add To Favourites");
                    favouriteButton.setBackgroundColor(Color.YELLOW);
                }
            }
        }.execute(movieId);
    }

    public boolean isFavourite(String movieId){
        String Id = movieId;
        String selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?";
        Cursor cursor = getContext().getContentResolver().query(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                selection,
                new String[]{Id},
                null,
                null);
        if(cursor.getCount()>0){
            return true;
        }
        else {
            return false;
        }
    }

}
