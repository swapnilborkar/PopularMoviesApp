package com.swapnilborkar.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
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
public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = "MovieDetailFragment:";
    public static Palette palette;
    int defaultColor;
    int colorAccent;
    int lightVibrantColor;
    int alternateColor;

    public MovieDetailFragment() {


    }


    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);


        //assert ((AppCompatActivity)getActivity()).getSupportActionBar() != null;
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Loading Image and Applying Pal
        String baseUrl = "http://image.tmdb.org/t/p/w780/";

        int w;
        int h;

        final String backdropBaseUrl = "http://image.tmdb.org/t/p/w780/";
        String backdropUrl = getArguments().getString("movie_backdrop_url", "null");
        final ImageView backdropImage = (ImageView) rootView.findViewById(R.id.img_backdrop);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            w = getResources().getDisplayMetrics().widthPixels; //dividing by numColumns
            h = (int) (w * 1.5); //adjusting height to 1.5x times the width
        } else {
            w = getResources().getDisplayMetrics().widthPixels; //dividing by numColumns
            h = getResources().getDisplayMetrics().heightPixels / 3; //adjusting height to 1.5x times the width
        }

        Picasso.with(getContext())
                .load(backdropBaseUrl + backdropUrl)
                .resize(w, h)
                .centerCrop()
                .transform(PaletteTransformation.instance())
                .into(backdropImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) backdropImage.getDrawable()).getBitmap();
                        palette = PaletteTransformation.getPalette(bitmap);

                        defaultColor = getColor(getContext(), R.color.colorPrimary);
                        colorAccent = getColor(getContext(), R.color.colorAccent);
                        lightVibrantColor = palette.getLightVibrantColor(defaultColor);


                        if (lightVibrantColor != defaultColor) {
                            float[] hsv = new float[3];
                            Color.colorToHSV(lightVibrantColor, hsv);
                            hsv[2] *= 0.8f;
                            int colorPrimaryDark = Color.HSVToColor(hsv);


                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(lightVibrantColor));

                            if (Build.VERSION.SDK_INT >= 21)
                                getActivity().getWindow().setStatusBarColor(colorPrimaryDark);
                        }

                        if (lightVibrantColor == defaultColor) {
                            alternateColor = palette.getLightMutedColor(defaultColor);

                            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(alternateColor));
                            float[] hsv = new float[3];
                            Color.colorToHSV(alternateColor, hsv);
                            hsv[2] *= 0.8f;
                            int colorPrimaryDark = Color.HSVToColor(hsv);

                            if (Build.VERSION.SDK_INT >= 21)
                                getActivity().getWindow().setStatusBarColor(colorPrimaryDark);
                        }
                    }
                });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            w = getResources().getDisplayMetrics().widthPixels / 6; //dividing by numColumns
            h = (int) (w * 1.5); //adjusting height to 1.5x times the width
        } else {
            w = getResources().getDisplayMetrics().widthPixels / 2; //dividing by numColumns
            h = (int) (w * 1.5); //adjusting height to 1.5x times the width
        }


        final ImageView moviePoster = (ImageView) rootView.findViewById(R.id.img_poster2);
        String posterURL = getArguments().getString("movie_poster_url");


        assert moviePoster != null;
        Picasso.with(getContext())
                .load(baseUrl + posterURL)
                .resize(w, h)
                .centerCrop()
                .error(R.drawable.icon_cry)
                .placeholder(R.drawable.placeholder)
                .transform(PaletteTransformation.instance())
                .into(moviePoster);


        TextView movieSynopsis = (TextView) rootView.findViewById(R.id.txt_overview);
        movieSynopsis.setText(getArguments().getString("movie_synopsis", "null"));

        if (movieSynopsis.getText() == null) {
            movieSynopsis.setText(getResources().getText(R.string.no_synopsis));
        }


        final TextView movieRating = (TextView) rootView.findViewById(R.id.txt_rating);
        String rating = String.valueOf(getArguments().getDouble("movie_rating", 0.00));
        movieRating.setText(rating);

        final TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.txt_release);
        String releaseYear = getArguments().getString("movie_release_date", "null");
        movieReleaseDate.setText(releaseYear.substring(0, 4));

        final TextView moviePopularity = (TextView) rootView.findViewById(R.id.txt_popularity);
        String popularity = String.valueOf(getArguments().getDouble("movie_popularity", 0.0));
        moviePopularity.setText(popularity.substring(0, 4));


        FetchReviewTask reviewTask = new FetchReviewTask();
        reviewTask.execute();

        FetchTrailerTask trailerTask = new FetchTrailerTask();
        trailerTask.execute();

        return rootView;
    }


    public String getMovieId() {
        return Integer.toString(getArguments().getInt("movie_id", 0));
    }


    public class FetchReviewTask extends AsyncTask<Void, Void, String> {

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
            String API_KEY = "?api_key=" + ApiKeys.tMDB_API_KEY;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(baseUrl + id + endpoint + API_KEY);


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

            Log.v("MovieDetailFragment:", ReviewJsonStr);
            return null;
        }
    }


    public class FetchTrailerTask extends AsyncTask<Void, Void, String> {

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
            String API_KEY = "?api_key=" + ApiKeys.tMDB_API_KEY;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(baseUrl + id + endpoint + API_KEY);


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

            Log.v("MovieDetailFragment:", ReviewJsonStr);
            return null;
        }
    }

}



