package com.example.latheeshvirupakshi.popularmoviestage2.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.latheeshvirupakshi.popularmoviestage2.Fragments.MainActivityFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by latheeshvirupakshi on 2/16/17.
 */

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String poster_image;
    private String image2;
    private String overview;
    private int rating;
    private String date;

    public String getDate() {return date;}

    public String getTitle() {
        return title;
    }

    public String getPoster_image() {
        return poster_image;
    }

    public String getImage2() {
        return image2;
    }

    public String getOverview() {
        return overview;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }


    public Movie() {

    }

    public Movie(JSONObject movie) throws JSONException {
        this.id = movie.getInt("id");
        this.title = movie.getString("original_title");
        this.poster_image = movie.getString("poster_path");
        this.image2 = movie.getString("backdrop_path");
        this.overview = movie.getString("overview");
        this.rating = movie.getInt("vote_average");
        this.date = movie.getString("release_date");
    }

    public Movie(Cursor cursor) {
        this.id = cursor.getInt(MainActivityFragment.COL_MOVIE_ID);
        this.title = cursor.getString(MainActivityFragment.COL_TITLE);
        this.poster_image = cursor.getString(MainActivityFragment.COL_IMAGE);
        this.image2 = cursor.getString(MainActivityFragment.COL_IMAGE2);
        this.overview = cursor.getString(MainActivityFragment.COL_OVERVIEW);
        this.rating = cursor.getInt(MainActivityFragment.COL_RATING);
        this.date = cursor.getString(MainActivityFragment.COL_DATE);
    }


    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster_image = in.readString();
        image2 = in.readString();
        overview = in.readString();
        rating = in.readInt();
        date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {

            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster_image);
        dest.writeString(image2);
        dest.writeString(overview);
        dest.writeInt(rating);
        dest.writeString(date);
    }
}
