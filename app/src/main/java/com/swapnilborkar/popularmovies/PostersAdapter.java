package com.swapnilborkar.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class PostersAdapter extends ArrayAdapter<PopularMovies> {

    MainActivity mainActivity;
    Context mcontext;


    public PostersAdapter(Activity context, List<PopularMovies> popularMovies) {

        super(context, 0, popularMovies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PopularMovies popularMovies = getItem(position);

        View gridRow = convertView;

        if (convertView == null) {

            gridRow = LayoutInflater.
                    from(getContext()).
                    inflate(R.layout.grid_item, parent, false);
        } else {

            ImageView imageView = (ImageView) gridRow.findViewById(R.id.imageView);
            //imageView.setImageResource(popularMovies.moviePoster);
            Picasso.with(getContext().getApplicationContext()).load("http://image.tmdb.org/t/p/w780//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(imageView);

            TextView textView = (TextView) gridRow.findViewById(R.id.textView);
            textView.setText(popularMovies.title);

        }

        return gridRow;
    }
}
