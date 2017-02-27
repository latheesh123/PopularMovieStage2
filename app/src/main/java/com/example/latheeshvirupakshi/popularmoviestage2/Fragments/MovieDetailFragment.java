package com.example.latheeshvirupakshi.popularmoviestage2.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.latheeshvirupakshi.popularmoviestage2.Adapter.ReviewAdapter;
import com.example.latheeshvirupakshi.popularmoviestage2.Adapter.TrailerAdapter;
import com.example.latheeshvirupakshi.popularmoviestage2.Async.ReviewAsyncTask;
import com.example.latheeshvirupakshi.popularmoviestage2.Async.TrailerAsyncTask;
import com.example.latheeshvirupakshi.popularmoviestage2.R;
import com.example.latheeshvirupakshi.popularmoviestage2.data.MovieContract;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Movie;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Review;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Trailer;
import com.example.latheeshvirupakshi.popularmoviestage2.ui.MovieDetailsActivity;
import com.example.latheeshvirupakshi.popularmoviestage2.util.Util;
import com.linearlistview.LinearListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.latheeshvirupakshi.popularmoviestage2.R.layout.trailer;

/**
 * Created by latheeshvirupakshi on 2/16/17.
 */

public class MovieDetailFragment extends Fragment {

    public static final String TAG = MovieDetailFragment.class.getSimpleName();

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private Movie mMovie;


    @BindView(R.id.detail_image) ImageView mImageView;
    @BindView(R.id.detail_title) TextView mTitleView;
    @BindView(R.id.detail_overview) TextView mOverviewView;
    @BindView(R.id.detail_date) TextView mDateView;
    @BindView(R.id.detail_vote_average) TextView mVoteAverageView;

    @BindView(R.id.detail_trailers) LinearListView mTrailersView;
    @BindView(R.id.detail_reviews) LinearListView mReviewsView;

    @BindView(R.id.detail_reviews_cardview) CardView mReviewsCardview;
    @BindView(R.id.detail_trailers_cardview) CardView mTrailersCardview;

    @BindView(R.id.detail_layout) ScrollView mDetailLayout;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private Toast mToast;
    private ShareActionProvider mShareActionProvider;

    private Trailer mTrailer;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mMovie != null) {
            inflater.inflate(R.menu.detail, menu);

            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return Util.isFavorited(getActivity(), mMovie.getId());
                }

                @Override
                protected void onPostExecute(Integer isFavorited) {
                    action_favorite.setIcon(isFavorited == 1 ?
                            R.drawable.abc_btn_rating_star_on_mtrl_alpha :
                            R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                }
            }.execute();


        }
    }



    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                if (mMovie != null) {


                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            return Util.isFavorited(getActivity(), mMovie.getId());
                        }

                        @Override
                        protected void onPostExecute(Integer isFavorited) {
                            if (isFavorited == 1) {
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(
                                                MovieContract.MovieEntry.CONTENT_URI,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                                new String[]{Integer.toString(mMovie.getId())}
                                        );
                                    }

                                    @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                            else {
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();

                                        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
                                        values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
                                        values.put(MovieContract.MovieEntry.COLUMN_IMAGE, mMovie.getPoster_image());
                                        values.put(MovieContract.MovieEntry.COLUMN_IMAGE2, mMovie.getImage2());
                                        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                                        values.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getRating());
                                        values.put(MovieContract.MovieEntry.COLUMN_DATE, mMovie.getDate());

                                        return getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                                                values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;


            case R.id.action_share:

                startActivity(Intent.createChooser(Sharemovie(),getString(R.string.Sharemessage)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(MovieDetailFragment.DETAIL_MOVIE);
        }

        View rootView = inflater.inflate(R.layout.activity_details_fragment, container, false);
        ButterKnife.bind(this, rootView);

        if (mMovie != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }



        mTrailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
        mTrailersView.setAdapter(mTrailerAdapter);

        mTrailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {

                Trailer trailer = mTrailerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Util.trailerlist + trailer.getKey()));
                startActivity(intent);
            }
        });

        mReviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
        mReviewsView.setAdapter(mReviewAdapter);

        if (mMovie != null) {

            String image_url = Util.buildImageUrl(185, mMovie.getImage2());
            Glide.with(this).load(image_url).into(mImageView);

            mTitleView.setText(mMovie.getTitle());
            mOverviewView.setText(mMovie.getOverview());


            String movie_date = mMovie.getDate();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mDateView.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mVoteAverageView.setText(Integer.toString(mMovie.getRating())+"/10");
        }

        String sharedFact = mTitleView.getText().toString();
        getActivity().setTitle(sharedFact);


        return rootView;
    }



    public Intent Sharemovie() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + " " +
                "http://www.youtube.com/watch?v=" + mTrailer.getKey());
        return shareIntent;
    }





    @Override
    public void onStart() {
        super.onStart();

        if (mMovie != null) {

            new TrailerAsyncTask()
            {

                @Override
                protected void onPostExecute(List<Trailer> trailers) {
                    if (trailers != null) {
                        if (trailers.size() > 0) {
                            mTrailersCardview.setVisibility(View.VISIBLE);
                            if (mTrailerAdapter != null) {
                                mTrailerAdapter.clear();
                                for (Trailer trailer : trailers) {
                                    mTrailerAdapter.add(trailer);
                                }
                            }

                            mTrailer = trailers.get(0);
                            if (mShareActionProvider != null) {
                                mShareActionProvider.setShareIntent(Sharemovie());
                            }
                        }
                    }
                }
            }.execute(Integer.toString(mMovie.getId()));



         new ReviewAsyncTask()
            {

                @Override
                protected void onPostExecute(List<Review> reviews) {
                    if (reviews != null) {
                        if (reviews.size() > 0) {
                            mReviewsCardview.setVisibility(View.VISIBLE);
                            if (mReviewAdapter != null) {
                                mReviewAdapter.clear();
                                for (Review review : reviews) {
                                    mReviewAdapter.add(review);
                                }
                            }
                        }
                    }
                }
            }.execute(Integer.toString(mMovie.getId()));


        }
    }





}
