package com.example.latheeshvirupakshi.popularmoviestage2.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.latheeshvirupakshi.popularmoviestage2.interfaces.Callback;
import com.example.latheeshvirupakshi.popularmoviestage2.Fragments.MovieDetailFragment;
import com.example.latheeshvirupakshi.popularmoviestage2.R;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Movie;

public class MainActivity extends AppCompatActivity implements Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Movie Stage 2");

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(),
                                MovieDetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.DETAIL_MOVIE, movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,fragment, MovieDetailFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class)
                    .putExtra(MovieDetailFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }
}
