package com.example.latheeshvirupakshi.popularmoviestage2.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.latheeshvirupakshi.popularmoviestage2.Adapter.MovieListAdapter;
import com.example.latheeshvirupakshi.popularmoviestage2.Async.FavoriteAsyncTask;
import com.example.latheeshvirupakshi.popularmoviestage2.Async.MoviesAsyncTask;
import com.example.latheeshvirupakshi.popularmoviestage2.interfaces.Callback;
import com.example.latheeshvirupakshi.popularmoviestage2.R;
import com.example.latheeshvirupakshi.popularmoviestage2.data.MovieContract;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Movie;
import com.example.latheeshvirupakshi.popularmoviestage2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by latheeshvirupakshi on 2/16/17.
 */

public class MainActivityFragment extends Fragment {


    private GridView mGridView;

    private MovieListAdapter mMovieGridAdapter;

    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String FAVORITE = "favorite";
    private static final String MOVIES_KEY = "movies";

    private String mSortBy = POPULARITY_DESC;

    private ArrayList<Movie> mMovies = null;

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_IMAGE2 = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_RATING = 6;
    public static final int COL_DATE = 7;




    public MainActivityFragment() {
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        MenuItem action_sort_by_favorite = menu.findItem(R.id.action_sort_by_favorite);


        if (mSortBy.contentEquals(POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_popularity.setChecked(true);
            }
        } else if (mSortBy.contentEquals(RATING_DESC)) {
            if (!action_sort_by_rating.isChecked()) {
                action_sort_by_rating.setChecked(true);
            }
        } else if (mSortBy.contentEquals(FAVORITE)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_favorite.setChecked(true);
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = POPULARITY_DESC;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = RATING_DESC;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_favorite:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = FAVORITE;
                updateMovies(mSortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_fragment, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridview_movies);

        mMovieGridAdapter = new MovieListAdapter(getActivity(), new ArrayList<Movie>());

        mGridView.setAdapter(mMovieGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieGridAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                mSortBy = savedInstanceState.getString(SORT_SETTING_KEY);
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                mMovieGridAdapter.setData(mMovies);
            } else {
                updateMovies(mSortBy);
            }
        } else {
            updateMovies(mSortBy);
        }

        return view;
    }


    private void updateMovies(String sort_by) {
        if (!sort_by.contentEquals(FAVORITE)) {
            new MoviesAsyncTask(){

                @Override
                protected void onPostExecute(List<Movie> movies) {
                    if (movies != null) {
                        if (mMovieGridAdapter != null) {
                            mMovieGridAdapter.setData(movies);
                        }
                        mMovies = new ArrayList<>();
                        mMovies.addAll(movies);
                    }
                }
            }.execute(sort_by);

        }

        else {


            new FavoriteAsyncTask(getActivity())
            {
                @Override
                protected void onPostExecute(List<Movie> movies) {
                    if (movies != null) {
                        if (mMovieGridAdapter != null) {
                            mMovieGridAdapter.setData(movies);
                        }
                        mMovies = new ArrayList<>();
                        mMovies.addAll(movies);
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!mSortBy.contentEquals(POPULARITY_DESC)) {
            outState.putString(SORT_SETTING_KEY, mSortBy);
        }
        if (mMovies != null) {
            outState.putParcelableArrayList(MOVIES_KEY, mMovies);
        }
        super.onSaveInstanceState(outState);
    }

}
