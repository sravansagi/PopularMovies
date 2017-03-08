package com.sravan.ad.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by HP on 3/8/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favouritemovies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME
                + " (" +
                MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL" +
                " );";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieContract.FavoriteMovieEntry.TABLE_NAME );
        onCreate(db);
    }
}
