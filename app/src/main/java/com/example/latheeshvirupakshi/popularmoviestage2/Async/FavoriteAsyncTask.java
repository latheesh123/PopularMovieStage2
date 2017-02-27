package com.example.latheeshvirupakshi.popularmoviestage2.Async;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.latheeshvirupakshi.popularmoviestage2.data.MovieContract;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Movie;
import com.example.latheeshvirupakshi.popularmoviestage2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by latheeshvirupakshi on 2/22/17.
 */

public class FavoriteAsyncTask extends AsyncTask<Void, Void, List<Movie>> {

    private Context mContext;


    public FavoriteAsyncTask(Context context) {
        mContext = context;
    }

    private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        List<Movie> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                results.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                Util.MOVIE_COLUMNS,
                null,
                null,
                null
        );
        return getFavoriteMoviesDataFromCursor(cursor);
    }



}
