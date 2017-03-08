package com.sravan.ad.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by HP on 3/8/2017.
 */

public class MovieProvider extends ContentProvider{

    private MovieDBHelper movieDBHelper;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private final static int FAV_MOVIE = 100;
    //private final static int FAV_MOVIE_ID = 200;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieContract.PATH_FAV_MOVIE, FAV_MOVIE);
        //uriMatcher.addURI(authority, MovieContract.PATH_FAV_MOVIE + "/#", FAV_MOVIE_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
       movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (URI_MATCHER.match(uri)) {
            case FAV_MOVIE:
                retCursor = movieDBHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            /*case FAV_MOVIE_ID:
                retCursor = movieDBHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;*/
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        if (getContext() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = URI_MATCHER.match(uri);
        switch (match){
            case FAV_MOVIE:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
           /* case FAV_MOVIE_ID:
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;*/
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        Uri returnUri;
        switch (URI_MATCHER.match(uri)) {
            case FAV_MOVIE:
                 long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                         null,
                        values);
                if ( _id > 0 )
                    returnUri = MovieContract.FavoriteMovieEntry.buildFavouriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsDeleted;
        switch (URI_MATCHER.match(uri)) {
            case FAV_MOVIE:
                rowsDeleted = db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsUpdated;
        switch (URI_MATCHER.match(uri)) {
            case FAV_MOVIE:
                rowsUpdated = db.update(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
