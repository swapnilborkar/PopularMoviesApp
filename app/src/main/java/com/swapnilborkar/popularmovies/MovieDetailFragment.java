package com.swapnilborkar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

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
    @Bind(R.id.fab_favorite_movies)
    FloatingActionButton fab;
    @Bind(R.id.pb_backdrop)
    ProgressBar backdropPb;
    ImageView playBack;
    ImageView backdropImage;

    ArrayList<MovieTrailer> trailers = new ArrayList<>();
    private List<String> movieReviewList = new ArrayList<>();
    private List<String> movieTrailerListKey = new ArrayList<>();
    private List<String> movieTrailerListName = new ArrayList<>();

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
        final View rootView = inflater.inflate(R.layout.fragment_movie, container, false);


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_favorite_movies);
        backdropPb = (ProgressBar) rootView.findViewById(R.id.pb_backdrop);
        playBack = (ImageView) rootView.findViewById(R.id.VideoPreviewPlayButton);
        Boolean dualPane = ((MainActivity) getActivity()).isDualPane();
        if (!dualPane) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString(MovieData.getMovieTitle()));
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Loading Image and Applying Pal
        String baseUrl = "http://image.tmdb.org/t/p/w500/";

        int w;
        int h;

        final String backdropBaseUrl = "http://image.tmdb.org/t/p/w780/";
        String backdropUrl = getArguments().getString("movie_backdrop_url", "null");
        backdropImage = (ImageView) rootView.findViewById(R.id.img_backdrop);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            w = getResources().getDisplayMetrics().widthPixels; //dividing by numColumns
            h = (int) (w * 1.5); //adjusting height to 1.5x times the width
        } else {
            w = getResources().getDisplayMetrics().widthPixels; //dividing by numColumns
            h = getResources().getDisplayMetrics().heightPixels / 3; //adjusting height to 1.5x times the width
        }

//        Log.e("Width = ", Integer.toString(w));
//        Log.e("Height =", Integer.toString(h));

        Picasso.with(getContext())
                .load(backdropBaseUrl + backdropUrl)
                .resize(w, h)
                .centerCrop()
                .placeholder(R.drawable.backdrop_placeholder)
                .transform(PaletteTransformation.instance())
                .into(backdropImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                        backdropPb.setVisibility(View.GONE);
                        playBack.setVisibility(View.VISIBLE);
                        fab.show();


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

//        Log.e("Width = ", Integer.toString(w));
//        Log.e("Height =", Integer.toString(h));


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

//        Log.e("Rating Length:", Integer.toString(rating.length()));

        if (rating.length() > 3)
        movieRating.setText(rating);

        else {
            rating = rating + "0";
            movieRating.setText(rating);
        }




        final TextView movieReleaseDate = (TextView) rootView.findViewById(R.id.txt_release);
        String releaseYear = getArguments().getString("movie_release_date", "null");
        movieReleaseDate.setText(releaseYear.substring(0, 4));

        final TextView moviePopularity = (TextView) rootView.findViewById(R.id.txt_popularity);
        String popularity = String.valueOf(getArguments().getDouble("movie_popularity", 0.0));
        moviePopularity.setText(popularity.substring(0, 4));


        fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));


        final ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_MOVE) {

                    fab.hide();
                }

                if (action == MotionEvent.ACTION_DOWN) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            fab.show();

                        }
                    }, 2000);
                }
                return false;
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View fabView = rootView.findViewById(R.id.fab_coordinator_view);
                Snackbar snackbar;
                snackbar = Snackbar.make(fabView, getArguments().getString(MovieData.getMovieTitle()) + " is added to your favorites!", Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.WHITE);
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });

        FetchReviewTask reviewTask = new FetchReviewTask();
        reviewTask.execute();

        ListView reviewListView = (ListView) rootView.findViewById(R.id.review_list_view);
        reviewListView.setFocusable(false);

        reviewListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        fab.show();
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        fab.hide();
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        ListView trailerListView = (ListView) rootView.findViewById(R.id.trailer_list_view);
        TrailerAdapter ta = new TrailerAdapter(getContext(), trailers);
        trailerListView.setAdapter(ta);

        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.get(position).getKey())));
            }
        });

        FetchTrailerTask trailerTask = new FetchTrailerTask();
        trailerTask.execute();

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String getMovieId() {
        return Integer.toString(getArguments().getInt("movie_id", 0));
    }


    public class FetchReviewTask extends AsyncTask<Void, Void, String> {

        String endpoint = "/reviews";
        String id = getMovieId();

        private void getReviewDataFromJson(String reviewJsonStr) throws JSONException {
            //get the root "result" array
            JSONObject reviewObject = new JSONObject(reviewJsonStr);
            JSONArray reviewArray = reviewObject.getJSONArray("results");

            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);

                movieReviewList.add(i, review.getString("content"));
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String reviewJsonStr = null;
            String baseUrl = "http://api.themoviedb.org/3/movie/";

            //Insert your own API key in /res/strings.xml
            String API_KEY = "?api_key=" + ApiKeys.tMDB_API_KEY;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(baseUrl + id + endpoint + API_KEY);
                Log.e("Review String: ", url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    reviewJsonStr = null;
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
                    reviewJsonStr = null;
                }
                reviewJsonStr = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                reviewJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                getReviewDataFromJson(reviewJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
//            Log.i(LOG_TAG, "Result review is:   " + movieReviewList);

            ArrayList<MovieReviews> reviews = new ArrayList<>();

            for (int i = 0; i < movieReviewList.size(); i++) {
                MovieReviews review = new MovieReviews();
                review.content = movieReviewList.get(i);
                reviews.add(review);
            }

            ListView reviewListView = (ListView) getView().findViewById(R.id.review_list_view);
            ReviewAdapter ra = new ReviewAdapter(getContext(), reviews);
            reviewListView.setAdapter(ra);
        }
    }


    public class FetchTrailerTask extends AsyncTask<Void, Void, String> {

        String endpoint = "/videos";
        String id = getMovieId();

        private void getTrailerDataFromJson(String trailerJsonStr) throws JSONException {
            //get the root "result" array
            JSONObject trailerObject = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = trailerObject.getJSONArray("results");
            //base Url for the TrailerInfo
            final String YoutubeBaseUrl = "https://www.youtube.com/watch?v=";

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                if (trailer.getString("site").contentEquals("YouTube")) {
                    movieTrailerListKey.add(i, YoutubeBaseUrl + trailer.getString("key"));
                    movieTrailerListName.add(i, trailer.getString("name"));
                }
                Log.i(LOG_TAG, " YouTube URL is: " + movieTrailerListKey.get(i));
                Log.i(LOG_TAG, " YouTube Trailer name is: " + movieTrailerListName.get(i));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String trailerJsonString = null;
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
                    trailerJsonString = null;
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
                    trailerJsonString = null;
                }
                trailerJsonString = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                trailerJsonString = null;
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

            try {
                getTrailerDataFromJson(trailerJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Log.v("MovieDetailFragment:", ReviewJsonStr);
            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            Log.i(LOG_TAG, "Result trailer url is:   " + movieTrailerListKey);
            Log.i(LOG_TAG, "Result trailer name is:   " + movieTrailerListName);

            // get all member variables in place
            for (int i = 0; i < movieTrailerListKey.size(); i++) {
                final MovieTrailer trailer = new MovieTrailer();
                trailer.name = movieTrailerListName.get(i);
                trailer.key = movieTrailerListKey.get(i);
                trailers.add(trailer);

                backdropImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getKey())));
                    }
                });


            }
        }
    }

}



