package com.sravan.ad.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.sravan.ad.popularmovies.data.MovieContract;
import com.sravan.ad.popularmovies.data.TMDBMovie;
import com.sravan.ad.popularmovies.data.TMDBMovieReview;
import com.sravan.ad.popularmovies.data.TMDBMovieTrailer;
import com.sravan.ad.popularmovies.utilities.FetchReviewsTask;
import com.sravan.ad.popularmovies.utilities.FetchTrailerTask;
import com.sravan.ad.popularmovies.utilities.MovieReviewRecycleAdapter;
import com.sravan.ad.popularmovies.utilities.MovieTrailerRecycleAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements MovieReviewRecycleAdapter.ReviewCallback,
        MovieTrailerRecycleAdapter.TrailorCallback {

    public static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    public static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";

    @BindView(R.id.detail_movie_title)
    TextView title;
    @BindView(R.id.detail_movie_poster)
    ImageView poster;
    @BindView(R.id.details_movie_overview)
    TextView overview;
    @BindView(R.id.detail_movie_rating)
    TextView movieRating;
    @BindView(R.id.detail_movie_release_date)
    TextView movieReleaseDate;
    @BindView(R.id.favorites_button)
    Button favouriteButton;
    @BindView(R.id.detail_movie_trailers)
    RecyclerView trailerRecyclerView;
    @BindView(R.id.detail_movie_reviews)
    RecyclerView reviewsRecycleView;

    TMDBMovie mTMDBmovie = null;
    MovieTrailerRecycleAdapter movieTrailerRecycleAdapter;
    MovieReviewRecycleAdapter movieReviewRecycleAdapter;

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareTrailer();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    }
                });
                if(savedInstanceState == null) {
                    updateFavourites(mTMDBmovie.getMovieId());
                }
                movieTrailerRecycleAdapter = new MovieTrailerRecycleAdapter(getActivity(),new ArrayList<TMDBMovieTrailer>(),this);
                LinearLayoutManager trailorLayoutManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                trailerRecyclerView.setLayoutManager(trailorLayoutManager);
                trailerRecyclerView.setAdapter(movieTrailerRecycleAdapter);
                fetchTrailers();
                movieReviewRecycleAdapter = new MovieReviewRecycleAdapter(getActivity(),new ArrayList<TMDBMovieReview>(),this);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                reviewsRecycleView.setLayoutManager(mLayoutManager);
                reviewsRecycleView.setAdapter(movieReviewRecycleAdapter);

                if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
                    ArrayList<TMDBMovieTrailer> trailers = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
                    movieTrailerRecycleAdapter.movieTrailers.clear();
                    movieTrailerRecycleAdapter.movieTrailers.addAll(trailers);
                    movieTrailerRecycleAdapter.notifyDataSetChanged();
                }else {
                    fetchTrailers();
                }
                if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
                    ArrayList<TMDBMovieReview> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
                    movieReviewRecycleAdapter.movieReviewArrayList.clear();
                    movieReviewRecycleAdapter.movieReviewArrayList.addAll(reviews);
                    movieReviewRecycleAdapter.notifyDataSetChanged();
                } else {
                    fetchReviews();
                }
            }
        }
        else{
            Toast.makeText(getContext(),"Unable to display the movie details", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<TMDBMovieReview> reviews = movieReviewRecycleAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty()){
            outState.putParcelableArrayList(EXTRA_REVIEWS,reviews);
        }
        ArrayList<TMDBMovieTrailer> trailers = movieTrailerRecycleAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()){
            outState.putParcelableArrayList(EXTRA_TRAILERS,reviews);
        }
    }

    private void fetchReviews() {
        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(getActivity(),movieReviewRecycleAdapter);
        fetchReviewsTask.execute(mTMDBmovie.getMovieId());
    }

    private void fetchTrailers() {
        FetchTrailerTask fetchTrailerTask = new FetchTrailerTask(getActivity(),movieTrailerRecycleAdapter);
        fetchTrailerTask.execute(mTMDBmovie.getMovieId());
    }

    private void shareTrailer() {
        Log.i(LOG_TAG,"On Share");
        if (movieTrailerRecycleAdapter.getItemCount() > 0){
            TMDBMovieTrailer movieTrailer = movieTrailerRecycleAdapter.getTrailers().get(0);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTMDBmovie.getOriginalTitle());
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, movieTrailer.getName() + ": "
                    + movieTrailer.getTrailerUrl());
            startActivity(Intent.createChooser(sharingIntent, getActivity().getString(R.string.action_share)));
        }
        else {
            Toast.makeText(getActivity(), "There are not Trailers to share", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void open(TMDBMovieReview review) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(review.getUrl())));
    }

    @Override
    public void open(TMDBMovieTrailer trailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl())));
    }
}
