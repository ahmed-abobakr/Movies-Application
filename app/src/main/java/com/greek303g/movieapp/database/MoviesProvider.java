package com.greek303g.movieapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ahmedabobakr on 9/21/16.
 */
public class MoviesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    public static final int FAVOURITE_MOVIES = 100;
    public static final int FAVOURITE_MOVIES_WITH_ID = 101;


    private static final String sFavouriteMoviesIdSelection =
            MoviesContract.FavouriteMoviesEntry.TABLE_NAME+
                    "." + MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_ID + " = ? ";


    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor resultCursor;
        switch (sUriMatcher.match(uri)){
            case FAVOURITE_MOVIES:
                resultCursor = db.query(MoviesContract.FavouriteMoviesEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case FAVOURITE_MOVIES_WITH_ID:
                resultCursor = db.query(MoviesContract.FavouriteMoviesEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, null);
                break;
            default:
                throw  new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return resultCursor;
    }


    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case FAVOURITE_MOVIES:
                return MoviesContract.FavouriteMoviesEntry.CONTENT_TYPE;
            case FAVOURITE_MOVIES_WITH_ID:
                return MoviesContract.FavouriteMoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("UnKnown Uri: " + uri);
        }

    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case FAVOURITE_MOVIES:
                _id = db.insert(MoviesContract.FavouriteMoviesEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = MoviesContract.FavouriteMoviesEntry.buildFavouriteMoviesUri(_id);
                }else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("UnKnown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        switch (sUriMatcher.match(uri)){
            case FAVOURITE_MOVIES:
                rows = db.delete(MoviesContract.FavouriteMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw  new UnsupportedOperationException("Unknow Uri: " + uri);
        }

        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        switch (sUriMatcher.match(uri)){
            case FAVOURITE_MOVIES:
                rows = db.update(MoviesContract.FavouriteMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }




    public static UriMatcher buildUriMatcher(){
        String content = MoviesContract.CONTENT_AUTHORITY;

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(content, MoviesContract.PATH_FAVOURITE_MOVIES, FAVOURITE_MOVIES);
        uriMatcher.addURI(content, MoviesContract.PATH_FAVOURITE_MOVIES + "/#", FAVOURITE_MOVIES_WITH_ID);
        return uriMatcher;
    }
}
