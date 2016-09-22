package com.greek303g.movieapp.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ahmedabobakr on 9/21/16.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.greek303g.movieapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE_MOVIES = "favourite_movies";

    public static class FavouriteMoviesEntry  implements BaseColumns{
        public static final String TABLE_NAME = "favourite_movies";

        public static final String COLUMN_Favourite_Moies_ID = "movie_id";

        public static final String COLUMN_Favourite_Moies_POSTER = "movie_poster";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_FAVOURITE_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_FAVOURITE_MOVIES;

        public static Uri buildFavouriteMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
