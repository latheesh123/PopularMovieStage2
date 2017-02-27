package com.example.latheeshvirupakshi.popularmoviestage2.ui;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.latheeshvirupakshi.popularmoviestage2.Fragments.MovieDetailFragment;
import com.example.latheeshvirupakshi.popularmoviestage2.R;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Movie;


public class MovieDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

     //   View decorView = getWindow().getDecorView();
      //  int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
     //           | View.SYSTEM_UI_FLAG_FULLSCREEN;
      //  decorView.setSystemUiVisibility(uiOptions);



        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.DETAIL_MOVIE,
                    getIntent().getParcelableExtra(MovieDetailFragment.DETAIL_MOVIE));

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);


            getFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container,fragment)
                    .commit();
        }
    }
}
