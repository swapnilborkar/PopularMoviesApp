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

        int w = getContext().getResources().getDisplayMetrics().widthPixels / 2; //dividing by numColumns
        int h = (int) (w * 1.5); //adjusting height to 1.5x times the width

        Picasso.with(getActivity())
                .load(baseurl + intent.getStringExtra("url"))
                .resize(w, h)
                .centerCrop()
                .into(moviePoster);


        TextView movieSynopsis = (TextView) rootView.findViewById(R.id.txt_synopsis);
        movieSynopsis.setText(intent.getStringExtra("synopsis"));

        TextView movieRating = (TextView) rootView.findViewById(R.id.txt_rating);
        movieRating.setText(intent.getStringExtra("rating"));

        TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.txt_release);
        movieReleaseDate.setText(intent.getStringExtra("release"));



        return rootView;
    }
}
