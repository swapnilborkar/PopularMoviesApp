package com.swapnilborkar.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class PostersAdapter extends ArrayAdapter<PopularMovies> {

    MainActivity mainActivity;
    private Context mContext;


    public PostersAdapter(Activity context, List<PopularMovies> popularMovies) {

        super(context, 0, popularMovies);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PopularMovies popularMovies = getItem(position);

        View gridRow = convertView;

        if (convertView == null) {

            gridRow = LayoutInflater.
                    from(getContext()).
                    inflate(R.layout.grid_item, parent, false);
        }

        String baseUrl = "http://image.tmdb.org/t/p/w342/";
        int w;
        int h;



            w = gridRow.getResources().getDisplayMetrics().widthPixels / 2; //dividing by numColumns
            h = (int) (w * 1.5); //adjusting height to 1.5x times the width

        if (gridRow.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            {
                w = gridRow.getResources().getDisplayMetrics().widthPixels / 4; //dividing by numColumns
                h = (int) (w * 1.5); //adjusting height to 1.5x times the width

            }
        }


        ImageView imageView = (ImageView) gridRow.findViewById(R.id.imageView);
            Picasso.with(mContext)
                    .load(baseUrl + popularMovies.imageUrl)
                    .resize(w, h)
                    .centerCrop()
                    .into(imageView);


        return gridRow;
    }
}
