package com.swapnilborkar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.spinner) Spinner spinner;
    private PostersAdapter postersAdapter;
    private ArrayList<PopularMovies> popularMovies;
    private String LOG_TAG = MainActivity.class.getSimpleName();
    private static String sortMode;


    private void UpdateMovies() {
        //Executes the background Network Call

        FetchMovieTask task = new FetchMovieTask();
        task.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        checkConnection();
        SharedPreferences prefs = getPreferences(0);
        spinner.setSelection(prefs.getInt("spinnerSelection", 0));


        //Spinner on Start
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                SharedPreferences.Editor editor = getPreferences(0).edit();
                int selectedPosition;

                switch (position) {


                    case 0:

                        selectedPosition = spinner.getSelectedItemPosition();
                        editor.putInt("spinnerSelection", selectedPosition);
                        editor.apply();
                        sortMode = "popular";
                        if (checkConnection()) {
                            UpdateMovies();
                        }
                        break;

                    case 1:


                        selectedPosition = spinner.getSelectedItemPosition();
                        editor.putInt("spinnerSelection", selectedPosition);
                        editor.apply();
                        sortMode = "top_rated";
                        if (checkConnection()) {
                            UpdateMovies();
                        }
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


    }


    public boolean checkConnection() {
        //Checks if Internet Connection is Available
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        ImageView noInternet = (ImageView) findViewById(R.id.no_internet);
        TextView txtNoInternet = (TextView) findViewById(R.id.txt_no_internet);


        if (isConnected) {

            noInternet.setVisibility(View.GONE);
            txtNoInternet.setVisibility(View.GONE);
            UpdateMovies();
            return true;

        } else {


            noInternet.setVisibility(View.VISIBLE);
            txtNoInternet.setVisibility(View.VISIBLE);
            return false;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movie_sort, R.layout.toolbar_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final GridView gridView = (GridView) findViewById(R.id.grid_view);
        postersAdapter = new PostersAdapter(this, new ArrayList<PopularMovies>());
        gridView.setAdapter(postersAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                PopularMovies popularMovies = postersAdapter.getItem(position);
                int movie_id = popularMovies.id;
                String url = popularMovies.imageUrl;
                String backdrop = popularMovies.backDropUrl;
                String title = popularMovies.title;
                String releaseDate = popularMovies.releaseDate;
                String synopsis = popularMovies.synopsis;
                double rating = popularMovies.rating;
                String ratingString = String.valueOf(rating);
                double popularity = popularMovies.popularity;
                String popularityString = String.valueOf(popularity);


                Intent intent = new Intent(MainActivity.this, MovieActivity.class)
                        .putExtra("id", movie_id)
                        .putExtra("url", url)
                        .putExtra("title", title)
                        .putExtra("release", releaseDate)
                        .putExtra("synopsis", synopsis)
                        .putExtra("rating", ratingString)
                        .putExtra("popularity", popularityString)
                        .putExtra("backdrop", backdrop);

                startActivity(intent);
            }
        });


    }


    public ArrayList<PopularMovies> getPopularMoviesFromJson(String MovieJsonString) throws JSONException {


        ArrayList<PopularMovies> popularMoviesArrayList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(MovieJsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("results");


        //parsing every entry in the array using a loop
        for (int i = 0; i < jsonArray.length(); i++) {
            String imageUrl = jsonArray.getJSONObject(i).getString("poster_path");
            int id = jsonArray.getJSONObject(i).getInt("id");
            String title = jsonArray.getJSONObject(i).getString("original_title");
            String synopsis = jsonArray.getJSONObject(i).getString("overview");
            String releaseDate = jsonArray.getJSONObject(i).getString("release_date");
            double rating = jsonArray.getJSONObject(i).getDouble("vote_average");
            double popularity = jsonArray.getJSONObject(i).getDouble("popularity");
            String backDropUrl = jsonArray.getJSONObject(i).getString("backdrop_path");

            PopularMovies movie = new PopularMovies(imageUrl, id, title, synopsis, releaseDate, rating, popularity, backDropUrl);
            popularMoviesArrayList.add(movie);

        }

        return popularMoviesArrayList;
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public class FetchMovieTask extends AsyncTask<Void, Void, String> {


        ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pb.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String MoviesJsonStr = null;
            String baseUrl = "http://api.themoviedb.org/3/movie/";


            //Insert your own API key in /res/strings.xml
            String APIKEY = "?api_key=" + getResources().getString(R.string.api_key);


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                if (sortMode ==null) {
                   sortMode = "popular";
                }

                URL url = new URL(baseUrl + sortMode + APIKEY);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    MoviesJsonStr = null;
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
                    MoviesJsonStr = null;
                }
                MoviesJsonStr = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                MoviesJsonStr = null;
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

            //Log.v(LOG_TAG, MoviesJsonStr);
            return MoviesJsonStr;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                postersAdapter.clear();
                try {
                    popularMovies = getPopularMoviesFromJson(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                {
                for (PopularMovies movie : popularMovies) {

                    postersAdapter.add(movie);

                }

                    pb.setVisibility(View.GONE);
                }
            }
        }
    }
}



