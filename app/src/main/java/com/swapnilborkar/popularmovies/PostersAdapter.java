package com.swapnilborkar.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by SWAPNIL on 07-02-2016.
 */
public class PostersAdapter extends BaseAdapter {

    MainActivity mainActivity;
    private ArrayList<Integer> images;


    public PostersAdapter(MainActivity mainActivity, ArrayList<Integer> images)
    {
        this.mainActivity=mainActivity;
        this.images=images;
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridRow = convertView;
        if (convertView == null) {

            gridRow = LayoutInflater.
                    from(mainActivity).
                    inflate(R.layout.grid_row, parent, false);
        }

        ImageView imageView = (ImageView) gridRow.findViewById(R.id.imageView);
        imageView.setImageResource(images.get(position));

        return gridRow;
    }
}
