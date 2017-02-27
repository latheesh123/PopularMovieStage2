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
import com.example.latheeshvirupakshi.popularmoviestage2.model.Trailer;
import com.example.latheeshvirupakshi.popularmoviestage2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by latheeshvirupakshi on 2/16/17.
 */

public class TrailerAdapter extends BaseAdapter {


    private final Context mContext;
    private final LayoutInflater mInflater;
    private final Trailer mTrailer = new Trailer();

    private List<Trailer> mObjects;

    public TrailerAdapter(Context context, List<Trailer> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Trailer object) {
        synchronized (mTrailer) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mTrailer) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Trailer getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.trailer, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Trailer trailer = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        String thumbnail_url = Util.trailerUrl+ trailer.getKey() + "/0.jpg";
        Glide.with(getContext()).load(thumbnail_url).into(viewHolder.imageView);

        viewHolder.nameView.setText(trailer.getName());

        return view;
    }

    public static class ViewHolder {

        @BindView(R.id.trailer_image) ImageView imageView;
        @BindView(R.id.trailer_name) TextView nameView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

}
