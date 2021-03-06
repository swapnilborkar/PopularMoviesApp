package com.swapnilborkar.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MovieTrailersAdapter extends ArrayAdapter<MovieTrailers> {

    public MovieTrailersAdapter(Context context, ArrayList<MovieTrailers> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get Movie object from the ArrayAdapter at the appropriate position
        MovieTrailers current = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);
        }
        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerName.setText(current.name);


        return convertView;
    }
}


