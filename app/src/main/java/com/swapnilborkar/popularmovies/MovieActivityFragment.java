package com.swapnilborkar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieActivityFragment extends Fragment {

    public MovieActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        String baseurl = "http://image.tmdb.org/t/p/w780/";
        Intent intent = getActivity().getIntent();

        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.img_poster);
        Picasso.with(getActivity()).load(baseurl + intent.getStringExtra("url")).into(moviePoster);


        TextView movieSynopsis = (TextView) rootView.findViewById(R.id.txt_synopsis);
        movieSynopsis.setText(intent.getStringExtra("synopsis"));


        return rootView;
    }
}
