package com.example.latheeshvirupakshi.popularmoviestage2.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latheeshvirupakshi.popularmoviestage2.R;
import com.example.latheeshvirupakshi.popularmoviestage2.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by latheeshvirupakshi on 2/16/17.
 */


public class ReviewAdapter extends BaseAdapter {



    private final Context mContext;
    private final LayoutInflater mInflater;
    private final Review mReview = new Review();

    private List<Review> mObjects;

    public ReviewAdapter(Context context, List<Review> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Review object) {
        synchronized (mReview) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mReview) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Review getItem(int position) {
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
            view = mInflater.inflate(R.layout.review, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Review review = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        viewHolder.authorView.setText(review.getAuthor());
        viewHolder.contentView.setText(Html.fromHtml(review.getContent()));

        return view;
    }

    public static class ViewHolder {

        @BindView(R.id.review_author) TextView authorView;
        @BindView(R.id.review_content) TextView  contentView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
