package com.greek303g.movieapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmedabobakr on 9/21/16.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movies.db";

    public MoviesDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.FavouriteMoviesEntry.TABLE_NAME +
                " (" + MoviesContract.FavouriteMoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_ID + " TEXT UNIQUE NOT NULL, " +
                MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_POSTER + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXSISTS " + MoviesContract.FavouriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
