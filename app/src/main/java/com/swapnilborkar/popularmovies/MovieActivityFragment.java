package com.swapnilborkar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

//        String baseurl = "http://image.tmdb.org/t/p/w780/";
        Intent intent = getActivity().getIntent();

//        final ImageView moviePoster = (ImageView) rootView.findViewById(R.id.img_poster);

        TextView movieSynopsis = (TextView) rootView.findViewById(R.id.txt_synopsis);
        movieSynopsis.setText(intent.getStringExtra("synopsis"));

        final TextView movieRating = (TextView) rootView.findViewById(R.id.txt_rating);
        movieRating.setText(intent.getStringExtra("rating"));

        final TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.txt_release);
        String releaseYear = intent.getStringExtra("release");
        movieReleaseDate.setText(releaseYear.substring(0, 4));





        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();



    }
}



