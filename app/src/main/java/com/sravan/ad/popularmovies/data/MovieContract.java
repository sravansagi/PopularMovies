package com.sravan.ad.popularmovies.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {




    public static final String CONTENT_AUTHORITY = "com.sravan.ad.popularmovies.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAV_MOVIE = "favourite";

    public static final class FavoriteMovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "original_title";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        public static Uri buildFavouriteMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_POSTER_PATH,
                COLUMN_MOVIE_OVERVIEW,
                COLUMN_MOVIE_VOTE_AVERAGE,
                COLUMN_MOVIE_RELEASE_DATE,
        };
    }

}
