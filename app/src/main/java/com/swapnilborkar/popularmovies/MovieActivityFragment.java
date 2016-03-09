package com.swapnilborkar.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieActivityFragment extends Fragment {

    private static final String LOG_TAG = "MovieActivityFragment:";

    public MovieActivityFragment() {



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        Intent intent = getActivity().getIntent();


        TextView movieSynopsis = (TextView) rootView.findViewById(R.id.txt_overview);
        movieSynopsis.setText(intent.getStringExtra("synopsis"));

        if (movieSynopsis.getText() == null) {
            movieSynopsis.setText(getResources().getText(R.string.no_synopsis));
        }


        final TextView movieRating = (TextView) rootView.findViewById(R.id.txt_rating);
        movieRating.setText(intent.getStringExtra("rating"));

        final TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.txt_release);
        String releaseYear = intent.getStringExtra("release");
        movieReleaseDate.setText(releaseYear.substring(0, 4));

        final TextView moviePopularity = (TextView) rootView.findViewById(R.id.txt_popularity);
        String popularity = intent.getStringExtra("popularity");
        moviePopularity.setText(popularity.substring(0, 4));

        final String backdropBaseUrl = "http://image.tmdb.org/t/p/w500/";
        String backdropUrl = getActivity().getIntent().getStringExtra("backdrop");

        final ImageView backdropImage = (ImageView)rootView.findViewById(R.id.img_backdrop);

        Picasso.with(getContext())
                .load(backdropBaseUrl + backdropUrl).into(backdropImage);


                        FetchReviewTask reviewTask = new FetchReviewTask();
                        reviewTask.execute();

                        FetchTrailerTask trailerTask = new FetchTrailerTask();
                        trailerTask.execute();


        return rootView;

    }

    public String getMovieId()
    {
        Intent intent = getActivity().getIntent();
        return Integer.toString(intent.getIntExtra("id", 0));
    }




    public class FetchReviewTask extends AsyncTask<Void, Void, String>
    {

        String endpoint = "/reviews";
        String id = getMovieId();

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String ReviewJsonStr = null;
            String baseUrl = "http://api.themoviedb.org/3/movie/";

            //Insert your own API key in /res/strings.xml
            String APIKEY = "?api_key=" + getResources().getString(R.string.api_key);


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(baseUrl +  id  + endpoint + APIKEY);



                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    ReviewJsonStr = null;
                }
                if (inputStream != null)
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                assert reader != null;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // builder for debugging.
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    ReviewJsonStr = null;
                }
                ReviewJsonStr = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                ReviewJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            Log.v("MovieActivityFragment:", ReviewJsonStr);
            return null;
        }
    }


    public class FetchTrailerTask extends AsyncTask<Void, Void, String>
    {

        String endpoint = "/videos";
        String id = getMovieId();

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String ReviewJsonStr = null;
            String baseUrl = "http://api.themoviedb.org/3/movie/";


            //Insert your own API key in /res/strings.xml
            String APIKEY = "?api_key=" + getResources().getString(R.string.api_key);


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(baseUrl +  id  + endpoint + APIKEY);


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    ReviewJsonStr = null;
                }
                if (inputStream != null)
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                assert reader != null;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // builder for debugging.
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    ReviewJsonStr = null;
                }
                ReviewJsonStr = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                ReviewJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            Log.v("MovieActivityFragment:", ReviewJsonStr);
            return null;
        }
    }

}



