package com.swapnilborkar.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;


public class ReviewAdapter extends ArrayAdapter<MovieReviews> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    public ReviewAdapter(Context context, ArrayList<MovieReviews> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get Movie object from the ArrayAdapter at the appropriate position
        MovieReviews current = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.expandableTextView.setText(current.content);

        return convertView;
    }

    private static class ViewHolder {
        ExpandableTextView expandableTextView;
    }
}