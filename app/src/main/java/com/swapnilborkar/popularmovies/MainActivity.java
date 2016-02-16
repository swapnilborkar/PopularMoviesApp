package com.swapnilborkar.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {



    private PostersAdapter postersAdapter;
    private ArrayList<PopularMovies> popularMovies;
    private String LOG_TAG = MainActivity.class.getSimpleName();

    private void UpdateMovies() {
        //Executes the background Network Call
        FetchMovieTask task = new FetchMovieTask();
        task.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        UpdateMovies();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        PopularMovies[] popularMovies = {
                new PopularMovies("Dirty Grandpa", getText(R.string.fake_movie_summary).toString(), R.drawable.m1),
                new PopularMovies("Ride Along 2", getText(R.string.fake_movie_summary).toString(), R.drawable.m2),
                new PopularMovies("Deadpool", getText(R.string.fake_movie_summary).toString(), R.drawable.m3),
                new PopularMovies("Batman v Superman: Dawn of Justice", getText(R.string.fake_movie_summary).toString(), R.drawable.m4),
                new PopularMovies("Captain America: Civil War", getText(R.string.fake_movie_summary).toString(), R.drawable.m5),
                new PopularMovies("Suicide Squad", getText(R.string.fake_movie_summary).toString(), R.drawable.m6),
                new PopularMovies("Get A Job", getText(R.string.fake_movie_summary).toString(), R.drawable.m7),
                new PopularMovies("The Jungle Book", getText(R.string.fake_movie_summary).toString(), R.drawable.m8)
        };

        postersAdapter = new PostersAdapter(this, Arrays.asList(popularMovies));
        gridView.setAdapter(postersAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(MainActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();

                PopularMovies popularMovie = postersAdapter.getItem(position);
                String title = popularMovie.movieName;
                String synopsis = popularMovie.movieSummary;


                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra("Position", Integer.toString(position));
                intent.putExtra("Movie Name", title);
                intent.putExtra("Synopsis", synopsis);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String MoviesJsonStr = null;
            String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";

            //ToDo: Remove API Key before pushing to Github!
            //Insert your own API key in /res/strings.xml
            String APIKEY = "&api_key=" + getResources().getString(R.string.api_key);

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(baseUrl + APIKEY);


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
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
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    MoviesJsonStr = null;
                }
                MoviesJsonStr = buffer.toString();
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

            Log.v(LOG_TAG, MoviesJsonStr);
            return MoviesJsonStr;
        }


    }
}



