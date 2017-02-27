package com.example.latheeshvirupakshi.popularmoviestage2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.latheeshvirupakshi.popularmoviestage2.R;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Movie;
import com.example.latheeshvirupakshi.popularmoviestage2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by latheeshvirupakshi on 2/16/17.
 */


public class MovieListAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final Movie mMovie=new Movie();

    private List<Movie> mObjects;

    public MovieListAdapter(Context context, List<Movie> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Movie object) {
        synchronized (mMovie) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }


    public void clear() {
        synchronized (mMovie) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }


    public void setData(List<Movie> data) {
        clear();
        for (Movie movie : data) {
            add(movie);
        }
    }


    @Override
    public int getCount() {
        return mObjects.size();
    }


    @Override
    public Movie getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row=convertView;

        ViewHolder viewHolder;
        if (row==null)
        {

            row=mInflater.inflate(R.layout.movie,parent,false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);

        }

        final Movie movie = getItem(position);

        String image_url = Util.imageUrl+ movie.getPoster_image();

        viewHolder = (ViewHolder) row.getTag();

        Glide.with(getContext()).load(image_url).into(viewHolder.imageView);
        viewHolder.titleView.setText(movie.getTitle());
        return row;
    }


    public static class ViewHolder {

        @BindView(R.id.grid_item_image) ImageView imageView;
        @BindView(R.id.grid_item_title) TextView titleView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
