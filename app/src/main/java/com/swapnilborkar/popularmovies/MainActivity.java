package com.swapnilborkar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private PostersAdapter postersAdapter;
    private ArrayList<PopularMovies> popularMovies;

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


}


