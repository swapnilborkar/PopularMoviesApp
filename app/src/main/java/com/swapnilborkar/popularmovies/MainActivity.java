package com.swapnilborkar.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.BindColor;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectedListener {

    private static String sortMode;
    @Bind(R.id.spinner)
    Spinner spinner;
    @BindColor(R.color.colorPrimary)
    int primaryColor;
    @BindColor(R.color.colorPrimaryDark)
    int primaryColorDark;
    private Boolean dualPaneLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);


            isDualPane();

            MainFragment fragment = new MainFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_main, fragment);
            transaction.commit();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();

        } else {
            getSupportFragmentManager().popBackStack();

        }
    }


    public Boolean isDualPane() {
        dualPaneLayout = findViewById(R.id.container) != null;
        return dualPaneLayout;
    }

    public void showSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        assert spinner != null;
        spinner.setVisibility(View.VISIBLE);

    }

    public void hideSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        assert spinner != null;
        spinner.setVisibility(View.GONE);
    }

    public void showConnectivityError() {
        ImageView errorImage = (ImageView) findViewById(R.id.img_error);
        assert errorImage != null;
        errorImage.setVisibility(View.VISIBLE);

    }

    public void setSpinner(int sort) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movie_sort, R.layout.toolbar_spinner_item);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assert spinner != null;
        spinner.setAdapter(adapter);
        if (sort == 0)
            spinner.setSelection(0);
        else
            spinner.setSelection(1);

        //Spinner on Start
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                SharedPreferences.Editor editor = getPreferences(0).edit();
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                assert spinner != null;
                int selectedPosition;
                MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main);

                switch (position) {

                    case 0:

                        selectedPosition = spinner.getSelectedItemPosition();
                        editor.putInt("spinnerSelection", selectedPosition);
                        editor.apply();
                        sortMode = "popular";
                        fragment.getSortMode(sortMode);
                        fragment.updateMovies();

                        break;

                    case 1:

                        selectedPosition = spinner.getSelectedItemPosition();
                        editor.putInt("spinnerSelection", selectedPosition);
                        editor.apply();
                        sortMode = "top_rated";
                        fragment.getSortMode(sortMode);
                        fragment.updateMovies();
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    @Override
    public void onMovieSelected(PopularMovies selectedMovie) {

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle movieBundle = new Bundle();
        movieBundle.putString("movie_title", selectedMovie.getTitle());
        movieBundle.putInt("movie_id", selectedMovie.getId());
        movieBundle.putString("movie_poster_url", selectedMovie.getImageUrl());
        movieBundle.putString("movie_backdrop_url", selectedMovie.getBackDropUrl());
        movieBundle.putString("movie_synopsis", selectedMovie.getSynopsis());
        movieBundle.putDouble("movie_rating", selectedMovie.getRating());
        movieBundle.putDouble("movie_popularity", selectedMovie.getPopularity());
        movieBundle.putString("movie_release_date", selectedMovie.getReleaseDate());
        movieDetailFragment.setArguments(movieBundle);

        // Master detail flow logic
        if (dualPaneLayout) {
            // Replace using child fragment manager and the detail frame layout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, movieDetailFragment).commit();
        } else {
            // Replace using activity fragment manager and the main frame layout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_main, movieDetailFragment).addToBackStack("item_stack").commit();

        }


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


}







