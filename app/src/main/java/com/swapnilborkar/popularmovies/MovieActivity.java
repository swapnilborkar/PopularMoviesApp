package com.swapnilborkar.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MovieActivity extends AppCompatActivity {

    private String LOG_TAG = MovieActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView textView = (TextView)findViewById(R.id.txtMoviePosition);

        String position =  getIntent().getStringExtra("Position");

        if (position!=null)
        {
            textView.setText(position);
        }





    }}
