package com.example.latheeshvirupakshi.popularmoviestage2.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.latheeshvirupakshi.popularmoviestage2.data.MovieContract;

/**
 * Created by latheeshvirupakshi on 2/16/17.
 */

public class Util {
    public static int isFavorited(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,   // projection
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[] { Integer.toString(id) },   // selectionArgs
                null    // sort order
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }
    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_IMAGE2,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DATE
    };

public static String imageUrl="http://image.tmdb.org/t/p/w185";

    public static String trailerUrl="http://img.youtube.com/vi/";


    public static String trailerlist="http://www.youtube.com/watch?v=";

    public static String buildImageUrl(int width, String fileName) {
        return "http://image.tmdb.org/t/p/w" + Integer.toString(width) + fileName;
    }
}
