package com.swapnilborkar.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

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

public class MainFragment extends Fragment {

    private static String sortMode;
    @Bind(R.id.spinner)
    Spinner spinner;

    //    @Bind(R.id.progress_bar)
//    ProgressBar pb;
    @Bind(R.id.grid_view)
    GridView gridView;
    OnMovieSelectedListener movieSelectedListener;
    ArrayList<PopularMovies> popularMovies;
    Activity mainActivity;
    ArrayList<PopularMovies> moviesArrayList = new ArrayList<>();
    private PostersAdapter postersAdapter;
    private String LOG_TAG = MainActivity.class.getSimpleName();


    public MainFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setSpinner();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (Activity) context;
        }
    }

    public void setMovieSelectedListener(OnMovieSelectedListener movieSelectedListener) {
        // Setting this fragment as a listener for when a FeedItem is clicked
        this.movieSelectedListener = movieSelectedListener;
    }

    public void getSortMode(String sortMode) {
        MainFragment.sortMode = sortMode;
    }

    public void updateMovies() {
        //Executes the background Network Call

        FetchMovieTask task = new FetchMovieTask();
        task.execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).showSpinner();




        final GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
        if (((MainActivity) getActivity()).isDualPane()) {
            gridView.setNumColumns(2);
        }
        postersAdapter = new PostersAdapter(getActivity(), moviesArrayList);
        gridView.setAdapter(postersAdapter);
        setMovieSelectedListener((OnMovieSelectedListener) mainActivity);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                movieSelectedListener.onMovieSelected(moviesArrayList.get(position));
                ((MainActivity) getActivity()).hideSpinner();



            }
        });


        return rootView;
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

    public interface OnMovieSelectedListener {
        // Interface for when a MovieItem is clicked
        void onMovieSelected(PopularMovies selectedMovie);



    }

    public class FetchMovieTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //pb.setVisibility(View.VISIBLE);


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
            String API_KEY = "?api_key=" + ApiKeys.tMDB_API_KEY;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                if (sortMode == null) {
                    sortMode = "popular";
                }

                URL url = new URL(baseUrl + sortMode + API_KEY);

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

                    //pb.setVisibility(View.GONE);
                }
            }
        }
    }
}




